package infodoc.core.ui.fieldfactory;

import infodoc.core.InfodocConstants;
import infodoc.core.container.validator.JavaReportJavaClassValidator;
import infodoc.core.dto.JavaReport;
import infodoc.core.ui.common.InfodocTheme;

import java.util.LinkedHashSet;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class JavaReportFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;
	
	private static LinkedHashSet<String> javaClasses = new LinkedHashSet<String>();
	
	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("javaClass".equals(pid)) {
			final Select select = new Select();
			select.setNewItemsAllowed(true);
			
			for(String javaClass : javaClasses) {
				select.addItem(javaClass);
			}
			
			JavaReport report = (JavaReport) bean;
			
			if(report.getJavaClass() != null && !report.getJavaClass().isEmpty()) {
				select.addItem(report.getJavaClass());
			}
			
			field = select;
			
		} else if("icon".equals(pid)) {
			field = InfodocTheme.getIconSelect(uiContext.getApplication());
		}
		
		return field;
	}
	
	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		if("javaClass".equals(pid)) {
			field.addValidator(new JavaReportJavaClassValidator(InfodocConstants.uiErrorJavaClassInvalidOrNotFound));
		}
	}

	public static LinkedHashSet<String> getJavaClasses() {
		return javaClasses;
	}

}
