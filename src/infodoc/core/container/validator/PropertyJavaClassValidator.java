package infodoc.core.container.validator;

import infodoc.core.field.FieldFactory;

import com.vaadin.data.validator.AbstractValidator;

public class PropertyJavaClassValidator extends AbstractValidator {

	private static final long serialVersionUID = 1L;
	
	public PropertyJavaClassValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public boolean isValid(Object value) {
		
		if(value != null && !value.toString().isEmpty()) {
			try {
				Object instance = Class.forName(value.toString()).newInstance();
				
				if(!FieldFactory.class.isAssignableFrom(instance.getClass())) {
					return false;
				}
				
			} catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}

}
