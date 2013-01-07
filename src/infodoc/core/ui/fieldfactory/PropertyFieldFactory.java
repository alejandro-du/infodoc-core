package infodoc.core.ui.fieldfactory;

import infodoc.core.InfodocConstants;
import infodoc.core.container.validator.PropertyJavaClassValidator;
import infodoc.core.dto.Property;
import infodoc.core.dto.User;
import infodoc.core.field.FieldFactory;
import infodoc.core.ui.comun.InfodocTheme;

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
import com.vaadin.ui.TextArea;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class PropertyFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;
	
	private static LinkedHashSet<String> javaClasses = new LinkedHashSet<String>();
	
	@Override
	public Field createCustomField(Object bean, final Item item, String pid, Component uiContext, Class<?> propertyType) {
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
			
			Property property = (Property) bean;
			
			if(property.getJavaClass() != null && !property.getJavaClass().isEmpty()) {
				select.addItem(property.getJavaClass());
			}
			
			select.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String javaClass = (String) event.getProperty().getValue();
					
					if(javaClass != null && !javaClass.isEmpty()) {
						try {
							FieldFactory fieldFactory = (FieldFactory) Class.forName(javaClass).newInstance();
							select.setDescription(fieldFactory.getHelp());
							
						} catch (ClassNotFoundException e) {
						} catch (Exception e) {
							LoggerFactory.getLogger(PropertyFieldFactory.class).error("Error creating FieldFactory.", e);
							throw new RuntimeException(e);
						}
						
					}
				}
			});
			
			field = select;
			
		} else if("icon".equals(pid)) {
			field = InfodocTheme.getIconSelect(uiContext.getApplication());
			
		} else if("color".equals(pid)) {
			Select select = new Select();
			
			select.addItem(InfodocTheme.CLASS_BACKGROUND_YELLOW);
			select.addItem(InfodocTheme.CLASS_BACKGROUND_BLUE);
			select.addItem(InfodocTheme.CLASS_BACKGROUND_RED);
			select.addItem(InfodocTheme.CLASS_BACKGROUND_GREEN);
			select.addItem(InfodocTheme.CLASS_BACKGROUND_GRAY);
			
			select.setItemCaption(InfodocTheme.CLASS_BACKGROUND_YELLOW, InfodocConstants.uiColorYellow);
			select.setItemCaption(InfodocTheme.CLASS_BACKGROUND_BLUE, InfodocConstants.uiColorBlue);
			select.setItemCaption(InfodocTheme.CLASS_BACKGROUND_RED, InfodocConstants.uiColorRed);
			select.setItemCaption(InfodocTheme.CLASS_BACKGROUND_GREEN, InfodocConstants.uiColorGreen);
			select.setItemCaption(InfodocTheme.CLASS_BACKGROUND_GRAY, InfodocConstants.uiColorGray);
			
			field = select;
			
		} else if("parameter".equals(pid)) {
			field = new TextArea();
		}
		
		return field;
	}

	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		if("javaClass".equals(pid)) {
			field.addValidator(new PropertyJavaClassValidator(InfodocConstants.uiErrorJavaClassInvalidOrNotFound));
		}
	}

	public static LinkedHashSet<String> getJavaClasses() {
		return javaClasses;
	}

}
