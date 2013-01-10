package infodoc.core.container;

import infodoc.core.InfodocConstants;
import infodoc.core.dto.Notification;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.NotificationInstance;
import infodoc.core.dto.ActivityInstance;
import infodoc.core.dto.User;
import infodoc.core.job.NotificationJob;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.LoggerFactory;

import enterpriseapp.EnterpriseApplication;

@SuppressWarnings("unchecked")
public class NotificationInstanceContainer extends UserGroupFilteredContainer<NotificationInstance> {

	private static final long serialVersionUID = 1L;

	public NotificationInstanceContainer() {
		super(NotificationInstance.class, "activityInstance.user.userGroup.parentUserGroup.id");
	}
	
	@Override
	public Serializable saveOrUpdateEntity(NotificationInstance instance) {
		cancelPreviousInstances(instance.getActivityInstance());
		
		Serializable id = super.saveOrUpdateEntity(instance);
		instance.setId(id);
		schedule(instance);
		
		return id;
	}
	
	public void cancelPreviousInstances(ActivityInstance activityInstance) {
		ActivityInstance previous = InfodocContainerFactory.getActivityInstanceContainer().findPreviousInstance(activityInstance);
		
		if(previous != null) {
			List<Long> previousInstances = specialQuery("select id from NotificationInstance where notification.automaticallyCancel is true and sent is false and canceled is false and activityInstance.id = ?", new Object[] {previous.getId()});
			
			if(!previousInstances.isEmpty()) {
				LoggerFactory.getLogger(NotificationInstanceContainer.class).info("Notification instances canceled (ids=" + previousInstances + ")");
				update(
					"update NotificationInstance" +
					" set canceled = true" +
					" where id in(:previousInstances)",
					null, null,new String[] {"previousInstances"}, new Collection[] {previousInstances});
			}
		}
	}

	public void createNotificationInstance(ActivityInstance activityInstance) {
		cancelPreviousInstances(activityInstance);
		NotificationContainer notificationContainer = InfodocContainerFactory.getNotificationContainer();
		List<Notification> notifications = notificationContainer.findByActivityId(activityInstance.getActivity().getId());
		
		for(Notification notification : notifications) {
			NotificationInstance notificationInstance = new NotificationInstance();
			notificationInstance.setCanceled(false);
			notificationInstance.setSent(false);
			notificationInstance.setNotification(notification);
			notificationInstance.setActivityInstance(activityInstance);
			notificationInstance.setSendTime(getNotificationTime(activityInstance, notification));
			
			saveOrUpdateEntity(notificationInstance);
		}
	}
	
