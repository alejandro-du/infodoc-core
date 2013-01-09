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
		return query("from Form where disabled = ?", new Object[] {disabled});
	}
	
	public List<Form> findByUserId(Long userId) {
		return query(
			"select distinct p" +
			" from User u" +
			" join u.userGroup g" +
			" join g.activities a" +
			" join a.form p" +
			" where u.id = ? and p.disabled is false",
			new Object[] {userId});
	}
	
}
