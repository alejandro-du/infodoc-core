package infodoc.core.container;

import infodoc.core.dto.Validation;

@SuppressWarnings("unchecked")
public class ValidationContainer extends UserGroupFilteredContainer<Validation> {
	
	private static final long serialVersionUID = 1L;

	public ValidationContainer() {
		super(Validation.class, "userGroup.id");
	}

	@Override
	public void beforeSaveOrUpdate(Validation validation) {
		if(validation.getUserGroup() == null) {
			validation.setUserGroup(getUserGroup().getParentUserGroup());
		}
	}
	
}
