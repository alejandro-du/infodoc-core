package infodoc.core.container;

import infodoc.core.dto.Process;

import java.util.List;

@SuppressWarnings("unchecked")
public class ProcessContainer extends UserGroupFilteredContainer<Process> {
	
	private static final long serialVersionUID = 1L;

	public ProcessContainer() {
		super(Process.class, "userGroup.id");
	}
	
	@Override
	public void beforeSaveOrUpdate(Process process) {
		if(process.getUserGroup() == null) {
			process.setUserGroup(getUserGroup().getParentUserGroup());
		}
	}
	
	public List<Process> findByDisabled(boolean disabled) {
		return query("from Process where disabled = ?", new Object[] {disabled});
	}
	
	public List<Process> findByUserId(Long userId) {
		return query(
			"select distinct p" +
			" from User u" +
			" join u.userGroup g" +
			" join g.activities a" +
			" join a.process p" +
			" where u.id = ? and p.disabled is false",
			new Object[] {userId});
	}
	
}
