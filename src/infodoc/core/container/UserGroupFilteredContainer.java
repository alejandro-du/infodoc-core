package infodoc.core.container;

import infodoc.core.dto.UserGroup;
import infodoc.core.dto.User;
import enterpriseapp.EnterpriseApplication;
import enterpriseapp.hibernate.DefaultHbnContainer;

@SuppressWarnings("unchecked")
public class UserGroupFilteredContainer<T> extends DefaultHbnContainer<T>{
	
	private static final long serialVersionUID = 1L;
	
	protected String userGroupPropertyId;

	public UserGroupFilteredContainer(Class<T> clazz, String userGroupPropertyId) {
		super(clazz);
		this.userGroupPropertyId = userGroupPropertyId;
		EnterpriseApplication app = EnterpriseApplication.getInstance();
		
		if(app != null) {
			User user = (User) app.getUser();
			
			if(user != null && user.getUserGroup().getParentUserGroup() != null) {
				addContainerFilter(userGroupPropertyId, "" + user.getUserGroup().getParentUserGroup().getId(), "", false, true);
			}
		}
	}
	
	public String getHqlConditionToFilterByUserGroup() {
		EnterpriseApplication app = EnterpriseApplication.getInstance();
		
		if(app != null) {
			User user = (User) app.getUser();
			
			if(user != null && user.getUserGroup().getParentUserGroup() != null) {
				return userGroupPropertyId + " = " + user.getUserGroup().getParentUserGroup().getId();
			}
		}
		
		return "";
	}
	
	public UserGroup getUserGroup() {
		User user = (User) EnterpriseApplication.getInstance().getUser();
		
		if(user != null) {
			return user.getUserGroup();
		} else {
			return null;
		}
	}
	
}