	public Date getNotificationTime(ActivityInstance activityInstance, Notification notification) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, notification.getSendAfterMinutes() == null ? 0 : notification.getSendAfterMinutes());
		calendar.add(Calendar.HOUR_OF_DAY, notification.getSendAfterHours() == null ? 0 : notification.getSendAfterHours());
		calendar.add(Calendar.DAY_OF_MONTH, notification.getSendAfterDays() == null ? 0 : notification.getSendAfterDays());
		calendar.add(Calendar.MONTH, notification.getSendAfterMonths() == null ? 0 : notification.getSendAfterMonths());
		
		if(
			(notification.getSendAfterMinutes() == null || notification.getSendAfterMinutes().equals(0))
			&& (notification.getSendAfterHours() == null || notification.getSendAfterHours().equals(0))
			&& (notification.getSendAfterDays() == null || notification.getSendAfterDays().equals(0))
			&& (notification.getSendAfterMonths() == null || notification.getSendAfterMonths().equals(0))
		) {
			calendar.add(Calendar.SECOND, 5); // to avoid scheduling before current time  
		}
		
		return calendar.getTime();
	}

	public void schedulePendingInstances() {
		Date now = Calendar.getInstance().getTime();
		List<NotificationInstance> instances = query(
			"select ni" +
			" from NotificationInstance ni" +
			" where ni.notification.disabled is false" +
			" and ni.sent is false" +
			" and ni.canceled is false" +
			" and ni.sendTime > ?",
			new Object[] {now});
		
		for(NotificationInstance instance : instances) {
			schedule(instance);
		}
	}

	public void schedule(NotificationInstance instance) {
		try {
			if(!instance.getSent() && !instance.getCanceled()) {
				if(Calendar.getInstance().getTime().before(instance.getSendTime())) {
					JobKey jobKey = new JobKey(instance.getId().toString());
					
					if(EnterpriseApplication.getScheduler().checkExists(jobKey)) {
						EnterpriseApplication.getScheduler().deleteJob(jobKey);
						LoggerFactory.getLogger(NotificationInstanceContainer.class).info("Notification instance deleted (id=" + instance.getId() + ")");
					}
					
					JobDetail job = JobBuilder.newJob(NotificationJob.class).withIdentity(jobKey).build();
					
					Trigger trigger = TriggerBuilder.newTrigger()
						.startAt(instance.getSendTime())
						.usingJobData("notificationInstanceId", instance.getId()) // inyected via NotificationJob.setNotificationInstanceId()
						.build();
					
					EnterpriseApplication.getScheduler().scheduleJob(job, trigger);
					LoggerFactory.getLogger(NotificationInstanceContainer.class).info("Notification instance scheduled (id=" + instance.getId() + ")");
				}
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Collection<String> getRecipients(NotificationInstance instance) {
		HashSet<String> recipients = new HashSet<String>();
		Notification notification = instance.getNotification();
		
		if(notification.getNotifyCurrentUser()) {
			recipients.add(instance.getActivityInstance().getUser().getLogin());
		}
		
		if(notification.getNotifyNextUsers() && instance.getActivityInstance().getAssignedUsers() != null) {
			for(User user : instance.getActivityInstance().getAssignedUsers()) {
				recipients.add(user.getLogin());
			}
		}
		
		if(notification.notifyCurrentUserGroup()) {
			List<User> users = InfodocContainerFactory.getUserContainer().findByUserGroupId(instance.getActivityInstance().getUser().getUserGroup().getId());
			
			for(User user : users) {
				recipients.add(user.getLogin());
			}
		}
		
		if(notification.getNotifyNextUserGroups() && instance.getActivityInstance().getAssignedUserGroups() != null) {
			for(UserGroup userGroup : instance.getActivityInstance().getAssignedUserGroups()) {
				for(User user : userGroup.getUsers()) {
					recipients.add(user.getLogin());
				}
			}
		}
		
		if(notification.getUsers() != null) {
			for(User user : notification.getUsers()) {
				recipients.add(user.getLogin());
			}
		}
		
		if(notification.getEmail() != null && !notification.getEmail().isEmpty()) {
			recipients.add(notification.getEmail());
		}
		
		return recipients;
	}
	
	public String getMessage(NotificationInstance instance) {
		String message = InfodocConstants.infodocDefaultNotificationTemplate;
		Notification notification = instance.getNotification();
		PropertyValueContainer propertyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
		
		if(notification.getMessage() != null) {
			message = message.replace("${message}", notification.getMessage());
		} else {
			message = message.replace("${message}", "");
		}
		
		String values = "";
		
		if(notification.getIncludeProperties()) {
			values += "<i>" + InfodocConstants.uiFormNumber + "</i>: " + instance.getActivityInstance().getCaseDto().getNumber();
			values += "<br/>";
			
			for(PropertyValue value : instance.getActivityInstance().getCaseDto().getPropertyValues()) {
				String valorString = propertyValueContainer.getStringValue(value);
				
				if(valorString != null && !valorString.isEmpty()) {
					values += "<i>" + value.getProperty().getName() + "</i>: " + valorString;
					values += "<br/>";
				}
			}
			
			message = message.replace("${properties}", values);
		} else {
			message = message.replace("${properties}", "");
		}
		
		return message;
	}

}
