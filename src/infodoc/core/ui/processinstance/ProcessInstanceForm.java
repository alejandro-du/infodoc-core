package infodoc.core.ui.processinstance;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Property;
import infodoc.core.dto.ClassificationValue;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.dto.Validation;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;

public class ProcessInstanceForm extends Form {

	private static final long serialVersionUID = 1L;
	
	protected ProcessInstance instance;
	protected Activity activity;
	protected User user;
	protected List<Property> properties;
	protected boolean focus;
	protected boolean showAllProperties;
	protected boolean showShearchProperties;
	protected TextField commentsTextField;
	
	public ProcessInstanceForm(ProcessInstance instance, Activity activity, User user) {
		this(instance, activity, user, false);
	}
	
	public ProcessInstanceForm(ProcessInstance instance, Activity activity, User user, boolean focus) {
		this(instance, activity, user, focus, false, false);
	}
	
	public ProcessInstanceForm(ProcessInstance instance, Activity activity, User user, boolean focus, boolean showAllProperties, boolean showShearchProperties) {
		this.instance = instance;
		this.activity = activity;
		this.user = user;
		this.focus = focus;
		this.showAllProperties = showAllProperties;
		this.showShearchProperties = showShearchProperties;
	}
	
	@Override
	public void attach() {
		createProperties();
	}
	
	@Override
	public void focus() {
		if(properties != null && !properties.isEmpty()) {
			super.focus();
		}
	}
	
	public void createProperties() {
		if(showAllProperties) {
			properties = InfodocContainerFactory.getPropertyContainer().findByProcessId(instance.getProcess().getId());
		} else {
			properties = InfodocContainerFactory.getPropertyContainer().findByUserIdAndProcessIdAndActivityId(user.getId(), instance.getProcess().getId(), activity.getId());
		}
		
		try {
			for(Property property : properties) {
				FieldFactory fieldWrapper = (FieldFactory) Class.forName(property.getJavaClass()).newInstance();
				Field field;
				
				if(showShearchProperties) {
					field = fieldWrapper.getSearchField(property, this, activity, instance.getProcess(), getApplication());
				} else {
					field = fieldWrapper.getField(property, this, activity, instance.getProcess(), getApplication());
				}
				
				if(property.getIcon() != null && !property.getIcon().isEmpty()) {
					field.setIcon(new ThemeResource(property.getIcon()));
				}
				
				if(property.getColor() != null && !property.getColor().isEmpty()) {
					field.addStyleName(property.getColor());
				}
				
				if(property.getBold()) {
					field.addStyleName(InfodocTheme.CLASS_BOLD);
				}
				
				if(property.getItalic()) {
					field.addStyleName(InfodocTheme.CLASS_ITALIC);
				}
				
				field.setWidth("100%");
				field.setCaption(property.getName());
				field.setRequired(property.getRequired() && !showShearchProperties);
				
				Object value = InfodocContainerFactory.getProcessInstanceContainer().getValue(instance, property);
				
				if(value != null) {
					field.setValue(value);
				}
				
				if(property.getValidations() != null && !property.getValidations().isEmpty()) {
					addValidations(property, field);
				}
				
				addField(property.getName(), field);
			}
			
			if(activity != null && activity.getAllowComments()) {
				commentsTextField = new TextField(InfodocConstants.uiObservations);
				commentsTextField.setWidth("100%");
				commentsTextField.setIcon(new ThemeResource(InfodocTheme.iconComments));
				addField("_infodoc.observaciones_", commentsTextField);
			}
			
			fireValueChange(false);
			
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		if(focus) {
			focus();
		}
	}
	
	public void addValidations(Property property, Field field) {
		for(Validation validation : property.getValidations()) {
			try {
				ValidatorFactory validatorWrapper = (ValidatorFactory) Class.forName(validation.getJavaClass()).newInstance();
				field.addValidator(validatorWrapper.getValidator(validation.getErrorMessage(), validation.getParameter(), getApplication()));
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public PropertyValue getPropertyValue(ProcessInstance instance, Property property) {
		if(instance.getPropertyValues() != null) {
			for(PropertyValue value : instance.getPropertyValues()) {
				if(value.getProperty().equals(property)) {
					return InfodocContainerFactory.getPropertyValueContainer().getEntity(value.getId());
				}
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<PropertyValue> getPropertyValues() {
		List<PropertyValue> values = new ArrayList<PropertyValue>();
		
		for(Property property : properties) {
			Field field = getField(property.getName());
			
			if(field != null) {
				field.setRequiredError(InfodocConstants.uiRequiredField + ": " + property.getName() + ".");
				
				PropertyValue value = getPropertyValue(instance, property);
				
				if(value == null) {
					value = new PropertyValue();
					value.setProperty(property);
				}
				
				value.setProcessInstance(instance);
				values.add(value);
				
				if(field.getValue() != null && field.isVisible()) {
					try {
						FieldFactory fieldWrapper = (FieldFactory) Class.forName(property.getJavaClass()).newInstance();
							
						if(fieldWrapper.getType().equals(FieldType.LONG)) {
							value.setLongValue((Long) field.getValue());
							
						} else if(fieldWrapper.getType().equals(FieldType.BYTE_ARRAY)) {
							value.setByteArrayValue((byte[]) field.getValue());
							value.setStringValue(property.getParameter());
							
						} else if(fieldWrapper.getType().equals(FieldType.STRING)) {
							value.setStringValue((String) field.getValue());
							
						} else if(fieldWrapper.getType().equals(FieldType.BOOLEAN)) {
							value.setBooleanValue((Boolean) field.getValue());
							
						} else if(fieldWrapper.getType().equals(FieldType.CLASSIFICATION_VALUES)) {
							Set<ClassificationValue> classificationValues = (Set<ClassificationValue>) field.getValue();
							value.setClassificationsValueValue(classificationValues);
							
						} else if(fieldWrapper.getType().equals(FieldType.PROCESS_INSTANCES)) {
							Set<ProcessInstance> processInstances = (Set<ProcessInstance>) field.getValue();
							value.setProcessInstancesValue(processInstances);
							
						} else if(fieldWrapper.getType().equals(FieldType.DATE)) {
							value.setDateValue((Date) field.getValue());
							
						} else {
							throw new RuntimeException("Unsupported FieldType.");
						}
						
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				
			}
		}
		
		return values;
	}
	
	@Override
    public void validate() throws InvalidValueException {
		List<Property> properties = InfodocContainerFactory.getPropertyContainer().findByUserIdAndProcessIdAndActivityId(user.getId(), instance.getProcess().getId(), activity.getId());
		List<PropertyValue> values = getPropertyValues();
		
		for(PropertyValue value : values) {
			if(!properties.contains(value.getProperty())) {
				throw new InvalidValueException(InfodocConstants.uiErrorPropertiesModified);
			}
		}
		
		super.validate();
	}
	
	public void clear() {
		Process process = instance.getProcess();
		instance = new ProcessInstance();
		instance.setProcess(process);
		removeAllProperties();
		createProperties();
	}

	public ProcessInstance getProcessInstance() {
		return instance;
	}
	
	public String getComments() {
		String comments = null;
		
		if(commentsTextField != null) {
			comments = commentsTextField.getValue().toString();
		}
		
		return comments;
	}

	public boolean isShowAllProperties() {
		return showAllProperties;
	}
	
	public boolean isShowShearchProperties() {
		return showShearchProperties;
	}

}
