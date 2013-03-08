package infodoc.core.container;

import infodoc.core.dto.Property;
import infodoc.core.dto.ClassificationValue;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.Case;
import infodoc.core.dto.Form;
import infodoc.core.dto.Activity;
import infodoc.core.dto.ActivityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

@SuppressWarnings("unchecked")
public class CaseContainer extends UserGroupFilteredContainer<Case> {
	
	private static final long serialVersionUID = 1L;
	
	public CaseContainer() {
		super(Case.class, "form.userGroup.id");
	}

	@Override
	public synchronized Serializable saveEntity(Case caseDto) {
		try {
			sessionManager.getSession().beginTransaction();
			
			Long nextValue = InfodocContainerFactory.getNumerationContainer().getNextValue(caseDto.getForm().getId());
			caseDto.setNumber(nextValue);
			
			Serializable serializable = super.saveEntity(caseDto);
			
			return serializable;
			
		} catch (Exception e) {
			sessionManager.getSession().getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}
	
	public Case getCurrentByNumber(Long number) {
		return singleQuery(
			"select c" +
			" from Case c" +
			" where c.number = ?" +
			" order by c.id desc" +
			" limit 1", new Object[] {number});
	}
	
	public List<Case> findMyCases(Long userId, Long formId) {
		return query(
			"select distinct c" +
			" from Case c" +
			" join c.activityInstances ai" +
			" join ai.activity.nextActivities nextAct" +
			" left join ai.assignedUsers assignedU1" +
			" left join ai.assignedUserGroups assignedUG" +
			" left join assignedUG.users assignedU2" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.caseDto.id = c.id)" +
			" and (assignedU1.id = ? or assignedU2.id = ? or (ai.assignedUsers is empty and ai.assignedUserGroups is empty))" +
			" and (? = null or c.form.id = ?)" +
			" order by ai.executionTime desc",
			new Object [] {userId, userId, formId, formId}
		);
	}

