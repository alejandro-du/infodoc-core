package infodoc.core.ui.activity;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutor;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.processinstance.ProcessInstanceForm;
import infodoc.core.ui.processinstance.ProcessInstancesList;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import enterpriseapp.Utils;

public abstract class ActivityListExecutorTemplate extends ActivityExecutor implements ClickListener {
	
	private static final long serialVersionUID = 1L;
	
	protected ProcessInstancesList processInstanceList;
	protected Button updateButton = new Button();
	protected Button executeForAllButton = new Button();
	protected List<ProcessInstance> processInstances;
	protected List<ProcessInstanceForm> forms;
	
	public ActivityListExecutorTemplate() {
		super();
	}

	public ActivityListExecutorTemplate(Activity activity, User user) {
		super(activity, user);
	}
	
	public abstract void execute(ProcessInstanceForm form);
	
	@Override
	public void initLayout() {
		
		updateButton.addListener(this);
		updateButton.setIcon(new ThemeResource(InfodocTheme.iconUpdate));
		updateButton.setDescription(InfodocConstants.uiRefresh);
		
		executeForAllButton.addListener(this);
		executeForAllButton.setIcon(new ThemeResource(InfodocTheme.iconExecuteAll));
		executeForAllButton.setDescription(InfodocConstants.uiExecuteAll);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setMargin(true);
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(updateButton);
		buttonsLayout.addComponent(executeForAllButton);
		
		processInstanceList = new ProcessInstancesList();
		addProcessInstances();
		
		Panel processInstancesPanel = new Panel();
		processInstancesPanel.addStyleName(InfodocTheme.PANEL_LIGHT);
		processInstancesPanel.setSizeFull();
		processInstancesPanel.addComponent(processInstanceList);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(buttonsLayout);
		layout.addComponent(processInstancesPanel);
		layout.setExpandRatio(processInstancesPanel, 1);
		
		setCompositionRoot(layout);
	}
	
	public void addProcessInstances() {
		processInstanceList.removeAllComponents();
		processInstances = getProcessInstances();
		
		forms = new ArrayList<ProcessInstanceForm>();
		
		for(ProcessInstance processInstance : processInstances) {
			ProcessInstanceForm form = createForm(processInstance);
			forms.add(form);
			
			Panel rightPanel = new Panel(InfodocConstants.uiForm);
			rightPanel.addComponent(form);
			
			processInstanceList.addAtBeginning(processInstance, rightPanel);
		}
	}

	@Override
	public Long countAvailableProcessInstances() {
		return (long) getProcessInstances().size();
	}
	
	public List<ProcessInstance> getProcessInstances() {
		List<ProcessInstance> availableInstances = InfodocContainerFactory.getProcessInstanceContainer().findAvailableByUserIdAndNextActivityId(getUser().getId(), getActivity().getId());
		
		if(getUniqueProcessInstance() != null) {
			processInstances = new ArrayList<ProcessInstance>();
			
			if(availableInstances.contains(getUniqueProcessInstance())) {
				processInstances.add(getUniqueProcessInstance());
			}
			
		} else {
			processInstances = availableInstances;
		}

		return processInstances;
	}

	public ProcessInstanceForm createForm(ProcessInstance processInstance) {
		Label leftSpacer = new Label();
		leftSpacer.setWidth("100%");
		
		Button executeButton = new Button(getActivity().getName());
		executeButton.addListener(this);
		executeButton.addListener(this);
		
		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.addComponent(leftSpacer);
		footer.setExpandRatio(leftSpacer, 1);
		footer.addComponent(executeButton);
		footer.setComponentAlignment(executeButton, Alignment.BOTTOM_RIGHT);
		
		ProcessInstanceForm form = new ProcessInstanceForm(processInstance, getActivity(), getUser());
		form.setFooter(footer);
		
		executeButton.setData(form);
		
		return form;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if(updateButton.equals(event.getButton())) {
			update();
			
		} else if(executeForAllButton.equals(event.getButton())) {
			confirmExecuteForAll();
			
		} else {
			if(event.getButton().getData() != null) {
				ProcessInstanceForm form = (ProcessInstanceForm) event.getButton().getData();
				
				executeForOneInstance(form);
			}
		}
	}
	
	public void update() {
		addProcessInstances();
		getWindow().showNotification("" + processInstances.size() + " " + InfodocConstants.uiMatchesFound);
	}
	
	public void confirmExecuteForAll() {
		Utils.yesNoDialog(this, InfodocConstants.uiConfirmExecuteForAll, new ConfirmDialog.Listener() {
			
			public void onClose(ConfirmDialog dialog) {
				if(dialog.isConfirmed()) {
					executeForAllInstances();
				}
			}
		});
		
	}
	
	public void executeForAllInstances() {
		for(ProcessInstanceForm form : forms) {
			executeForInstace(form);
		}
	}

	public void executeForOneInstance(ProcessInstanceForm form) {
		executeForInstace(form);
	}

	public void executeForInstace(ProcessInstanceForm form) {
		try {
			validateProcessInstanceIsAvailable(form.getProcessInstance());
			execute(form);
			
		} catch (InvalidValueException e) {
			form.setComponentError(new UserError(e.getMessage()));
		}
	}
	
	public void validateProcessInstanceIsAvailable(ProcessInstance processInstance) {
		List<ProcessInstance> availableInstances = getProcessInstances();
		
		if(!availableInstances.contains(processInstance)) {
			throw new InvalidValueException(InfodocConstants.uiErrorProcessInstanceNotAvailable);
		}
	}

}
