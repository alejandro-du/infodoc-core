package infodoc.core.container;

import infodoc.core.dto.Property;
import infodoc.core.dto.ClassificationValue;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.ActivityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

@SuppressWarnings("unchecked")
public class ProcessInstanceContainer extends UserGroupFilteredContainer<ProcessInstance> {
	
	private static final long serialVersionUID = 1L;
	
	public ProcessInstanceContainer() {
		super(ProcessInstance.class, "process.userGroup.id");
	}

	@Override
	public synchronized Serializable saveEntity(ProcessInstance processInstance) {
		try {
			sessionManager.getSession().beginTransaction();
			
			Long nextValue = InfodocContainerFactory.getNumerationContainer().getNextValue(processInstance.getProcess().getId());
			processInstance.setNumber(nextValue);
			
			Serializable serializable = super.saveEntity(processInstance);
			
			return serializable;
			
		} catch (Exception e) {
			sessionManager.getSession().getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}
	
	public ProcessInstance getCurrentByNumber(Long number) {
		return singleQuery(
			"select pi" +
			" from ProcessInstance pi" +
			" where pi.number = ?" +
			" order by pi.id desc" +
			" limit 1", new Object[] {number});
	}
	
	public List<ProcessInstance> findMyProcessInstances(Long userId, Long processId) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" join pi.activityInstances ai" +
			" join ai.activity.nextActivities nextAct" +
			" left join ai.assignedUsers assignedU1" +
			" left join ai.assignedUserGroups assignedUG" +
			" left join assignedUG.users assignedU2" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.processInstance.id = pi.id)" +
			" and (assignedU1.id = ? or assignedU2.id = ?)" +
			" and (? = null or pi.process.id = ?)" +
			" order by ai.executionTime desc",
			new Object [] {userId, userId, processId, processId}
		);
	}

	public List<ProcessInstance> findAvailableByUserIdAndNextActivityId(Long userId, Long activityId) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" join pi.activityInstances ai" +
			" join ai.activity.nextActivities nextAct" +
			" left join ai.assignedUsers assignedU1" +
			" left join ai.assignedUserGroups assignedUG" +
			" left join assignedUG.users assignedU2" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.processInstance.id = pi.id)" +
			" and (assignedU1.id = ? or assignedU2.id = ?)" +
			" and nextAct.id = ?" +
			" order by ai.executionTime desc",
			new Object [] {userId, userId, activityId}
		);
	}

	public List<ProcessInstance> findAssignedToOtherUserByUserIdAndNextActivityId(Long userId, Long activityId) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" join pi.activityInstances ai" +
			" join ai.activity.nextActivities nextAct" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai.processInstance.id = pi.id)" +
			" and ai.user.id = ?" +
			" and nextAct.id = ?" +
			" and ai.user.id not in (select u.id from ActivityInstance ai2 join ai2.assignedUsers u where ai2.id = ai.id)",
			new Object [] {userId, activityId}
		);
	}
	
	public List<ProcessInstance> findAssignedByUserIdAndCurrentActivityId(Long userId, Long activityId) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" join pi.activityInstances ai" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.processInstance.id = pi.id)" +
			" and ai.user.id = ?" +
			" and ai.activity.id = ?",
			new Object [] {userId, activityId}
		);
	}
	
	public List<ProcessInstance> findByActivityId(Long activityId) {
		return query(
				"select distinct pi" +
				" from ProcessInstance pi" +
				" join pi.activityInstances ai" +
				" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.processInstance.id = pi.id)" +
				" and ai.activity.id = ?" +
				" and pi." + getHqlConditionToFilterByUserGroup(),
				new Object [] {activityId}
			);
	}
	
	public List<ProcessInstance> findPendingByProcessId(Long processId, Date from, Date to) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" join pi.activityInstance lastAi" +
			" join pi.activityInstance firstAi" +
			" where firstAi.id = (select min(ai.id) from ActivityInstance ai where ai.processInstance.id = pi.id)" +
			" and lastAi.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.processInstance.id = pi.id)" +
			" and pi.process.id = ?" +
			" and date(firstAi.executionTime) >= date(?)" +
			" and date(lastAi.executionTime) <= date(?)",
			new Object [] {processId, from, to}
		);
	}
	
	public List<ProcessInstance> findFinishedByProcessId(Long processId, Date from, Date to) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" join pi.activityInstances lastAi" +
			" join pi.activityInstances firstAi" +
			" left join lastAi.activity.nextActivities nextAct" +
			" where firstAi.id = (select min(ai.id) from ActivityInstance ai where ai.processInstance.id = pi.id)" +
			" and lastAi.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.processInstance.id = pi.id)" +
			" and pi.process.id = ?" +
			" and nextAct is null" +
			" and date(firstAi.executionTime) >= date(?)" +
			" and date(lastAi.executionTime) <= date(?)",
			new Object [] {processId, from, to}
		);
	}

	public List<ProcessInstance> findByNumberAndProcessId(Long number, Long processId) {
		return query(
			"select distinct pi" +
			" from ProcessInstance pi" +
			" where pi.process.id = ?" +
			" and pi.number = ?" +
			" and pi." + getHqlConditionToFilterByUserGroup(),
			new Object[] {number, processId});
	}
	
	// TODO: validate consistency
	public ProcessInstance updateInstance(ProcessInstance instance, List<PropertyValue> propertyValues, ActivityInstance activityInstance) {
		try {
			InfodocContainerFactory.getProcessInstanceContainer().saveOrUpdateEntity(instance);
			return saveActivityInstance(instance, propertyValues, activityInstance);
			
		} catch (Exception e) {
			sessionManager.getSession().getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}

	// TODO: validate consistency
	public ProcessInstance saveInstace(ProcessInstance instance, List<PropertyValue> propertyValues, ActivityInstance activityInstace) {
		try {
			InfodocContainerFactory.getProcessInstanceContainer().saveEntity(instance);
			return saveActivityInstance(instance, propertyValues, activityInstace);
			
		} catch (Exception e) {
			sessionManager.getSession().getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}
	
	private ProcessInstance saveActivityInstance(ProcessInstance processInstance, List<PropertyValue> propertyValues, ActivityInstance activityInstance) {
		activityInstance.setProcessInstance(processInstance);
		InfodocContainerFactory.getActivityInstanceContainer().saveEntity(activityInstance);
		
		if(propertyValues != null) {
			for(PropertyValue value : propertyValues) {
				InfodocContainerFactory.getPropertyValueContainer().saveOrUpdateEntity(value);
			}
		}
		
		return InfodocContainerFactory.getProcessInstanceContainer().getEntity(processInstance.getId());
	}
	
	public Object getValue(ProcessInstance pi, Property property) {
		if(pi.getPropertyValues() != null) {
			for(PropertyValue value : pi.getPropertyValues()) {
				if(value.getProperty().getId().equals(property.getId())) {
					return InfodocContainerFactory.getPropertyValueContainer().getValue(value);
				}
			}
		}
		
		return null;
	}
	
	public ActivityInstance getFisrtActivityInstance(ProcessInstance instance) {
		ActivityInstance activityInstance = null;
		
		if(instance.getActivityInstances() != null && !instance.getActivityInstances().isEmpty()) {
			activityInstance = (ActivityInstance) instance.getActivityInstances().toArray()[0];
		}
		
		return activityInstance;
	}

	public ActivityInstance getLastActivityInstance(ProcessInstance instance) {
		ActivityInstance activityInstance = null;
		
		if(instance.getActivityInstances() != null && !instance.getActivityInstances().isEmpty()) {
			activityInstance = (ActivityInstance) instance.getActivityInstances().toArray()[instance.getActivityInstances().size() - 1];
		}
		
		return activityInstance;
	}

	public Collection<Long> search(
		Process process,
		List<PropertyValue> propertyValues,
		String number,
		Activity activity,
		Date startDate,
		Date endDate,
		String initialUser,
		String endUser,
		String initialUserGroup,
		String endUserGroup,
		String comments,
		boolean lastActivityInstance,
		boolean pending
	) {
		List<Long> foundInstancesIds = new ArrayList<Long>();
		
		for(PropertyValue value : propertyValues) {
			String query =
			"select distinct pi.id" +
			" from ProcessInstance pi" +
			" left join pi.propertyValues value" +
			" left join value.classificationsValueValue classificationsValueValue" +
			" left join value.processInstancesValue processInstancesValue" +
			" left join pi.activityInstances activityInstance" +
			" left join activityInstance.assignedUsers assignedUser" +
			" left join activityInstance.user.userGroup initialUserGroup" +
			" left join activityInstance.assignedUserGroups assignedUserGroup1" +
			" left join assignedUser.userGroup assignedUserGroup2" +
			" where pi.process.id = :processId" +
			" :foundFilter" +
			" :lastAcitivityFilter" +
			" :pendingFilter" +
			" and ('' || :number is null or '' || pi.number like :number)" +
			" and ('' || :activityId is null or activityInstance.activity.id = :activityId)" +
			" and ('' || :startDate is null or activityInstance.executionTime >= :startDate)" +
			" and ('' || :endDate is null or activityInstance.executionTime <= :endDate)" +
			" and ('' || :initialUser is null or lower(activityInstance.user.login) like lower(:initialUser))" +
			" and ('' || :endUser is null or lower(assignedUser.login) like lower(:endUser))" +
			" and ('' || :initialUserGroup is null or lower(initialUserGroup.name) like lower(:initialUserGroup))" +
			" and ('' || :endUserGroup is null or lower(assignedUserGroup1.name) like lower(:endUserGroup) or lower(assignedUserGroup2.name) like lower(:endUserGroup))" +
			" and ('' || :comments is null or lower(activityInstance.comments) like lower(:comments))" +
			" and (" +
				" (" +
					" value.property.id = :propertyId" +
					" and (" +
						" (:stringValue is null or lower(value.stringValue) like lower(:stringValue))" +
						" and (:booleanValue is null or :booleanValue is false or value.booleanValue = :booleanValue)" +
						" and (:dateValue is null or date(value.dateValue) = date(:dateValue))" +
						" and (:longValue is null or value.longValue = :longValue)" +
						" :classificationsValueFilter" +
						" :processInstancesValueFilter" +
					" )" +
				" ) or " +
				" (" +
					" (value.property.id != :propertyId or value is null)" +
					" and (" +
						" (:stringValue is null)" +
						" and (:booleanValue is null)" +
						" and (:dateValue is null)" +
						" and (:longValue is null)" +
						" :falseClassificationsValueFilter" +
						" :falseProcessInstancesValueFilter" +
					" )" +
				" )" +
			" )";
			
			List<Long> classificationsValuesIds = new ArrayList<Long>();
			List<Long> processInstancesIds = new ArrayList<Long>();
			
			if(value.getClassificationsValueValue() != null && !value.getClassificationsValueValue().isEmpty()) {
				
				for(ClassificationValue classification : value.getClassificationsValueValue()) {
					if(classification != null && classification.getId() != null) {
						classificationsValuesIds.add(classification.getId());
					}
				}
				
				query = query.replace(":classificationsValueFilter", "and (classificationsValueValue.id in (:classificationsIds))");
				query = query.replace(":falseClassificationsValueFilter", "and (1 = 2)");
				
			} else {
				query = query.replace(":classificationsValueFilter", "");
				query = query.replace(":falseClassificationsValueFilter", "");
			}
			
			if(value.getProcessInstancesValue() != null && !value.getProcessInstancesValue().isEmpty()) {
				
				for(ProcessInstance pi : value.getProcessInstancesValue()) {
					if(pi != null && pi.getId() != null) {
						processInstancesIds.add(pi.getId());
					}
				}
				
				query = query.replace(":processInstancesValueFilter", "and (processInstancesValue.id in (:processInstancesIds))");
				query = query.replace(":falseProcessInstancesValueFilter", "and (1 = 2)");
				
			} else {
				query = query.replace(":processInstancesValueFilter", "");
				query = query.replace(":falseProcessInstancesValueFilter", "");
			}
			
			if(foundInstancesIds.isEmpty()) {
				query = query.replace(":foundFilter", "");
				
			} else {
				query = query.replace(":foundFilter", "and pi.id in (:foundInstancesIds)");
			}
			
			if(lastActivityInstance) {
				query = query.replace(":lastAcitivityFilter", "and activityInstance.id = (select max(ai.id) from ActivityInstance ai where ai.processInstance.id = pi.id)");
			} else {
				query = query.replace(":lastAcitivityFilter", "");
			}
			
			if(pending) {
				query = query.replace(":pendingFilter", "and activityInstance.activity.nextActivities is not empty");
			} else {
				query = query.replace(":pendingFilter", "");
			}
			
			Query q = sessionManager.getSession().createQuery(query);
			q.setParameter("processId", process.getId());
			q.setParameter("propertyId", value.getProperty().getId());
			q.setParameter("stringValue", value.getStringValue() == null || value.getStringValue().isEmpty() ? null : "%" + value.getStringValue() + "%");
			q.setParameter("booleanValue", value.getBooleanValue() == null || ! value.getBooleanValue() ? null : value.getBooleanValue());
			q.setParameter("dateValue", value.getDateValue());
			q.setParameter("longValue", value.getLongValue());
			q.setParameter("number", number == null || number.isEmpty() ? null : number);
			q.setParameter("activityId", activity == null ? null : activity.getId());
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
			q.setParameter("initialUser", initialUser == null || initialUser.isEmpty()? null : "%" + initialUser + "%");
			q.setParameter("endUser", endUser == null || endUser.isEmpty() ? null : "%" + endUser + "%");
			q.setParameter("initialUserGroup", initialUserGroup == null || initialUserGroup.isEmpty() ? null : "%" + initialUserGroup + "%");
			q.setParameter("endUserGroup", endUserGroup == null || endUserGroup.isEmpty() ? null : "%" + endUserGroup + "%");
			q.setParameter("comments", comments == null || comments.isEmpty() ? null : "%" + comments + "%");
			
			if(value.getClassificationsValueValue() != null && !value.getClassificationsValueValue().isEmpty()) {
				q.setParameterList("classificationsIds", classificationsValuesIds);
			}
			
			if(value.getProcessInstancesValue() != null && !value.getProcessInstancesValue().isEmpty()) {
				q.setParameterList("processInstancesIds", processInstancesIds);
			}
			
			if(foundInstancesIds.isEmpty()) {
				foundInstancesIds.addAll(q.list());
				
			} else {
				q.setParameterList("foundInstancesIds", foundInstancesIds);
				List<Long> newOnes = q.list();
				foundInstancesIds.retainAll(newOnes);
			}
			
			if(foundInstancesIds.isEmpty()) {
				break;
			}
		}
		
		return foundInstancesIds;
	}

}