	public List<Case> findAvailableByUserIdAndNextActivityId(Long userId, Long activityId) {
		return query(
			"select distinct c" +
			" from Case c" +
			" join c.activityInstances ai" +
			" join ai.activity.nextActivities nextAct" +
			" left join ai.assignedUsers assignedU1" +
			" left join ai.assignedUserGroups assignedUG" +
			" left join assignedUG.users assignedU2" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.caseDto.id = c.id)" +
			" and (assignedU1.id = ? or assignedU2.id = ? or (ai.assignedUsers is empty and ai.assignedUserGroups is empty))" +
			" and nextAct.id = ?" +
			" order by ai.executionTime desc",
			new Object [] {userId, userId, activityId}
		);
	}

	public List<Case> findAssignedToOtherUserByUserIdAndNextActivityId(Long userId, Long activityId) {
		return query(
			"select distinct c" +
			" from Case c" +
			" join c.activityInstances ai" +
			" join ai.activity.nextActivities nextAct" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai.caseDto.id = c.id)" +
			" and ai.user.id = ?" +
			" and nextAct.id = ?" +
			" and ai.user.id not in (select u.id from ActivityInstance ai2 join ai2.assignedUsers u where ai2.id = ai.id)",
			new Object [] {userId, activityId}
		);
	}
	
	public List<Case> findAssignedByUserIdAndCurrentActivityId(Long userId, Long activityId) {
		return query(
			"select distinct c" +
			" from Case c" +
			" join c.activityInstances ai" +
			" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.caseDto.id = c.id)" +
			" and ai.user.id = ?" +
			" and ai.activity.id = ?",
			new Object [] {userId, activityId}
		);
	}
	
	public List<Case> findByActivityId(Long activityId) {
		return query(
				"select distinct c" +
				" from Case c" +
				" join c.activityInstances ai" +
				" where ai.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.caseDto.id = c.id)" +
				" and ai.activity.id = ?" +
				" and c." + getHqlConditionToFilterByUserGroup(),
				new Object [] {activityId}
			);
	}
	
	public List<Case> findPendingByFormId(Long formId, Date from, Date to) {
		return query(
			"select distinct c" +
			" from Case c" +
			" join c.activityInstances lastAi" +
			" join c.activityInstances firstAi" +
			" where firstAi.id = (select min(ai.id) from ActivityInstance ai where ai.caseDto.id = c.id)" +
			" and lastAi.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.caseDto.id = c.id)" +
			" and lastAi.activity.nextActivities is not empty" +
			" and c.form.id = ?" +
			" and date(firstAi.executionTime) >= date(?)" +
			" and date(lastAi.executionTime) <= date(?)",
			new Object [] {formId, from, to}
		);
	}
	
	public List<Case> findFinishedByFormId(Long formId, Date from, Date to) {
		return query(
			"select distinct c" +
			" from Case c" +
			" join c.activityInstances lastAi" +
			" join c.activityInstances firstAi" +
			" left join lastAi.activity.nextActivities nextAct" +
			" where firstAi.id = (select min(ai.id) from ActivityInstance ai where ai.caseDto.id = c.id)" +
			" and lastAi.id = (select max(ai2.id) from ActivityInstance ai2 where ai2.caseDto.id = c.id)" +
			" and c.form.id = ?" +
			" and nextAct is null" +
			" and date(firstAi.executionTime) >= date(?)" +
			" and date(lastAi.executionTime) <= date(?)",
			new Object [] {formId, from, to}
		);
	}

	public List<Case> findByNumberAndFormId(Long number, Long formId) {
		return query(
			"select distinct c" +
			" from Case c" +
			" where c.form.id = ?" +
			" and c.number = ?" +
			" and c." + getHqlConditionToFilterByUserGroup(),
			new Object[] {number, formId});
	}
	
	public List<Case> findReferencingCases(Long referencingPropertyId, Long referencedCaseId, Long formId) {
		return query(
			"select c" +
			" from Case c" +
			" join c.propertyValues pv" +
			" join pv.property p" +
			" join pv.caseDtosValue cv" +
			" where p.id = ?" +
			" and cv.id = ?" +
			" and c.form.id = ?",
			new Object[] {referencingPropertyId, referencedCaseId, formId}
		);
	}
	
	// TODO: validate consistency
	public Case updateInstance(Case instance, List<PropertyValue> propertyValues, ActivityInstance activityInstance) {
		try {
			InfodocContainerFactory.getCaseContainer().saveOrUpdateEntity(instance);
			return saveActivityInstance(instance, propertyValues, activityInstance);
			
		} catch (Exception e) {
			sessionManager.getSession().getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}

	// TODO: validate consistency
	public Case saveInstace(Case instance, List<PropertyValue> propertyValues, ActivityInstance activityInstace) {
		try {
			InfodocContainerFactory.getCaseContainer().saveEntity(instance);
			return saveActivityInstance(instance, propertyValues, activityInstace);
			
		} catch (Exception e) {
			sessionManager.getSession().getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}
	
	private Case saveActivityInstance(Case caseDto, List<PropertyValue> propertyValues, ActivityInstance activityInstance) {
		activityInstance.setCaseDto(caseDto);
		InfodocContainerFactory.getActivityInstanceContainer().saveEntity(activityInstance);
		
		if(propertyValues != null) {
			for(PropertyValue value : propertyValues) {
				InfodocContainerFactory.getPropertyValueContainer().saveOrUpdateEntity(value);
			}
		}
		
		return InfodocContainerFactory.getCaseContainer().getEntity(caseDto.getId());
	}
	
	public Object getValue(Case c, Property property) {
		if(c.getPropertyValues() != null) {
			for(PropertyValue value : c.getPropertyValues()) {
				if(value.getProperty().getId().equals(property.getId())) {
					return InfodocContainerFactory.getPropertyValueContainer().getValue(value);
				}
			}
		}
		
		return null;
	}
	
	public ActivityInstance getFisrtActivityInstance(Case instance) {
		ActivityInstance activityInstance = null;
		
		if(instance.getActivityInstances() != null && !instance.getActivityInstances().isEmpty()) {
			activityInstance = (ActivityInstance) instance.getActivityInstances().toArray()[0];
		}
		
		return activityInstance;
	}

	public ActivityInstance getLastActivityInstance(Case instance) {
		ActivityInstance activityInstance = null;
		
		if(instance.getActivityInstances() != null && !instance.getActivityInstances().isEmpty()) {
			activityInstance = (ActivityInstance) instance.getActivityInstances().toArray()[instance.getActivityInstances().size() - 1];
		}
		
		return activityInstance;
	}

	public Collection<Long> search(
		Form form,
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
			"select distinct c.id" +
			" from Case c" +
			" left join c.propertyValues value" +
			" left join value.classificationsValueValue classificationsValueValue" +
			" left join value.caseDtosValue caseDtosValue" +
			" left join c.activityInstances activityInstance" +
			" left join activityInstance.assignedUsers assignedUser" +
			" left join activityInstance.user.userGroup initialUserGroup" +
			" left join activityInstance.assignedUserGroups assignedUserGroup1" +
			" left join assignedUser.userGroup assignedUserGroup2" +
			" where c.form.id = :formId" +
			" :foundFilter" +
			" :lastAcitivityFilter" +
			" :pendingFilter" +
			" and ('' || :number is null or '' || c.number like :number)" +
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
						" :caseDtosValueFilter" +
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
						" :falseCasesValueFilter" +
					" )" +
				" )" +
			" )";
			
			List<Long> classificationsValuesIds = new ArrayList<Long>();
			List<Long> caseDtosIds = new ArrayList<Long>();
			
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
			
			if(value.getCaseDtosValue() != null && !value.getCaseDtosValue().isEmpty()) {
				
				for(Case c : value.getCaseDtosValue()) {
					if(c != null && c.getId() != null) {
						caseDtosIds.add(c.getId());
					}
				}
				
				query = query.replace(":caseDtosValueFilter", "and (caseDtosValue.id in (:caseDtosIds))");
				query = query.replace(":falseCasesValueFilter", "and (1 = 2)");
				
			} else {
				query = query.replace(":caseDtosValueFilter", "");
				query = query.replace(":falseCasesValueFilter", "");
			}
			
			if(foundInstancesIds.isEmpty()) {
				query = query.replace(":foundFilter", "");
				
			} else {
				query = query.replace(":foundFilter", "and c.id in (:foundInstancesIds)");
			}
			
			if(lastActivityInstance) {
				query = query.replace(":lastAcitivityFilter", "and activityInstance.id = (select max(ai.id) from ActivityInstance ai where ai.caseDto.id = c.id)");
			} else {
				query = query.replace(":lastAcitivityFilter", "");
			}
			
			if(pending) {
				query = query.replace(":pendingFilter", "and activityInstance.activity.nextActivities is not empty");
			} else {
				query = query.replace(":pendingFilter", "");
			}
			
			Query q = sessionManager.getSession().createQuery(query);
			q.setParameter("formId", form.getId());
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
			
			if(value.getCaseDtosValue() != null && !value.getCaseDtosValue().isEmpty()) {
				q.setParameterList("caseDtosIds", caseDtosIds);
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
