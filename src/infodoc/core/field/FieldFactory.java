package infodoc.core.field;

import infodoc.core.dto.Property;
import infodoc.core.dto.Form;
import infodoc.core.dto.Activity;
import infodoc.core.ui.cases.CaseForm;

import com.vaadin.Application;
import com.vaadin.ui.Field;

public interface FieldFactory {
	
	FieldType getType();
	
	Field getField(Property property, CaseForm form, Activity activity, Form formDto, Application application);
	
	Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application);
	
	String getHelp();
	
}
