package infodoc.core.container.validator;

import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;
import infodoc.core.ui.common.InfodocReport;

import java.lang.reflect.Constructor;

import com.vaadin.data.validator.AbstractValidator;

public class JavaReportJavaClassValidator extends AbstractValidator {

	private static final long serialVersionUID = 1L;
	
	public JavaReportJavaClassValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public boolean isValid(Object value) {
		if(value != null && !value.toString().isEmpty()) {
			try {
				Constructor<?> constructor = Class.forName(value.toString()).getConstructor(Form.class, JavaReport.class);
				Object instance = constructor.newInstance(new Object[] {null, null});
				
				if(!InfodocReport.class.isAssignableFrom(instance.getClass())) {
					return false;
				}
				
			} catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}

}
