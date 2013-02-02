package infodoc.core.ui.common;

import infodoc.core.InfodocConstants;
import infodoc.core.dto.User;
import infodoc.core.ui.auth.LoginWindow;
import infodoc.core.ui.auth.OptionsWindow;
import infodoc.core.ui.auth.SessionComponent;

import java.util.List;

import com.vaadin.terminal.ThemeResource;

import enterpriseapp.ui.window.MDIWindow;
import enterpriseapp.ui.window.Module;

public class InfodocMainWindow extends MDIWindow {
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public InfodocMainWindow(User user, List<Module> modules) {
		super(InfodocConstants.uiInfodoc, modules);
		this.user = user;
		
		if(user == null) {
			initPublicContent();
		} else {
			initPrivateContent();
		}
	}
	
	public void initPublicContent() {
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.setClosable(false);
		addWindow(loginWindow);
		loginWindow.focus();
	}
	
	public void initPrivateContent() {
		initWorkbenchContent(user, InfodocConstants.uiHelp);
		layoutMenu.setIcon(new ThemeResource(InfodocTheme.iconLayout));
		tabsMenuItem.setIcon(new ThemeResource(InfodocTheme.iconTabs));
		windowsMenuItem.setIcon(new ThemeResource(InfodocTheme.iconWindows));
		closeAllMenuItem.setIcon(new ThemeResource(InfodocTheme.iconCloseAll));
		
		addSessionComponent();
		checkPasswordExpired();
	}
	
	public void addSessionComponent() {
		SessionComponent sesionComponent = new SessionComponent(user);
		sesionComponent.setSizeUndefined();
		
		menuLayout.addComponent(sesionComponent);
	}

	public void checkPasswordExpired() {
		if(user.isExpirePassword()) {
			OptionsWindow window = new OptionsWindow();
			window.setForceNewPassword(true);
			getWindow().addWindow(window);
		}
	}

}
