package infodoc.core.ui.common;

import java.io.File;
import java.util.Collection;

import com.vaadin.Application;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Select;
import com.vaadin.ui.themes.ChameleonTheme;

import enterpriseapp.Utils;

public class InfodocTheme extends ChameleonTheme {
	
	public static final String PANEL_ACTIVITY_INSTANCES = "activity-instance";
	
	public static final String PANEL_ACTIVITY_INSTANCE_SEARCH = "activity-instance-search";
	
	public static final String CLASS_INITIAL_USERS = "initial-users";
	
	public static final String CLASS_END_USERS = "end-users";
	
	public static final String CLASS_COMMENTS = "comments";
	
	public static final String CLASS_SUMMARY = "summary";
	
	public static final String CLASS_BACKGROUND_YELLOW = "background-yellow";
	
	public static final String CLASS_BACKGROUND_BLUE = "background-blue";
	
	public static final String CLASS_BACKGROUND_RED = "background-red";
	
	public static final String CLASS_BACKGROUND_GREEN = "background-green";
	
	public static final String CLASS_BACKGROUND_GRAY = "background-gray";
	
	public static final String CLASS_BOLD = "bold";
	
	public static final String CLASS_ITALIC = "italic";
	
	public static final String CAPTION_ITALIC = "caption-italic";
	
	public static final String COOL_FONT = "cool-font";
	
	public static final String BUTTON_LINK_BOLD = "link-bold";

	public static final String iconsDirectory = "icons";
	
	public static final String iconActivityCreate = "icons/activity-create.png";
	public static final String iconActivityUpdate = "icons/activity-update.png";
	public static final String iconActivityTransfer = "icons/activity-transfer.png";
	public static final String iconActivityUnassign = "icons/activity-unassigned.png";
	public static final String iconActivityAssignUser = "icons/activity-assign-user.png";
	public static final String iconActivityAssignUserGroup = "icons/activity-assign-user-group.png";
	
	public static final String iconLayout = "icons/layout.png";
	public static final String iconTabs = "icons/tabs.png";
	public static final String iconWindows = "icons/windows.png";
	public static final String iconCloseAll = "icons/close-all.png";
	public static final String iconAdmin = "icons/admin.png";
	public static final String iconUser = "icons/user.png";
	public static final String iconUserGroup = "icons/user-group.png";
	public static final String iconCases = "icons/cases.png";
	public static final String iconPropertyValue = "icons/property-value.png";
	public static final String iconActivityInstance = "icons/activity-instance.png";
	public static final String iconNotificationInstance = "icons/notification-instance.png";
	public static final String iconDatabase = "icons/database.png";
	public static final String iconReport = "icons/report.png";
	public static final String iconDatabaseTable = "icons/database-table.png";
	public static final String iconDatabaseColumn = "icons/database-column.png";
	public static final String iconLog = "icons/log.png";
	public static final String iconAuditLog = "icons/audit-log.png";
	public static final String iconHelp = "icons/help.png";
	public static final String iconAbout = "icons/about.png";
	public static final String iconUserManual = "icons/book.png";
	public static final String iconConfiguration = "icons/configuration.png";
	public static final String iconForm = "icons/form.png";
	public static final String iconNumeration = "icons/numeration.png";
	public static final String iconProperty = "icons/property.png";
	public static final String iconValidationInstance = "icons/validation.png";
	public static final String iconClassification = "icons/classification.png";
	public static final String iconClassificationInstance = "icons/classification-instance.png";
	public static final String iconActivity = "icons/activity.png";
	public static final String iconNotification = "icons/notification.png";
	public static final String iconSearch = "icons/search.png";
	public static final String iconReports = "icons/reports.png";
	public static final String iconNumber = "icons/number.png";
	public static final String iconDate = "icons/date.png";
	public static final String iconComments = "icons/comments.png";
	public static final String iconUpdate = "icons/update.png";
	public static final String iconHome = "icons/home.png";
	public static final String iconExecuteAll = "icons/execute-all.png";
	
	public static final String infodocBoxImage = "images/infodoc-box.png";
	
	public static Select getIconSelect(Application application) {
		Select select = new Select();
		FilesystemContainer fileContainer = new FilesystemContainer(new File(Utils.getWebContextRealPath(application) + "/VAADIN/themes/infodoc/icons"));
		
		Collection<File> files = fileContainer.getItemIds();
		
		for(File archivo : files) {
			if(archivo.isFile()) {
				String path = archivo.getPath();
				String icon = path.substring(path.indexOf(InfodocTheme.iconsDirectory));
				select.addItem(icon);
				select.setItemCaption(icon, archivo.getName());
				select.setItemIcon(icon, new ThemeResource(icon));
			}
		}
		
		return select;
	}
}
