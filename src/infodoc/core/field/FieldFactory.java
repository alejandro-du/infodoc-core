package infodoc.core.field;

import infodoc.core.dto.Activity;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.dto.User;
import infodoc.core.ui.cases.CaseForm;

import com.vaadin.ui.Field;

public interface FieldFactory {
	
	FieldType getType();
	
	Field getField(Property property, CaseForm form, Activity activity, Form formDto, User user);
	
	Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, User user);
	
	String getHelp();
	
}
