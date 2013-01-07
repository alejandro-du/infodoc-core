package infodoc.core.container.validator;

import java.lang.reflect.Constructor;

import com.vaadin.data.validator.AbstractValidator;

public class HqlReportParameterJavaClassValidator extends AbstractValidator {

	private static final long serialVersionUID = 1L;
	
	public HqlReportParameterJavaClassValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public boolean isValid(Object value) {
		if(value != null && !value.toString().isEmpty()) {
			try {
				Constructor<?> constructor = Class.forName(value.toString()).getConstructor(String.class);
				constructor.newInstance(new Object[] {"1"});
				
			} catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}

}
