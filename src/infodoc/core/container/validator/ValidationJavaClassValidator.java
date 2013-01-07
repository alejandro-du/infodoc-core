package infodoc.core.container.validator;

import infodoc.core.ui.validator.ValidatorFactory;

import com.vaadin.data.validator.AbstractValidator;

public class ValidationJavaClassValidator extends AbstractValidator {

	private static final long serialVersionUID = 1L;
	
	public ValidationJavaClassValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public boolean isValid(Object value) {
		if(value != null && !value.toString().isEmpty()) {
			try {
				Class<?> clazz = Class.forName(value.toString());
				
				if(!ValidatorFactory.class.isAssignableFrom(clazz)) {
					return false;
				}
				
			} catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}

}
