package infodoc.core.ui.fieldfactory;

import infodoc.core.InfodocConstants;
import infodoc.core.container.validator.HqlReportParameterJavaClassValidator;
import infodoc.core.dto.HqlReportParameter;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class HqlReportParameterFieldFactory extends DefaultCrudFieldFactory {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("javaClass".equals(pid)) {
			final Select select = new Select();
			select.addItem(BigDecimal.class.getName());
			select.addItem(Boolean.class.getName());
			select.addItem(Double.class.getName());
			select.addItem(Float.class.getName());
			select.addItem(Integer.class.getName());
			select.addItem(Long.class.getName());
			select.addItem(String.class.getName());
			select.setNewItemsAllowed(true);
			
			HqlReportParameter parameter = (HqlReportParameter) bean;
			
			if(parameter.getJavaClass() != null && !parameter.getJavaClass().isEmpty()) {
				select.addItem(parameter.getJavaClass());
			}
			
			field = select;
		}
		
		return field;
	}

	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		if("javaClass".equals(pid)) {
			field.addValidator(new HqlReportParameterJavaClassValidator(InfodocConstants.uiErrorJavaClassInvalidOrNotFound));
		}
	}
	
}
