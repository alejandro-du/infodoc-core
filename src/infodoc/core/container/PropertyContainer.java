package infodoc.core.container;

import infodoc.core.dto.Property;

import java.util.List;

@SuppressWarnings("unchecked")
public class PropertyContainer extends UserGroupFilteredContainer<Property> {
	
	private static final long serialVersionUID = 1L;

	public PropertyContainer() {
		super(Property.class, "userGroup.id");
	}
	
	@Override
	public void beforeSaveOrUpdate(Property property) {
		if(property.getUserGroup() == null) {
			property.setUserGroup(getUserGroup().getParentUserGroup());
		}
	}
	
	public List<Property> findByUserIdAndFormIdAndActivityId(Long userId, Long formId, Long activityId) {
		return query(
			"select distinct p" +
			" from Property p" +
			" join p.userGroups g" +
			" join g.users u" +
			" join p.forms proc" +
			" join p.activities a where u.id = ?" +
			" and proc.id = ? and a.id = ?" +
			" and p.disabled is false" +
			" order by p.position",
			new Object[] {userId, formId, activityId});
	}

	public List<Property> findByFormId(Long formId) {
		return query(
			"select distinct p" +
			" from Property p" +
			" join p.forms proc" +
			" where proc.id = ?" +
			" and p.disabled is false" +
			" order by p.position",
			new Object[] {formId});
	}

}
