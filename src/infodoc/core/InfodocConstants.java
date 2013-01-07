package infodoc.core;

import enterpriseapp.Utils;
import enterpriseapp.ui.Constants;

public abstract class InfodocConstants extends Constants {
	
	public static final String infodocDeveloperUrl = "http://alejandroduarte.weebly.com";
	public static final String uiInfodoc = Utils.getProperty("ui.infodoc");
	public static final String infodocVersion = Utils.getProperty("infodoc.version");
	public static final String infodocModules = Utils.getProperty("infodoc.modules");
	public static final String infodocCompanyName = Utils.getProperty("infodoc.companyName");
	public static final String infodocCompanyId = Utils.getProperty("infodoc.companyId");
	public static final String infodocCompanyAddress= Utils.getProperty("infodoc.companyAddress");
	public static final String infodocDefaultPrintTemplate = Utils.getProperty("infodoc.defaultPrintTemplate");
	public static final String infodocDefaultNotificationTemplate = Utils.getProperty("infodoc.defaultNotificationTemplate");
	public static final Integer infodocProcessInstancesPerPage = Integer.parseInt(Utils.getProperty("infodoc.processInstancesPerPage"));
	public static final String infodocDisabledFieldsForChildGroups = Utils.getProperty("infodoc.disabledFieldsForChildGroups");
	
	public static final String uiUserLogin = Utils.getProperty("ui.user.login");
	public static final String uiUserPassword = Utils.getProperty("ui.user.password");
	public static final String uiLogIn = Utils.getProperty("ui.logIn");
	public static final String uiWrongCredentials = Utils.getProperty("ui.worngCredentials");
	public static final String uiOptions = Utils.getProperty("ui.options");
	public static final String uiLogout = Utils.getProperty("ui.logout");
	public static final String uiAbout = Utils.getProperty("ui.about");
	public static final String uiUserManual = Utils.getProperty("ui.userManual");
	public static final String uiUserManualPdfName = Utils.getProperty("ui.userManualPdfName");
	public static final String uiCurrentPassword = Utils.getProperty("ui.currentPassword");
	public static final String uiNewPassword = Utils.getProperty("ui.newPassword");
	public static final String uiConfirmNewPassword = Utils.getProperty("ui.confirmNewPassword");
	public static final String uiPasswordsDoesntMatch = Utils.getProperty("ui.passwordsDoesntMatch");
	public static final String uiLog = Utils.getProperty("ui.log");
	public static final String uiExecuteAll = Utils.getProperty("ui.executeAll");
	public static final String uiConfirmExecuteForAll = Utils.getProperty("ui.confirmExecuteForAll");
	public static final String uiForm = Utils.getProperty("ui.form");
	public static final String uiSearch = Utils.getProperty("ui.search");
	public static final String uiClearForm = Utils.getProperty("ui.clearForm");
	public static final String uiPrint = Utils.getProperty("ui.print");
	public static final String uiDefaultLogin = Utils.getProperty("ui.defaultLogin", "");
	public static final String uiDefaultPassword = Utils.getProperty("ui.defaultPassword", "");
	public static final String uiDate = Utils.getProperty("ui.date");
	public static final String uiFrom = Utils.getProperty("ui.from");
	public static final String uiTo = Utils.getProperty("ui.to");
	public static final String uiTotal = Utils.getProperty("ui.total");
	public static final String uiFirstActivityInstance = Utils.getProperty("ui.firstActivityInstance");
	public static final String uiLastActivityInstance = Utils.getProperty("ui.lastActivityInstance");
	public static final String uiLastActivityInstances = Utils.getProperty("ui.lastActivityInstances");
	public static final String uiPendingProcessInstances = Utils.getProperty("ui.pendingProcessInstances");
	public static final String uiDateResolution = Utils.getProperty("ui.dateResolution");
	public static final String uiYear = Utils.getProperty("ui.year");
	public static final String uiMonth = Utils.getProperty("ui.month");
	public static final String uiDay = Utils.getProperty("ui.day");
	public static final String uiHour = Utils.getProperty("ui.hour");
	public static final String uiMinute = Utils.getProperty("ui.minute");
	public static final String uiSecond = Utils.getProperty("ui.second");
	public static final String uiDefaultUserGroupName = Utils.getProperty("ui.defaultUserGroupName");
	
	public static final String uiColorYellow = Utils.getProperty("ui.colorYellow");
	public static final String uiColorBlue = Utils.getProperty("ui.colorBlue");
	public static final String uiColorRed = Utils.getProperty("ui.colorRed");
	public static final String uiColorGreen = Utils.getProperty("ui.colorGreen");
	public static final String uiColorGray = Utils.getProperty("ui.colorGray");
	
	public static final String uiAdmin = Utils.getProperty("ui.admin");
	public static final String uiConfiguration = Utils.getProperty("ui.configuration");
	public static final String uiHelp = Utils.getProperty("ui.help");
	
	public static final String uiUser = Utils.getProperty("ui.user");
	public static final String uiUserGroup = Utils.getProperty("ui.userGroup");
	public static final String uiActivity = Utils.getProperty("ui.activity");
	
	public static final String uiActivities = Utils.getProperty("ui.activities");
	public static final String uiActivitiesInstances = Utils.getProperty("ui.activityInstances");
	public static final String uiAuditLog = Utils.getProperty("ui.auditLog");
	public static final String uiClassifications = Utils.getProperty("ui.classifications");
	public static final String uiClassificationValues = Utils.getProperty("ui.classificationValues");
	public static final String uiHqlReports = Utils.getProperty("ui.hqlReports");
	public static final String uiJavaReports = Utils.getProperty("ui.javaReports");
	public static final String uiNotifications = Utils.getProperty("ui.notifications");
	public static final String uiNotificationInstances = Utils.getProperty("ui.notificationInstances");
	public static final String uiNumerations = Utils.getProperty("ui.numerations");
	public static final String uiProcesses = Utils.getProperty("ui.processes");
	public static final String uiProcessInstances = Utils.getProperty("ui.processInstances");
	public static final String uiProperties = Utils.getProperty("ui.properties");
	public static final String uiPropertyValues = Utils.getProperty("ui.propertyValues");
	public static final String uiUsers = Utils.getProperty("ui.users");
	public static final String uiUserGroups = Utils.getProperty("ui.userGroups");
	public static final String uiValidations = Utils.getProperty("ui.validations");
	
	public static final String uiProcessNumber = Utils.getProperty("ui.processInstance.number");
	
	public static final String uiUserDashboardUrl = Utils.getProperty("ui.user.dashboardUrl");
	public static final String uiUserTimeZone = Utils.getProperty("ui.user.timeZone");
	
	public static final String uiUserPasswordError = Utils.getProperty("ui.user.password.error");
	public static final String uiErrorPropertiesModified = Utils.getProperty("ui.errorModifiedProperties");
	public static final String uiErrorProcessInstanceNotAvailable = Utils.getProperty("ui.errorProcessInstanceNotAvailable");
	public static final String uiErrorJavaClassInvalidOrNotFound = Utils.getProperty("ui.errorJavaClassInvalidOrNotFound");
	public static final String uiErrorIncorrectParam(String property) { return Utils.getProperty("ui.errorIncorrectParam", new String[] {property}); };
	public static final String uiInvalidDateValue = Utils.getProperty("ui.invalidDateValue");
	public static final String uiInvalidEmailValue = Utils.getProperty("ui.invalidEmailValue");
	public static final String uiInvalidValue = Utils.getProperty("ui.invalidValue");
	public static final String uiErrorModuleNotFound = Utils.getProperty("ui.moduleNotFound");
	
}
