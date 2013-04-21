package infodoc.core.container;

import infodoc.core.dto.Form;

import java.util.List;

@SuppressWarnings("unchecked")
public class FormContainer extends UserGroupFilteredContainer<Form> {
	
	private static final long serialVersionUID = 1L;

	public FormContainer() {
		super(Form.class, "userGroup.id");
	}
	
	@Override
	public void beforeSaveOrUpdate(Form form) {
		if(form.getUserGroup() == null) {
			form.setUserGroup(getUserGroup().getParentUserGroup());
		}
	}
	
	public List<Form> findByDisabled(boolean disabled) {
		return query("from Form where disabled = ? order by position", new Object[] {disabled});
	}
	
	public List<Form> findByUserId(Long userId) {
		return query(
			"select distinct f" +
			" from User u" +
			" join u.userGroup g" +
			" join g.activities a" +
			" join a.form f" +
			" where u.id = ? and f.disabled is false" +
			" order by f.position",
			new Object[] {userId});
	}
	
}
