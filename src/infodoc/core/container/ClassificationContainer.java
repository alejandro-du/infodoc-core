package infodoc.core.container;

import infodoc.core.dto.Classification;

@SuppressWarnings("unchecked")
public class ClassificationContainer extends UserGroupFilteredContainer<Classification> {
	
	private static final long serialVersionUID = 1L;

	public ClassificationContainer() {
		super(Classification.class, "userGroup.id");
	}

	@Override
	public void beforeSaveOrUpdate(Classification classification) {
		if(classification.getUserGroup() == null) {
			classification.setGroup(getUserGroup().getParentUserGroup());
		}
	}
	
}
