package infodoc.core.ui.auth;

import infodoc.core.InfodocConstants;
import enterpriseapp.ui.window.AuthWindow;

public class LoginWindow extends AuthWindow {

	private static final long serialVersionUID = 1L;
	
	public LoginWindow() {
		super(InfodocConstants.uiLogIn, InfodocConstants.uiLogIn, InfodocConstants.uiUserLogin, InfodocConstants.uiUserPassword, InfodocConstants.uiDefaultLogin, InfodocConstants.uiDefaultPassword, InfodocConstants.uiStaySignedIn);
	}
	
	@Override
	public void buttonClicked() {
		if(!AuthService.login(getApplication(), loginTf.getValue().toString(), passwordTf.getValue().toString())) {
			label.setCaption(InfodocConstants.uiWrongCredentials);
			panel.setVisible(true);
			
		} else {
			this.close();
		}
	}

}
