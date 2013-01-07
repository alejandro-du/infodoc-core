package infodoc.core.ui.fieldfactory;

import infodoc.core.InfodocConstants;
import infodoc.core.dto.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class UserGroupFieldFactory extends DefaultCrudFieldFactory {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void configureField(Field field, Object bean, Item item, String pid, Component uiContext, Class<?> propertyType, CrudField crudFieldAnnotation, Column columnAnnotation, JoinColumn joinColumnAnnotation, Type typeAnnotation) {
		User user = (User) EnterpriseApplication.getInstance().getUser();
		
		if(user != null && user.getUserGroup().getParentUserGroup() != null) {
			for(String fieldName : InfodocConstants.infodocDisabledFieldsForChildGroups.split(",")) {
				if(fieldName.trim().equals(pid)) {
					field.setEnabled(false);
				}
			}
		}
		
	}
	
}
