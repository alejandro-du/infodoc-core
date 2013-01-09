package infodoc.core.ui.common;

import infodoc.core.ui.fieldfactory.InfodocFieldFactory;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

import enterpriseapp.ui.crud.CrudComponent;
import enterpriseapp.ui.window.Module;

public abstract class InfodocModule implements Module {
	
	@Override
	public void init() { }
	
	protected Component addComponent(Component component, String caption, InfodocMainWindow mainWindow, boolean confirmClosing) {
		component.setSizeFull();
		mainWindow.addWorkbenchContent(component, caption, null, true, confirmClosing);
		return component;
	}
	
	protected void addCrud(CrudComponent<?> crudComponent, String caption, InfodocMainWindow mainWindow, boolean confirmClosing) {
		addComponent(crudComponent, caption, mainWindow, confirmClosing);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected CrudComponent addCrud(Class<?> type, String caption, InfodocMainWindow mainWindow, boolean verticalLayout, boolean confirmClosing) {
		CrudComponent crudComponent = new CrudComponent(type, new InfodocFieldFactory(), verticalLayout);
		addCrud(crudComponent, caption, mainWindow, confirmClosing);
		return crudComponent;
	}
	
	protected MenuItem addMenuItem(MenuItem menu, String caption, Resource icon, MenuBar.Command command, boolean canUserAccess) {
		if(canUserAccess) {
			return menu.addItem(caption, icon, command);
		}
		
		return null;
	}
	
	protected void removeEndingSeparator(MenuItem menuItem) {
		if(menuItem.getChildren().get(menuItem.getChildren().size() - 1).getText().isEmpty()) {
			menuItem.removeChild(menuItem.getChildren().get(menuItem.getChildren().size() -1));
		}
	}

	protected void addSeparator(MenuItem menuItem) {
		if(menuItem.getChildren() != null && !menuItem.getChildren().isEmpty()) {
			if(!menuItem.getChildren().get(menuItem.getChildren().size() - 1).getText().isEmpty()) {
				menuItem.addSeparator();
			}
		}
	}
	
}
