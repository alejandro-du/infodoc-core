package infodoc.core.ui.auth;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.UserContainer;
import infodoc.core.dto.User;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebBrowser;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.Utils;

public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	public static final String REMEMBER_ME_COOKIE = "suid";
	
	private static HashMap<Long, String> loggedUserIPs = new HashMap<Long, String>();
	private static HashMap<Long, Application> loggedUserApps = new HashMap<Long, Application>();
	private static HashMap<Long, String> rememberedUserTokens = new HashMap<Long, String>();
	
	private AuthService() {}
	
	public static boolean login(Application application, String login, String password, boolean rememberMe) {
		UserContainer userContainer = InfodocContainerFactory.getUserContainer();
		User user = userContainer.getNoDisabledByLoginAndPassword(login, password);
		
		if(user != null && user.getId() != null) {
			initApp(application, user);
			LoggerFactory.getLogger(AuthService.class).info("User authenticated (id=" + user.getId() + ")");
			
			if(rememberMe) {
				rememberMe(user.getId());
			} else {
				deleteRememberMeCookie();
				rememberedUserTokens.remove(user.getId());
			}
			
			return true;
		}
		
		return false;
	}
	
	public static void loginRememberedUser(Application application) {
		String tokenInCookie = null;
		Cookie[] cookies = EnterpriseApplication.getInstance().getHttpServletRequest().getCookies();
		
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals(REMEMBER_ME_COOKIE)) {
					tokenInCookie = cookies[i].getValue();
				}
			}
		}
		
		if(tokenInCookie != null) {
			for(Entry<Long, String> entry : rememberedUserTokens.entrySet()) {
				if(entry.getValue().equals(tokenInCookie)) {
					saveNewRememberMeCookie(entry.getKey());
					application.setUser(InfodocContainerFactory.getUserContainer().getEntity(entry.getKey()));
				}
			}
		}
	}
	
	private static void rememberMe(Long userId) {
		if(rememberedUserTokens.containsKey(userId)) {
			logout(loggedUserApps.get(userId));
			logger.warn("Remembered user's application deleted!!!");
		}
		
		saveNewRememberMeCookie(userId);
	}

	private static void saveNewRememberMeCookie(Long userId) {
		String token = Utils.generateRandomString(64);
		
		rememberedUserTokens.put(userId, token);
		
		Cookie cookie = new Cookie(REMEMBER_ME_COOKIE, token);
		cookie.setMaxAge(365 * 24 * 60 * 60);
		EnterpriseApplication.getInstance().getHttpServletResponse().addCookie(cookie);
	}

	public static void logout(Application application) {
		User user = (User) application.getUser();
		
		deleteRememberMeCookie();
		rememberedUserTokens.remove(user.getId());
		loggedUserIPs.remove(user.getId());
		loggedUserApps.remove(user.getId());
		
		application.close();
	}

	private static void deleteRememberMeCookie() {
		Cookie[] cookies = EnterpriseApplication.getInstance().getHttpServletRequest().getCookies();
		
		for(int i = 0; i < cookies.length; i++) {
			if(cookies[i].getName().equals(REMEMBER_ME_COOKIE)) {
				cookies[i].setMaxAge(0);
				cookies[i].setValue(null);
				EnterpriseApplication.getInstance().getHttpServletResponse().addCookie(cookies[i]);
			}
		}
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
			
			String storedIp = loggedUserIPs.get(user.getId());
			
			if(!ip.equals(storedIp)) {
				loggedUserIPs.remove(user.getId());
				loggedUserApps.remove(user.getId());
				
				loggedUserIPs.put(user.getId(), ip);
				loggedUserApps.put(user.getId(), application);
			}
			
		}
	}

	public static HashMap<Long, Application> getLoggedUserApps() {
		return loggedUserApps;
	}

}
