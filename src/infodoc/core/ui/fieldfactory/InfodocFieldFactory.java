package infodoc.core.ui.fieldfactory;

import infodoc.core.dto.Property;
import infodoc.core.dto.Notification;
import infodoc.core.dto.Numeration;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.HqlReport;
import infodoc.core.dto.NotificationInstance;
import infodoc.core.dto.HqlReportParameter;
import infodoc.core.dto.Classification;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.dto.Validation;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class InfodocFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if(Property.class.isAssignableFrom(bean.getClass())) {
			field = new PropertyFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(Numeration.class.isAssignableFrom(bean.getClass())) {
			field = new NumerationFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(Notification.class.isAssignableFrom(bean.getClass())) {
			field = new NotificationFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(JavaReport.class.isAssignableFrom(bean.getClass())) {
			field = new JavaReportFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(HqlReport.class.isAssignableFrom(bean.getClass())) {
			field = new HqlReportFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(UserGroup.class.isAssignableFrom(bean.getClass())) {
			field = new UserGroupFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(NotificationInstance.class.isAssignableFrom(bean.getClass())) {
			field = new NotificationInstanceFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(HqlReportParameter.class.isAssignableFrom(bean.getClass())) {
			field = new HqlReportParameterFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(Classification.class.isAssignableFrom(bean.getClass())) {
			field = new ClassificationFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(Process.class.isAssignableFrom(bean.getClass())) {
			field = new ProcessFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(Activity.class.isAssignableFrom(bean.getClass())) {
			field = new ActivityFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(User.class.isAssignableFrom(bean.getClass())) {
			field = new UserFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
			
		} else if(Validation.class.isAssignableFrom(bean.getClass())) {
			field = new ValidationFieldFactory().createCustomField(bean, item, pid, uiContext, propertyType);
		}
		
		return field;
	}

	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		super.configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
		
		if(Property.class.isAssignableFrom(bean.getClass())) {
			new PropertyFieldFactory().configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
			
		} else if(UserGroup.class.isAssignableFrom(bean.getClass())) {
			new UserGroupFieldFactory().configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
			
		} else if(JavaReport.class.isAssignableFrom(bean.getClass())) {
			new JavaReportFieldFactory().configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
			
		} else if(HqlReportParameter.class.isAssignableFrom(bean.getClass())) {
			new HqlReportParameterFieldFactory().configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
			
		} else if(Activity.class.isAssignableFrom(bean.getClass())) {
			new ActivityFieldFactory().configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
			
		} else if(Validation.class.isAssignableFrom(bean.getClass())) {
			new ValidationFieldFactory().configureField(field, bean, item, pid, uiContext, propertyType, crudFieldAnnotation, columnAnnotation, joinColumnAnnotation, typeAnnotation);
		}
	}
	
}
