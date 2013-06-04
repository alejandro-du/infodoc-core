package infodoc.core.ui.cases;

import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Case;
import infodoc.core.dto.ClassificationValue;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.User;
import infodoc.core.dto.Validation;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.common.InfodocTheme;
import infodoc.core.ui.validator.ValidatorFactory;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class CaseForm extends com.vaadin.ui.Form {

	private static final long serialVersionUID = 1L;
	
	protected Case instance;
	protected Activity activity;
	protected User user;
	protected List<Property> properties;
	protected boolean focus;
	protected boolean showAllProperties;
	protected boolean showShearchProperties;
	protected TextField commentsTextField;
	
	public CaseForm(Case instance, Activity activity, User user) {
		this(instance, activity, user, false);
	}
	
	public CaseForm(Case instance, Activity activity, User user, boolean focus) {
		this(instance, activity, user, focus, false, false);
	}
	
	public CaseForm(Case instance, Activity activity, User user, boolean focus, boolean showAllProperties, boolean showShearchProperties) {
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
			properties = InfodocContainerFactory.getPropertyContainer().findByFormId(instance.getForm().getId());
		} else {
			properties = InfodocContainerFactory.getPropertyContainer().findByUserIdAndFormIdAndActivityId(user.getId(), instance.getForm().getId(), activity.getId());
		}
		
		try {
			for(Property property : properties) {
				FieldFactory fieldWrapper = (FieldFactory) Class.forName(property.getJavaClass()).newInstance();
				Field field;
				
				if(showShearchProperties) {
					field = fieldWrapper.getSearchField(property, this, activity, instance.getForm(), user);
				} else {
					field = fieldWrapper.getField(property, this, activity, instance.getForm(), user);
				}
				
				if(field != null) {
					if(property.getIcon() != null && !property.getIcon().isEmpty()) {
						field.setIcon(new ThemeResource(property.getIcon()));
					}
					
					if(property.getColor() != null && !property.getColor().isEmpty()) {
						field.addStyleName(property.getColor());
					}
					
					if(property.isBold()) {
						field.addStyleName(InfodocTheme.CLASS_BOLD);
					}
					
					if(property.isItalic()) {
						field.addStyleName(InfodocTheme.CLASS_ITALIC);
					}
					
					field.setWidth("100%");
					field.setCaption(property.getName());
					field.setRequired(property.isRequired() && !showShearchProperties);
					
					Object value = InfodocContainerFactory.getCaseContainer().getValue(instance, property);
					
					if(value != null) {
						field.setValue(value);
					}
					
					if(property.getValidations() != null && !property.getValidations().isEmpty()) {
						addValidations(property, field);
					}
					
					addField(property.getName(), field);
				}
				
			}
			
			if(activity != null && activity.isAllowComments()) {
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
	
	public PropertyValue getPropertyValue(Case instance, Property property) {
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
	public Collection<PropertyValue> getPropertyValues() {
		TreeSet<PropertyValue> values = new TreeSet<PropertyValue>();
		
		for(Property property : properties) {
			Field field = getField(property.getName());
			
			if(field != null) {
				field.setRequiredError(InfodocConstants.uiRequiredField + ": " + property.getName() + ".");
				
				PropertyValue value = getPropertyValue(instance, property);
				
				if(value == null) {
					value = new PropertyValue();
					value.setProperty(property);
				}
				
				value.setCaseDto(instance);
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
							
						} else if(fieldWrapper.getType().equals(FieldType.CASES)) {
							Set<Case> caseDtos = (Set<Case>) field.getValue();
							value.setCaseDtosValue(caseDtos);
							
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
		List<Property> properties = InfodocContainerFactory.getPropertyContainer().findByUserIdAndFormIdAndActivityId(user.getId(), instance.getForm().getId(), activity.getId());
		Collection<PropertyValue> values = getPropertyValues();
		
		for(PropertyValue value : values) {
			if(!properties.contains(value.getProperty())) {
				throw new InvalidValueException(InfodocConstants.uiErrorPropertiesModified);
			}
		}
		
		super.validate();
	}
	
	public void clear() {
		Form form = instance.getForm();
		instance = new Case();
		instance.setForm(form);
		removeAllProperties();
		createProperties();
	}

	public Case getCase() {
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

	public Activity getActivity() {
		return activity;
	}

}
