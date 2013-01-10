package infodoc.core.container;

import infodoc.core.dto.Activity;

import java.util.List;

@SuppressWarnings("unchecked")
public class ActivityContainer extends UserGroupFilteredContainer<Activity> {
	
	private static final long serialVersionUID = 1L;

	public ActivityContainer() {
		super(Activity.class, "form.userGroup.id");
	}
	
	public List<Activity> findByUserIdAndFormId(Long userId, Long formId) {
		return query(
			"select distinct a" +
			" from User u" +
			" join u.userGroup g" +
			" join g.activities a" +
			" where u.id = ?" +
			" and a.form.id = ?" +
			" and a.disabled is false",
			new Object[] {userId, formId});
	}

	public List<Activity> findByFormId(Long formId) {
		return query(
			"select distinct a" +
			" from Activity a" +
			" where a.form.id = ?" +
			" and a.disabled is false",
			new Object[] {formId});
	}
	
	public List<Activity> findByCaseIdAndUserId(Long caseDtoId, Long userId) {
		return query(
			"select distinct nextAct" +
			" from ActivityInstance lastAi" +
			" join lastAi.activity.nextActivities nextAct" +
			" join lastAi.caseDto pi" +
			" left join lastAi.assignedUsers assignedU1" +
			" left join lastAi.assignedUserGroups assignedUG" +
			" left join assignedUG.users assignedU2" +
			" where pi.id = ?" +
			" and lastAi.id = (select max(ai.id) from ActivityInstance ai where ai.caseDto.id = pi.id)" +
			" and (assignedU1.id = ? or assignedU2.id = ? or (lastAi.assignedUsers is empty and lastAi.assignedUserGroups is empty))" +
			" and nextAct.id in (select a.id from User u join u.userGroup ug join ug.activities a where u.id = ?)",
			new Object [] {caseDtoId, userId, userId, userId}
		);
	}
	
}
