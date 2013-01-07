package infodoc.core.container;

import infodoc.core.dto.Activity;

import java.util.List;

@SuppressWarnings("unchecked")
public class ActivityContainer extends UserGroupFilteredContainer<Activity> {
	
	private static final long serialVersionUID = 1L;

	public ActivityContainer() {
		super(Activity.class, "process.userGroup.id");
	}
	
	public List<Activity> findByUserIdAndProcessId(Long userId, Long processId) {
		return query(
			"select distinct a" +
			" from User u" +
			" join u.userGroup g" +
			" join g.activities a" +
			" where u.id = ?" +
			" and a.process.id = ?" +
			" and a.disabled is false",
			new Object[] {userId, processId});
	}

	public List<Activity> findByProcessId(Long processId) {
		return query(
			"select distinct a" +
			" from Activity a" +
			" where a.process.id = ?" +
			" and a.disabled is false",
			new Object[] {processId});
	}
	
	public List<Activity> findByProcessInstanceIdAndUserId(Long processInstanceId, Long userId) {
		return query(
			"select distinct nextAct" +
			" from ActivityInstance lastAi" +
			" join lastAi.activity.nextActivities nextAct" +
			" join lastAi.processInstance pi" +
			" left join lastAi.assignedUsers assignedU1" +
			" left join lastAi.assignedUserGroups assignedUG" +
			" left join assignedUG.users assignedU2" +
			" where pi.id = ?" +
			" and lastAi.id = (select max(ai.id) from ActivityInstance ai where ai.processInstance.id = pi.id)" +
			" and (assignedU1.id = ? or assignedU2.id = ?)" +
			" and nextAct.id in (select a.id from User u join u.userGroup ug join ug.activities a where u.id = ?)",
			new Object [] {processInstanceId, userId, userId, userId}
		);
	}
	
}
