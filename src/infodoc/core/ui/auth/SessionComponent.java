package infodoc.core.ui.auth;

import infodoc.core.InfodocConstants;
import infodoc.core.ui.comun.InfodocTheme;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

import enterpriseapp.hibernate.dto.User;

public class SessionComponent extends CustomComponent implements Button.ClickListener {

	private static final long serialVersionUID = 1L;
	private Button loginButton = new Button(InfodocConstants.uiLogIn);
	private Button optionsButton = new Button(InfodocConstants.uiOptions);
	private Button logoutButton = new Button(InfodocConstants.uiLogout);
	
	public SessionComponent(User user) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setMargin(false, true, false, true);
		
		if(user == null) {
			layout.addComponent(loginButton);
			loginButton.addListener(this);
			loginButton.setStyleName(BaseTheme.BUTTON_LINK);
		} else {
			Label userLabel = new Label("<b>" + user.getLogin() + "</b> - ", Label.CONTENT_XHTML);
			userLabel.setSizeUndefined();
			userLabel.addStyleName(InfodocTheme.COOL_FONT);
			
			layout.addComponent(userLabel);
			layout.addComponent(optionsButton);
			layout.addComponent(logoutButton);
			
			layout.setComponentAlignment(userLabel, Alignment.MIDDLE_LEFT);
			layout.setComponentAlignment(optionsButton, Alignment.TOP_LEFT);
			layout.setComponentAlignment(logoutButton, Alignment.TOP_LEFT);
			
			optionsButton.addListener(this);
			logoutButton.addListener(this);
			optionsButton.setStyleName(BaseTheme.BUTTON_LINK);
			logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
		}
		
		layout.addStyleName("v-menubar");
		layout.setHeight("22px");
		
		setCompositionRoot(layout);
	}
	
	public void buttonClick(ClickEvent event) {
		if(event.getButton().equals(loginButton)) {
			getApplication().getMainWindow().addWindow(new LoginWindow());
			
		} else if(event.getButton().equals(optionsButton)) {
			getApplication().getMainWindow().addWindow(new OptionsWindow());
			
		} else if(event.getButton().equals(logoutButton)) {
			AuthService.logout(getApplication());
		}
	}

}
