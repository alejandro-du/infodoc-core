package infodoc.core.container.validator;

import com.vaadin.data.validator.RegexpValidator;

public class UserPasswordValidator extends RegexpValidator {

	private static final long serialVersionUID = 1L;

	public UserPasswordValidator(String errorMessage) {
		/*
		^                 # string begining
		(?=.*[0-9])       # at least one digit
		(?=.*[a-z])       # at least one lowercase
		(?=.*[A-Z])       # at least one uppercase
		.{8,}             # at least 8 characters
		$                 # end-of-string
		*/
		super("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", true, errorMessage);
	}

}
