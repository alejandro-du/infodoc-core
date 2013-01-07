package infodoc.core.container.validator;

import infodoc.core.ui.activity.ActivityExecutor;

import com.vaadin.data.validator.AbstractValidator;

public class ActivityJavaClassValidator extends AbstractValidator {

	private static final long serialVersionUID = 1L;
	
	public ActivityJavaClassValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public boolean isValid(Object value) {
		if(value != null && !value.toString().isEmpty()) {
			try {
				Object instance = Class.forName(value.toString()).newInstance();
				
				if(!ActivityExecutor.class.isAssignableFrom(instance.getClass())) {
					return false;
				}
				
			} catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}

}
