package infodoc.core.ui.activity;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Case;
import infodoc.core.dto.User;
import infodoc.core.ui.cases.CaseForm;
import infodoc.core.ui.cases.CasesList;
import infodoc.core.ui.common.InfodocTheme;

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
	
	protected CasesList caseDtoList;
	protected Button updateButton = new Button();
	protected Button executeForAllButton = new Button();
	protected List<Case> caseDtos;
	protected List<CaseForm> forms;
	
	public ActivityListExecutorTemplate() {
		super();
	}

	public ActivityListExecutorTemplate(Activity activity, User user) {
		super(activity, user);
	}
	
	public abstract void execute(CaseForm form);
	
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
		
		caseDtoList = new CasesList();
		addCases();
		
		Panel caseDtosPanel = new Panel();
		caseDtosPanel.addStyleName(InfodocTheme.PANEL_LIGHT);
		caseDtosPanel.setSizeFull();
		caseDtosPanel.addComponent(caseDtoList);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(buttonsLayout);
		layout.addComponent(caseDtosPanel);
		layout.setExpandRatio(caseDtosPanel, 1);
		
		setCompositionRoot(layout);
	}
	
	public void addCases() {
		caseDtoList.removeAllComponents();
		caseDtos = getCases();
		
		forms = new ArrayList<CaseForm>();
		
		for(Case caseDto : caseDtos) {
			CaseForm form = createForm(caseDto);
			forms.add(form);
			
			Panel rightPanel = new Panel(InfodocConstants.uiForm);
			rightPanel.addComponent(form);
			
			caseDtoList.addAtBeginning(caseDto, rightPanel);
		}
	}

	@Override
	public Long countAvailableCases() {
		return (long) getCases().size();
	}
	
	public List<Case> getCases() {
		List<Case> availableInstances = InfodocContainerFactory.getCaseContainer().findAvailableByUserIdAndNextActivityId(getUser().getId(), getActivity().getId());
		
		if(getUniqueCase() != null) {
			caseDtos = new ArrayList<Case>();
			
			if(availableInstances.contains(getUniqueCase())) {
				caseDtos.add(getUniqueCase());
			}
			
		} else {
			caseDtos = availableInstances;
		}

		return caseDtos;
	}

	public CaseForm createForm(Case caseDto) {
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
		
		CaseForm form = new CaseForm(caseDto, getActivity(), getUser());
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
				CaseForm form = (CaseForm) event.getButton().getData();
				
				executeForOneInstance(form);
			}
		}
	}
	
	public void update() {
		addCases();
		getWindow().showNotification("" + caseDtos.size() + " " + InfodocConstants.uiMatchesFound);
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
		for(CaseForm form : forms) {
			executeForInstace(form);
		}
	}

	public void executeForOneInstance(CaseForm form) {
		executeForInstace(form);
	}

	public void executeForInstace(CaseForm form) {
		try {
			validateCaseIsAvailable(form.getCase());
			execute(form);
			
		} catch (InvalidValueException e) {
			form.setComponentError(new UserError(e.getMessage()));
		}
	}
	
	public void validateCaseIsAvailable(Case caseDto) {
		List<Case> availableInstances = getCases();
		
		if(!availableInstances.contains(caseDto)) {
			throw new InvalidValueException(InfodocConstants.uiErrorCaseNotAvailable);
		}
	}

}
