package infodoc.core.ui.auth;


import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.UserContainer;
import infodoc.core.dto.User;

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class OptionsWindow extends Window implements Button.ClickListener  {
	
	private static final long serialVersionUID = 1L;
	
	public interface Listener {
		public void addComponent(FormLayout formLayout);

		public void attach(User user);

		public void updateButtonClick(User user);
	}
	
	private Panel panel = new Panel();
	private Label label = new Label();
	private PasswordField currentPasswordTf = new PasswordField(InfodocConstants.uiCurrentPassword);
	private PasswordField newPasswordTf = new PasswordField(InfodocConstants.uiNewPassword);
	private PasswordField newPasswordConfirmationTf = new PasswordField(InfodocConstants.uiConfirmNewPassword);
	private Select timeZone = new Select(InfodocConstants.uiUserTimeZone);
	private boolean forceNewPassword = false;
	private User user;
	private static Set<Listener> listeners = new HashSet<Listener>();
	
	public OptionsWindow() {
		super(InfodocConstants.uiOptions);
		setModal(true);
		setSizeUndefined();
		setResizable(false);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeUndefined();
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		
		Button updateButton = new Button(InfodocConstants.uiModify);
		updateButton.addListener(this);
		
		panel.setVisible(false);
		panel.addComponent(label);
		
		currentPasswordTf.setRequired(true);
		
		String[] availableTimeZones = TimeZone.getAvailableIDs();
		
		for(String zone : availableTimeZones) {
			timeZone.addItem(zone);
		}
		
		formLayout.addComponent(currentPasswordTf);
		formLayout.addComponent(newPasswordTf);
		formLayout.addComponent(newPasswordConfirmationTf);
		formLayout.addComponent(timeZone);
		
		for(Listener listener : listeners) {
			listener.addComponent(formLayout);
		}
		
		formLayout.addComponent(updateButton);
		
		verticalLayout.addComponent(panel);
		verticalLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		verticalLayout.addComponent(formLayout);
		verticalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
		
		setContent(verticalLayout);
		updateButton.setClickShortcut(KeyCode.ENTER);
	}
	
	@Override
	public void attach() {
		user = (User) getApplication().getUser();
		user = InfodocContainerFactory.getUserContainer().getEntity(user.getId());
		
		if(user.getTimeZone() != null) {
			timeZone.setValue(user.getTimeZone());
		} else {
			timeZone.setValue(TimeZone.getDefault().getID());
		}
		
		for(Listener listener : listeners) {
			listener.attach(user);
		}
		
		currentPasswordTf.focus();
	}

	public void buttonClick(ClickEvent event) {
		panel.setVisible(false);
		currentPasswordTf.setComponentError(null);
		newPasswordTf.setComponentError(null);
		newPasswordConfirmationTf.setComponentError(null);
		
		if(currentPasswordTf.getValue() == null || currentPasswordTf.getValue().toString().isEmpty()) {
			currentPasswordTf.setComponentError(new UserError(InfodocConstants.uiRequiredField));
			return;
			
		}
		
		UserContainer userContainer = InfodocContainerFactory.getUserContainer();
		User user = (User) getApplication().getUser();
		user = userContainer.getNoDisabledByLoginAndPassword(user.getLogin(), currentPasswordTf.getValue().toString());
		
		if(user == null || user.getId() == null) {
			currentPasswordTf.setComponentError(new UserError(InfodocConstants.uiWrongCredentials));
			return;
		}
		
		if(forceNewPassword && (newPasswordTf.getValue() == null || newPasswordTf.getValue().toString().isEmpty())) {
			newPasswordTf.setComponentError(new UserError(InfodocConstants.uiRequiredField));
			return;
		}
		
		if(newPasswordTf.getValue() != null && !newPasswordTf.getValue().toString().isEmpty()) {
			if(!newPasswordTf.getValue().equals(newPasswordConfirmationTf.getValue())) {
				newPasswordConfirmationTf.setComponentError(new UserError(InfodocConstants.uiPasswordsDoesntMatch));
				return;
			}
			
			try {
				userContainer.updatePassword(user, newPasswordTf.getValue().toString());
				
			} catch(InvalidValueException e) {
				newPasswordTf.setComponentError(new UserError(e.getMessage()));
				return;
			}
		}
		
		user.setTimeZone(timeZone.getValue().toString());
		
		for(Listener listener : listeners) {
			listener.updateButtonClick(user);
		}
		
		user = userContainer.getEntity(user.getId());
		userContainer.saveOrUpdateEntity(user);
		
		label.setCaption(InfodocConstants.uiSaved);
		panel.setVisible(true);
		setClosable(true);
	}

	public void setForceNewPassword(boolean forceNewPassword) {
		this.forceNewPassword = forceNewPassword;
		setClosable(!forceNewPassword);
		newPasswordTf.setRequired(forceNewPassword);
		newPasswordConfirmationTf.setRequired(forceNewPassword);
	}

	public static Set<Listener> getListeners() {
		return listeners;
	}

}
