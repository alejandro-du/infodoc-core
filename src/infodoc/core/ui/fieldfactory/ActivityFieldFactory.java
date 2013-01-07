package infodoc.core.ui.fieldfactory;

import java.util.LinkedHashSet;

import infodoc.core.InfodocConstants;
import infodoc.core.container.validator.ActivityJavaClassValidator;
import infodoc.core.dto.Activity;
import infodoc.core.ui.activity.ActivityExecutor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class ActivityFieldFactory extends DefaultCrudFieldFactory {

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
			
			Activity activity = (Activity) bean;
			
			if(activity.getJavaClass() != null && !activity.getJavaClass().isEmpty()) {
				select.addItem(activity.getJavaClass());
			}
			
			select.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String javaClass = (String) event.getProperty().getValue();
					
					if(javaClass != null && !javaClass.isEmpty()) {
						try {
							ActivityExecutor activityInstance = (ActivityExecutor) Class.forName(javaClass).newInstance();
							select.setDescription(activityInstance.getHelp());
							
						} catch (ClassNotFoundException e) {
						} catch (Exception e) {
							LoggerFactory.getLogger(PropertyFieldFactory.class).error("Error creating AbstractActivityInstanceComponent.", e);
							throw new RuntimeException(e);
						}
						
					}
				}
			});
			
			field = select;
			
		} else if("parameter".equals(pid)) {
			field = new TextArea();
		}
		
		return field;
	}
	
	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		if("javaClass".equals(pid)) {
			field.addValidator(new ActivityJavaClassValidator(InfodocConstants.uiErrorJavaClassInvalidOrNotFound));
		}
	}

	public static LinkedHashSet<String> getJavaClasses() {
		return javaClasses;
	}

}
