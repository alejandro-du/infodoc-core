package infodoc.core.ui.fieldfactory;

import infodoc.core.InfodocConstants;
import infodoc.core.container.validator.ValidationJavaClassValidator;
import infodoc.core.dto.User;
import infodoc.core.dto.Validation;
import infodoc.core.ui.validator.ValidatorFactory;

import java.util.LinkedHashSet;

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

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class ValidationFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;

	private static LinkedHashSet<String> javaClasses = new LinkedHashSet<String>();
	
	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("userGroup".equals(pid)) {
			User user = (User) EnterpriseApplication.getInstance().getUser();
			
			if(user != null && user.getUserGroup().getParentUserGroup() != null) {
				Select select = new Select();
				select.addItem(user.getUserGroup().getParentUserGroup());
				select.setEnabled(false);
				field = select;
			}
			
		} else if("javaClass".equals(pid)) {
			final Select select = new Select();
			select.setNewItemsAllowed(true);
			
			for(String javaClass : javaClasses) {
				select.addItem(javaClass);
			}
			
			Validation validation = (Validation) bean;
			
			if(validation.getJavaClass() != null && !validation.getJavaClass().isEmpty()) {
				select.addItem(validation.getJavaClass());
			}
			
			select.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String javaClass = (String) event.getProperty().getValue();
					
					if(javaClass != null && !javaClass.isEmpty()) {
						try {
							ValidatorFactory validatorFactory = (ValidatorFactory) Class.forName(javaClass).newInstance();
							select.setDescription(validatorFactory.getHelp());
							
						} catch (ClassNotFoundException e) {
						} catch (Exception e) {
							LoggerFactory.getLogger(PropertyFieldFactory.class).error("Error creating ValidatorFactory.", e);
							throw new RuntimeException(e);
						}
						
					}
				}
			});
			
			field = select;
		}
		
		return field;
	}

	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		if("javaClass".equals(pid)) {
			field.addValidator(new ValidationJavaClassValidator(InfodocConstants.uiErrorJavaClassInvalidOrNotFound));
		}
	}

	public static LinkedHashSet<String> getJavaClasses() {
		return javaClasses;
	}

}