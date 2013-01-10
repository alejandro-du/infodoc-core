package infodoc.core.container;

import java.io.Serializable;

import infodoc.core.InfodocConstants;
import infodoc.core.dto.PropertyValue;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import enterpriseapp.Utils;

@SuppressWarnings("unchecked")
public class PropertyValueContainer extends UserGroupFilteredContainer<PropertyValue> {
	
	private static final long serialVersionUID = 1L;

	public PropertyValueContainer() {
		super(PropertyValue.class, "caseDto.form.userGroup.id");
	}
	
	public Serializable saveOrUpdateEntity(PropertyValue propertyValue) {
		beforeSaveOrUpdate(propertyValue);
		
		sessionManager.getSession().saveOrUpdate(propertyValue);
		
		clearInternalCache();
		fireItemSetChange();
		
		afterSaveOrUpdate(propertyValue);
		return (Serializable) getIdForPojo(propertyValue);
	}
	
	public Object getValue(PropertyValue propertyValue) {
		Object value = null;
		
		try {
			FieldFactory fieldWrapper = (FieldFactory) Class.forName(propertyValue.getProperty().getJavaClass()).newInstance();
				
			if(fieldWrapper.getType().equals(FieldType.LONG)) {
				value = propertyValue.getLongValue();
				
			} else if(fieldWrapper.getType().equals(FieldType.BYTE_ARRAY)) {
				value = propertyValue.getByteArrayValue();
				
			} else if(fieldWrapper.getType().equals(FieldType.STRING)) {
				value = propertyValue.getStringValue();
				
			} else if(fieldWrapper.getType().equals(FieldType.BOOLEAN)) {
				value = propertyValue.getBooleanValue();
				
			} else if(fieldWrapper.getType().equals(FieldType.CLASSIFICATION_VALUES)) {
				value = propertyValue.getClassificationsValueValue();
				
			} else if(fieldWrapper.getType().equals(FieldType.PROCESS_INSTANCES)) {
				value = propertyValue.getCaseDtosValue();
				
			} else if(fieldWrapper.getType().equals(FieldType.DATE)) {
				value = propertyValue.getDateValue();
				
			} else {
				throw new RuntimeException("Not supported FieldType.");
			}
			
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		return value;
	}
	
	public String getStringValue(PropertyValue propertyValue) {
		String value = null;
		
		if(propertyValue.getLongValue() != null) {
			value = propertyValue.getLongValue().toString();
			
		} else if(propertyValue.getStringValue() != null) {
			value = propertyValue.getStringValue();
			
		} else if(propertyValue.getBooleanValue() != null) {
			value = propertyValue.getBooleanValue() ? InfodocConstants.uiYes : InfodocConstants.uiNo;
			
		} else if(propertyValue.getClassificationsValueValue() != null && !propertyValue.getClassificationsValueValue().isEmpty()) {
			value = propertyValue.getClassificationsValueValue().toString().replace("[", "").replace("]", "");
			
		} else if(propertyValue.getCaseDtosValue() != null && !propertyValue.getCaseDtosValue().isEmpty()) {
			value = propertyValue.getCaseDtosValue().toString().replace("[", "").replace("]", "");
			
		} else if(propertyValue.getDateValue() != null) {
			value = Utils.dateToString(propertyValue.getDateValue());
		}
		
		return value;
	}
	
}
