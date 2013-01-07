package infodoc.core.container;

import infodoc.core.InfodocConstants;
import infodoc.core.container.validator.UserPasswordValidator;
import infodoc.core.dto.User;
import infodoc.core.ui.auth.AuthService;

import java.io.Serializable;
import java.util.List;

import org.slf4j.LoggerFactory;

import enterpriseapp.Utils;

@SuppressWarnings("unchecked")
public class UserContainer extends UserGroupFilteredContainer<User> {

	private static final long serialVersionUID = 1L;

	public UserContainer() {
		super(User.class, "userGroup.parentUserGroup.id");
	}
	
	@Override
	public void beforeSaveOrUpdate(User user) {
		AuthService.logout(user);
	}
	
	@Override
	public Serializable saveOrUpdateEntity(User user) {
		
		if(user.getId() == null) {
			user.setPassword(Utils.getMD5Hash(user.getPassword()));
			
		} else {
			String previousPassword = getPasswordByUserId(user.getId());
			
			if(!previousPassword.equals(user.getPassword())) {
				user.setPassword(Utils.getMD5Hash(user.getPassword()));
			}
		}
		
		return super.saveOrUpdateEntity(user);
	}
	
	public long countNoDisabled() {
		return (Long) singleSpecialQuery("select count(id) from User where disabled is false");
	}
	
	@Override
	public Serializable saveEntity(User user) {
		return saveOrUpdateEntity(user);
	}
	
	public String getPasswordByUserId(long userId) {
		return (String) singleSpecialQuery("select password from User where id = ?", new Object[] {userId});
	}
	
	public User getNoDisabledByLoginAndPassword(String login, String password) {
		password = Utils.getMD5Hash(password);
		return singleQuery("from User where login = ? and password = ? and disabled is false", new Object[] {login, password});
	}
	
	public void updatePassword(User user, String newPassword) {
		UserPasswordValidator passwordValidator = new UserPasswordValidator(InfodocConstants.uiUserPasswordError);
		passwordValidator.validate(newPassword);
		String encrypted = Utils.getMD5Hash(newPassword);
		update("update User set password = ?, expirePassword = false where id = ?", new Object[] {encrypted, user.getId()});
		user.setPassword(encrypted);
		user.setExpirePassword(false);
		LoggerFactory.getLogger(UserContainer.class).info("User password changed (id=" + user.getId() + ")");
	}

	public List<User> findByUserGroupId(Long userGroupId) {
		return query("select u from User u where u.userGroup.id = ?", new Object[] {userGroupId});
	}

}
