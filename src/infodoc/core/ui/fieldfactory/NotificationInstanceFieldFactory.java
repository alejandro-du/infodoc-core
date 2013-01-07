package infodoc.core.ui.fieldfactory;

import java.util.TimeZone;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.Utils;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class NotificationInstanceFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;

	@Override
	public Field createCustomField(Object bean, Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("sendTime".equals(pid)) {
			DateField dateField = (DateField) createDateField(pid, propertyType);
			dateField.setResolution(DateField.RESOLUTION_SEC);
			dateField.setDateFormat(Utils.getAlternateDateTimeFormatPattern());
			dateField.setTimeZone(TimeZone.getTimeZone(EnterpriseApplication.getInstance().getTimeZoneId()));
			field = dateField;
		}
		
		return field;
	}

}
