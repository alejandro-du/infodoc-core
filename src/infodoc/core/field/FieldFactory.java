package infodoc.core.field;

import infodoc.core.dto.Property;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import com.vaadin.Application;
import com.vaadin.ui.Field;

public interface FieldFactory {
	
	FieldType getType();
	
	Field getField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application);
	
	Field getSearchField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application);
	
	String getHelp();
	
}
