package infodoc.core.ui.fieldfactory;

import java.util.TimeZone;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class UserFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("timeZone".equals(pid)) {
			Select select = new Select();
			String[] availableTimeZones = TimeZone.getAvailableIDs();
			
			for(String zones : availableTimeZones) {
				select.addItem(zones);
				field = select;
			}
		}
		
		return field;
	}
	
}
