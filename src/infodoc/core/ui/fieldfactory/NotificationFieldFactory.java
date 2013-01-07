package infodoc.core.ui.fieldfactory;


import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;

import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class NotificationFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;

	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("message".equals(pid)) {
			field = new TextArea();
		}
		
		return field;
	}
	
}
