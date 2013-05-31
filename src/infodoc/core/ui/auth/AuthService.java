package infodoc.core.ui.auth;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.UserContainer;
import infodoc.core.dto.User;

import java.util.HashMap;

import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebBrowser;

import enterpriseapp.EnterpriseApplication;

public class AuthService {
	
	private static HashMap<Long, String> loggedUserIds = new HashMap<Long, String>();
	private static HashMap<Long, Application> loggedUserApps = new HashMap<Long, Application>();
	
	private AuthService() {}
	
	public static boolean login(Application application, String login, String password) {
		UserContainer userContainer = InfodocContainerFactory.getUserContainer();
		User user = userContainer.getNoDisabledByLoginAndPassword(login, password);
		
		if(user != null && user.getId() != null) {
			initApp(application, user);
			LoggerFactory.getLogger(AuthService.class).info("User authenticated (id=" + user.getId() + ")");
			return true;
		}
		
		return false;
	}
	
	public static void logout(Application application) {
		User user = (User) application.getUser();
		
		loggedUserIds.remove(user.getId());
		loggedUserApps.remove(user.getId());
		
		EnterpriseApplication.getInstance().getHttpServletRequest().getSession().setMaxInactiveInterval(InfodocConstants.appSessionTimeout);
		
		application.close();
	}
	
	public static void logout(User user) {
		Application application = loggedUserApps.get(user.getId());
		
		if(application != null) {
			logout(application);
		}
	}
	
	private static void initApp(Application application, User user) {
		application.setUser(user);
		WebBrowser b = (WebBrowser) application.getMainWindow().getTerminal();
		
		if(b != null) {
			String ip = b.getAddress();
			updateIp(application, ip);
		}
		
		application.removeWindow(application.getMainWindow());
		application.init();
	}
	
	public static void updateIp(Application application, String ip) {
		User user = (User) application.getUser();
		
		if(user != null && user.getId() != null) {
			
			String storedIp = loggedUserIds.get(user.getId());
			
			if(!ip.equals(storedIp)) {
				loggedUserIds.remove(user.getId());
				loggedUserApps.remove(user.getId());
				
				loggedUserIds.put(user.getId(), ip);
				loggedUserApps.put(user.getId(), application);
			}
			
		}
	}

	public static HashMap<Long, Application> getLoggedUserApps() {
		return loggedUserApps;
	}

}
