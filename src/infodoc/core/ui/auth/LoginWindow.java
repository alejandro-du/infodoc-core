package infodoc.core.ui.auth;

import com.vaadin.ui.CheckBox;

import infodoc.core.InfodocConstants;
import enterpriseapp.ui.window.AuthWindow;

public class LoginWindow extends AuthWindow {

	private static final long serialVersionUID = 1L;
	
	private CheckBox rememberMe;
	
	public LoginWindow() {
		super(InfodocConstants.uiLogIn, InfodocConstants.uiLogIn, InfodocConstants.uiUserLogin, InfodocConstants.uiUserPassword, InfodocConstants.uiDefaultLogin, InfodocConstants.uiDefaultPassword);
	}
	
	@Override
	protected void initLayout() {
		super.initLayout();
		
		rememberMe = new CheckBox(InfodocConstants.uiRememberMe);
		formLayout.addComponent(rememberMe);
	}
	
	@Override
	public void buttonClicked() {
		if(!AuthService.login(getApplication(), loginTf.getValue().toString(), passwordTf.getValue().toString(), rememberMe.booleanValue())) {
			label.setCaption(InfodocConstants.uiWrongCredentials);
			panel.setVisible(true);
			
		} else {
			this.close();
		}
	}

}
