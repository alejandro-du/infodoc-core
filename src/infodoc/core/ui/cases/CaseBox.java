package infodoc.core.ui.cases;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.Case;
import infodoc.core.dto.ActivityInstance;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutorHelper;
import infodoc.core.ui.common.InfodocTheme;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.Utils;
import enterpriseapp.ui.DownloadField;

public class CaseBox extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	private Case caseDto;
	private FormLayout formLayout;
	private Panel mainPanel;
	private Panel activityInstancesPanel;
	private Link printLink;
	
	public CaseBox(Case caseDto) {
		this.caseDto = caseDto;
		initLayout();
	}

	public void initLayout() {
		formLayout = new FormLayout();
		formLayout.setMargin(false);
		
		activityInstancesPanel = new Panel(InfodocConstants.uiActivitHistory);
		activityInstancesPanel.setStyleName(InfodocTheme.PANEL_ACTIVITY_INSTANCES);
		activityInstancesPanel.setWidth("100%");
		
		mainPanel = new Panel(caseDto.toString());
		mainPanel.setStyleName(InfodocTheme.PANEL_BUBBLE);
		mainPanel.setIcon(new ThemeResource(caseDto.getForm().getIcon()));
		mainPanel.setWidth("100%");
		mainPanel.addComponent(formLayout);
		
		setCompositionRoot(mainPanel);
	}
	
	@Override
	public void attach() {
		formLayout.removeAllComponents();
		
		if(caseDto.getPropertyValues() != null) {
			for(PropertyValue value : caseDto.getPropertyValues()) {
				addValue(value, formLayout);
			}
		}
		
		User user = (User) EnterpriseApplication.getInstance().getUser();
		
		formLayout.addComponent(ActivityExecutorHelper.getAvailableActivitiesLayout(caseDto, user));
		
		printLink = new Link(InfodocConstants.uiPrint, new StreamResource(new StreamResource.StreamSource() {
			private static final long serialVersionUID = 1L;
			public InputStream getStream() {
				try {
					return new ByteArrayInputStream(CasePrintService.generateFile(caseDto));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, caseDto.toString().replace(" ", "") + ".pdf", getApplication()));
		
		printLink.setTargetName("_new");
		
		HorizontalLayout printLayout = new HorizontalLayout();
		printLayout.setWidth("100%");
		printLayout.addComponent(printLink);
		printLayout.setComponentAlignment(printLink, Alignment.TOP_RIGHT);
		
		mainPanel.addComponent(printLayout);
		
		if(caseDto.getActivityInstances() != null && !caseDto.getForm().getHideActivityHistory()) {
			for(ActivityInstance activityInstance : caseDto.getActivityInstances()) {
				addActivityInstance(activityInstance);
			}
		}
		
		VerticalLayout activityHistoryLayout = new VerticalLayout();
		activityHistoryLayout.setWidth("100%");
		activityHistoryLayout.setMargin(true, false, false, false);
		
		if(!caseDto.getForm().getHideActivityHistory()) {
			activityHistoryLayout.addComponent(activityInstancesPanel);
		}
		
		formLayout.addComponent(activityHistoryLayout);
	}
	
	public void addValue(final PropertyValue value, Layout layoutToAddTo) {
		Component component = null;
		
		if(value.getByteArrayValue() != null) {
			DownloadField downloadField = new DownloadField(getApplication()) {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getFileName() {
					return value.getStringValue();
				}
			};
			
			downloadField.setValue(value.getByteArrayValue());
			downloadField.setReadOnly(true);
			downloadField.setCaption(value.getProperty().getName());
			downloadField.setStyleName(InfodocTheme.CAPTION_ITALIC);
			
			component = downloadField;
			
		} else if(value.getCaseDtosValue() != null && !value.getCaseDtosValue().isEmpty()) {
			Set<Case> instances = value.getCaseDtosValue();
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setCaption(value.getProperty().getName());
			
			for(final Case caseDto : instances) {
				Button button = new Button(caseDto.toString());
				button.setStyleName(InfodocTheme.BUTTON_LINK);
				setStyle(value, button);
				button.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						showCase(caseDto);
					}
				});
				
				verticalLayout.addComponent(button);
			}
			
			component = verticalLayout;
			
		} else {
			String v = InfodocContainerFactory.getPropertyValueContainer().getStringValue(value);
			
			if(v != null && !v.isEmpty()) {
				Label label = new Label();
				label.setStyleName(InfodocTheme.CAPTION_ITALIC);
				label.setCaption(value.getProperty().getName());
				label.setValue(v);
				
				component = label;
			}
		}
		
		if(component != null) {
			layoutToAddTo.addComponent(component);
			setStyle(value, component);
		}
	}

	private void setStyle(final PropertyValue value, Component component) {
		if(value.getProperty().getIcon() != null && !value.getProperty().getIcon().isEmpty()) {
			component.setIcon(new ThemeResource(value.getProperty().getIcon()));
		}
		
		if(value.getProperty().getColor() != null && !value.getProperty().getColor().isEmpty()) {
			component.addStyleName(value.getProperty().getColor());
		}
		
		if(value.getProperty().getBold()) {
			component.addStyleName(InfodocTheme.CLASS_BOLD);
		}
		
		if(value.getProperty().getItalic()) {
			component.addStyleName(InfodocTheme.CLASS_ITALIC);
		}
	}
	
	private void showCase(Case instance) {
		instance = InfodocContainerFactory.getCaseContainer().getEntity(instance.getId());
		
		Window window = new Window(instance.getForm().getName());
		window.setWidth("680px");
		window.setHeight("480px");
		window.setModal(true);
		window.addComponent(new CaseBox(instance));
		
		getApplication().getMainWindow().addWindow(window);
	}

	private void addActivityInstance(ActivityInstance activityInstance) {
		String description = getDescription(activityInstance);
		Label label = new Label(description, Label.CONTENT_XHTML);
		label.addStyleName(InfodocTheme.LABEL_SMALL);
		activityInstancesPanel.addComponent(label);
	}

	private String getDescription(ActivityInstance activityInstance) {
		String description = Utils.dateTimeToString(activityInstance.getExecutionTime()) + " - <b>" + activityInstance.getActivity().getName() + ": </b>";
		
		String executedBy = activityInstance.getUser().getLogin();
		description = description + "<span class='" + InfodocTheme.CLASS_INITIAL_USERS + "'>" + executedBy + "<span>";
		String assignedTo = "";
		
		if(activityInstance.getAssignedUsers() != null && !activityInstance.getAssignedUsers().isEmpty()) {
			assignedTo = activityInstance.getAssignedUsers().toString().replace("[", "").replace("]", "");
		}
		
		if(activityInstance.getAssignedUserGroups() != null && !activityInstance.getAssignedUserGroups().isEmpty()) {
			assignedTo = activityInstance.getAssignedUserGroups().toString().replace("[", "").replace("]", "");
		}
		
		if(!assignedTo.isEmpty() && !assignedTo.equals(executedBy)) {
			description += " -> " + "<span class='" + InfodocTheme.CLASS_END_USERS + "'>" + assignedTo + "<span>";
		}
		
		if(activityInstance.getComments() != null && !activityInstance.getComments().trim().isEmpty()) {
			description += " <span class='" + InfodocTheme.CLASS_COMMENTS + "'>&quot;" + activityInstance.getComments().trim() + "&quot;</span>";
		}
		
		return description;
	}

}
