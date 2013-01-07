package infodoc.core.container;

import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.ActivityInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class ActivityInstanceContainer extends UserGroupFilteredContainer<ActivityInstance> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Serializable saveEntity(ActivityInstance activityInstance) {
		Serializable id = super.saveEntity(activityInstance);
		InfodocContainerFactory.getNotificationInstanceContainer().createNotificationInstance(activityInstance);
		
		return id;
	}
	
	public ActivityInstanceContainer() {
		super(ActivityInstance.class, "user.userGroup.parentUserGroup.id");
	}

	public List<ActivityInstance> findLast(int count) {
		String condition = getHqlConditionToFilterByUserGroup();
		
		if(!condition.isEmpty()) {
			condition = "where ai." + condition;
		}
		
		return query(
			"select distinct ai" +
			" from ActivityInstance ai " + condition +
			" order by ai.executionTime desc",
			null, null, null, null, count, null);
	}

	public Long countByActivityIdAndUserId(Long activityId, Long userId, Date from, Date to) {
		return (Long) singleSpecialQuery(
			"select count(distinct ai.id)" +
			" from ActivityInstance ai join ai.user u" +
			" where ai.activity.id = ?" +
			" and u.id = ?" +
			" and date(ai.executionTime) >= date(?)" +
			" and date(ai.executionTime) <= date(?)",
			new Object[] {activityId, userId, from, to}
		);
	}

	public Long countByActivityIdAndGroupId(Long activityId, Long userGroupId, Date from, Date to) {
		return (Long) singleSpecialQuery(
			"select count(distinct ai.id)" +
			" from ActivityInstance ai" +
			" join ai.user.userGroup g" +
			" where ai.activity.id = ?" +
			" and g.id = ?" +
			" and date(ai.executionTime) >= date(?)" +
			" and date(ai.executionTime) <= date(?)",
			new Object[] {activityId, userGroupId, from, to}
		);
	}
	
	public Long countByActivityId(Long activityId, Date from, Date to) {
		return (Long) singleSpecialQuery(
			"select count(distinct ai.id)" +
			" from ActivityInstance ai" +
			" where ai.activity.id = ?" +
			" and ai.executionTime >= ?" +
			" and ai.executionTime < ?",
			new Object[] {activityId, from, to}
		);
	}

	public List<Date> findDatesHavingActivityInstances(Long processId, Date from, Date to) {
		return specialQuery(
			"select distinct(ai.executionTime)" +
			" from ActivityInstance ai where" +
			" ai.activity.process.id = ?" +
			" and date(ai.executionTime) >= date(?)" +
			" and date(ai.executionTime) <= date(?)" +
			" order by ai.executionTime",
			new Object[] {processId, from, to}
		);
	}
	
	public Long getAverageTimeByActivityIdAndUserGroupId(Long activityId, Long userGroupId, Date from, Date to) {
		List<ActivityInstance> instances = listByActivityIdAndUserGroupId(activityId, userGroupId, from, to);
		return getAverageTime(instances);
	}

	public Long getAverageTimeByActivityIdAndUserId(Long activityId, Long userId, Date from, Date to) {
		List<ActivityInstance> instances = listByActivityIdAndUserId(activityId, userId, from, to);
		return getAverageTime(instances);
	}

	public Long getAverageTime(List<ActivityInstance> instances) {
		int count = instances.size();
		
		if(count == 0) {
			return 0l;
		}
		
		Long sum = 0l;
		
		for(ActivityInstance i : instances) {
			sum += getTimeSpent(i);
		}
		
		return sum / count;
	}

	public List<ActivityInstance> listByActivityIdAndUserGroupId(Long activityId, Long userGroupId, Date from, Date to) {
		return query(
			"select distinct ai from ActivityInstance ai" +
			" join ai.user.userGroup g" +
			" where ai.activity.id = ?" +
			" and g.id = ?" +
			" and date(ai.executionTime) >= date(?)" +
			" and date(ai.executionTime) <= date(?)",
			new Object[] {activityId, userGroupId, from, to}
		);
	}
	
	public List<ActivityInstance> listByActivityIdAndUserId(Long activityId, Long userId, Date from, Date to) {
		return query(
			"select distinct ai" +
			" from ActivityInstance ai" +
			" where ai.activity.id = ?" +
			" and ai.user.id = ?" +
			" and date(ai.executionTime) >= date(?)" +
			" and date(ai.executionTime) <= date(?)",
			new Object[] {activityId, userId, from, to}
		);
	}
	
	public long getTimeSpent(ActivityInstance instance) {
		ActivityInstance previous = findPreviousInstance(instance);
		
		if(previous == null) {
			return 0;
		}
		
		return (instance.getExecutionTime().getTime() - previous.getExecutionTime().getTime()) / 1000;
	}

	public ActivityInstance findPreviousInstance(ActivityInstance instance) {
		ProcessInstance processInstance = InfodocContainerFactory.getProcessInstanceContainer().getEntity(instance.getProcessInstance().getId());
		ActivityInstance previous = null;
		
		if(processInstance.getActivityInstances() != null) {
			for(ActivityInstance i : processInstance.getActivityInstances()) {
				if(!i.equals(instance)) {
					previous = i;
				} else {
					break;
				}
			}
		}
		
		return previous;
	}

}
