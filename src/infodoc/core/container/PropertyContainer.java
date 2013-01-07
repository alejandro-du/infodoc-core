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
	
	public List<Property> findByUserIdAndProcessIdAndActivityId(Long userId, Long processId, Long activityId) {
		return query(
			"select distinct p" +
			" from Property p" +
			" join p.userGroups g" +
			" join g.users u" +
			" join p.processes proc" +
			" join p.activities a where u.id = ?" +
			" and proc.id = ? and a.id = ?" +
			" and p.disabled is false" +
			" order by p.position",
			new Object[] {userId, processId, activityId});
	}

	public List<Property> findByProcessId(Long processId) {
		return query(
			"select distinct p" +
			" from Property p" +
			" join p.processes proc" +
			" where proc.id = ?" +
			" and p.disabled is false" +
			" order by p.position",
			new Object[] {processId});
	}

}
