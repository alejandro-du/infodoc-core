package infodoc.core.ui.fieldfactory;

import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocTheme;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.ui.crud.DefaultCrudFieldFactory;

public class ProcessFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;
	
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
			
		} else if("printTemplate".equals(pid)) {
			field = new TextArea();
			
		} else if("icon".equals(pid)) {
			field = InfodocTheme.getIconSelect(uiContext.getApplication());
		}
		
		return field;
	}

}
