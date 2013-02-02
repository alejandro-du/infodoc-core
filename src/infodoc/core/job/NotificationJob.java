package infodoc.core.job;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.NotificationInstanceContainer;
import infodoc.core.dto.NotificationInstance;

import java.util.Collection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

import enterpriseapp.job.TransactionalJob;
import enterpriseapp.mail.MailSender;


public class NotificationJob extends TransactionalJob {
	
	private Long notificationInstanceId;

	@Override
	public void executeJob(JobExecutionContext context) throws JobExecutionException {
		NotificationInstanceContainer notificationInstanceContainer = InfodocContainerFactory.getNotificationInstanceContainer();
		NotificationInstance notification = notificationInstanceContainer.getEntity(notificationInstanceId);
		
		if(!notification.isCanceled() && !notification.isSent()) {
			sendEmail(notification);
			notification.setSent(true);
			notification.setCanceled(false);
			notificationInstanceContainer.saveOrUpdateEntity(notification);
		}
	}

	public void sendEmail(NotificationInstance instance) {
		try {
			NotificationInstanceContainer notificationInstanceContainer = InfodocContainerFactory.getNotificationInstanceContainer();
			Collection<String> recipients = notificationInstanceContainer.getRecipients(instance);
			String message = notificationInstanceContainer.getMessage(instance);
			
			MailSender.send(recipients, instance.getNotification().getSubject(), message);
			
		} catch(Exception e) {
			LoggerFactory.getLogger(NotificationJob.class).error("Error sending notification email (id=" + notificationInstanceId + ")", e);
			throw new RuntimeException(e);
		}
	}
	
	public void setNotificationInstanceId(Long notificationId) {
		this.notificationInstanceId = notificationId;
	}

}
