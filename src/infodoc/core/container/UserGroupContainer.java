package infodoc.core.container;

import java.util.Set;

import infodoc.core.dto.UserGroup;
import infodoc.core.dto.User;
import infodoc.core.ui.auth.AuthService;

@SuppressWarnings("unchecked")
public class UserGroupContainer extends UserGroupFilteredContainer<UserGroup> {
	
	private static final long serialVersionUID = 1L;

	public UserGroupContainer() {
		super(UserGroup.class, "parentUserGroup.id");
	}

	@Override
	public void beforeSaveOrUpdate(UserGroup userGroup) {
		if(getUserGroup() != null && userGroup.getParentUserGroup() == null) {
			userGroup.setParentUserGroup(getUserGroup().getParentUserGroup());
		}
		
		Set<User> users = null;
		
		if(userGroup.getId() != null) {
			users = getEntity(userGroup.getId()).getUsers();
		}
		
		if(userGroup.getUsers() != null) {
			for(User user : users) {
				AuthService.logout(user);
			}
		}
	}
	
}
