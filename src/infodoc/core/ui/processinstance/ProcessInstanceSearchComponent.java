package infodoc.core.ui.processinstance;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import enterpriseapp.Utils;

public class ProcessInstanceSearchComponent extends CustomComponent implements ClickListener {

	private static final long serialVersionUID = 1L;
	
	private Process process;
	private final User user;
	
	private ProcessInstancePagedList pagedInstancesComponent;
	private ProcessInstanceForm form;
	private TextField number = new TextField(InfodocConstants.uiProcessNumber);
	private DateField startingDate = new DateField(InfodocConstants.uiStarting);
	private DateField endingDate = new DateField(InfodocConstants.uiEnding);
	private Select activity = new Select(InfodocConstants.uiActivity);
	private TextField initUser = new TextField();
	private TextField endUser = new TextField();
	private TextField initGroup = new TextField();
	private TextField endGroup = new TextField();
	private TextField comments = new TextField(InfodocConstants.uiObservations);
	private CheckBox lastActivityInstance = new CheckBox(InfodocConstants.uiLastActivityInstance);
	private CheckBox pendingInstances = new CheckBox(InfodocConstants.uiPendingProcessInstances);
	private Button searchButton = new Button(InfodocConstants.uiSearch);
	private Button clearButton = new Button(InfodocConstants.uiClearForm);
	private Panel formPanel = new Panel(InfodocConstants.uiForm);
	
	public ProcessInstanceSearchComponent(Process process, User user) {
		this.process = process;
		this.user = user;
		
		searchButton.addListener(this);
		clearButton.addListener(this);
		
		number.setWidth("100%");
		activity.setWidth("100%");
		startingDate.setWidth("100%");
		endingDate.setWidth("100%");
		initUser.setWidth("100%");
		endUser.setWidth("100%");
		initGroup.setWidth("100%");
		endGroup.setWidth("100%");
		
		formPanel.setWidth("100%");
		createForm();
		
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.setMargin(true);
		leftLayout.addComponent(formPanel);
		
		pagedInstancesComponent = new ProcessInstancePagedList();
		
		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setMargin(true);
		rightLayout.addComponent(pagedInstancesComponent);
		
		HorizontalSplitPanel layout = new HorizontalSplitPanel();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setFirstComponent(leftLayout);
		layout.setSecondComponent(rightLayout);
		
		setCompositionRoot(layout);
	}

	private void createForm() {
		ProcessInstance newInstance = new ProcessInstance();
		newInstance.setProcess(process);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		startingDate.setResolution(DateField.RESOLUTION_MIN);
		startingDate.setDateFormat(Utils.getDateTimeFormatPattern());
		startingDate.setValue(calendar.getTime());
		
		endingDate.setResolution(DateField.RESOLUTION_MIN);
		endingDate.setDateFormat(Utils.getDateTimeFormatPattern());
		
		List<Activity> activities = InfodocContainerFactory.getActivityContainer().findByProcessId(process.getId());
		
		for(Activity a : activities) {
			activity.addItem(a);
		}
		
		HorizontalLayout dateLayout = new HorizontalLayout();
		dateLayout.setSpacing(true);
		dateLayout.setWidth("100%");
		dateLayout.setCaption(InfodocConstants.uiDate);
		dateLayout.addComponent(startingDate);
		dateLayout.addComponent(endingDate);
		
		HorizontalLayout usersLayout = new HorizontalLayout();
		usersLayout.setSpacing(true);
		usersLayout.setWidth("100%");
		usersLayout.setCaption(InfodocConstants.uiUser);
		usersLayout.addComponent(initUser);
		usersLayout.addComponent(endUser);
		
		HorizontalLayout userGroupsLayout = new HorizontalLayout();
		userGroupsLayout.setSpacing(true);
		userGroupsLayout.setWidth("100%");
		userGroupsLayout.setCaption(InfodocConstants.uiUserGroup);
		userGroupsLayout.addComponent(initGroup);
		userGroupsLayout.addComponent(endGroup);
		
		comments.setWidth("100%");
		
		pendingInstances.setValue(true);
		lastActivityInstance.setValue(true);
		
		FormLayout activityLayout = new FormLayout();
		activityLayout.addComponent(activity);
		activityLayout.addComponent(dateLayout);
		activityLayout.addComponent(usersLayout);
		activityLayout.addComponent(userGroupsLayout);
		activityLayout.addComponent(comments);
		activityLayout.addComponent(lastActivityInstance);
		activityLayout.addComponent(pendingInstances);
		
		Panel activityPanel = new Panel();
		activityPanel.setStyleName(InfodocTheme.PANEL_ACTIVITY_INSTANCE_SEARCH);
		activityPanel.addComponent(activityLayout);
		
		Label leftSpacer = new Label();
		leftSpacer.setWidth("100%");
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setMargin(true, false, false, false);
		buttonsLayout.setWidth("100%");
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(leftSpacer);
		buttonsLayout.setExpandRatio(leftSpacer, 1);
		buttonsLayout.addComponent(clearButton);
		buttonsLayout.addComponent(searchButton);
		buttonsLayout.setComponentAlignment(clearButton, Alignment.BOTTOM_RIGHT);
		buttonsLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		
		// TODO: Add: "searchButton.setClickShortcut(KeyCode.ENTER);". Currently not working.
		
		number.setIcon(new ThemeResource(InfodocTheme.iconNumber));
		activity.setIcon(new ThemeResource(InfodocTheme.iconActivity));
		dateLayout.setIcon(new ThemeResource(InfodocTheme.iconDate));
		usersLayout.setIcon(new ThemeResource(InfodocTheme.iconUser));
		userGroupsLayout.setIcon(new ThemeResource(InfodocTheme.iconUserGroup));
		comments.setIcon(new ThemeResource(InfodocTheme.iconComments));
		
		VerticalLayout footer = new VerticalLayout();
		footer.addComponent(activityPanel);
		footer.addComponent(buttonsLayout);
		
		form = new ProcessInstanceForm(newInstance, null, user, true, true, true);
		form.setImmediate(false);
		form.setValidationVisible(false);
		form.setWidth("100%");
		form.setFooter(footer);
		form.addField("numero", number);
		
		formPanel.removeAllComponents();
		formPanel.addComponent(form);
	}
	
	public void buttonClick(ClickEvent event) {
		if(searchButton.equals(event.getButton())) {
			pagedInstancesComponent.removeAllComponents();
			
			Collection<Long> instancesIds = InfodocContainerFactory.getProcessInstanceContainer().search(
				form.getProcessInstance().getProcess(),
				form.getPropertyValues(),
				(String) number.getValue(),
				(Activity) activity.getValue(),
				(Date) startingDate.getValue(),
				(Date) endingDate.getValue(),
				initUser.getValue().toString(),
				endUser.getValue().toString(),
				initGroup.getValue().toString(),
				endGroup.getValue().toString(),
				comments.getValue().toString(),
				lastActivityInstance.booleanValue(),
				pendingInstances.booleanValue()
			);
			
			pagedInstancesComponent.addProcesInstances(instancesIds);
			getWindow().showNotification(instancesIds.size() + " " + InfodocConstants.uiMatchesFound);
			
		} else if(clearButton.equals(event.getButton())) {
			number.setValue("");
			number.setCaption(InfodocConstants.uiProcessNumber); // we need this to show caption after clearing
			activity.setValue(null);
			startingDate.setValue(null);
			endingDate.setValue(null);
			initUser.setValue("");
			endUser.setValue("");
			initGroup.setValue("");
			endGroup.setValue("");
			comments.setValue("");
			
			createForm();
		}
	}
}

