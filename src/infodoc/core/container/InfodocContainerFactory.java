package infodoc.core.container;

import infodoc.core.dto.Property;
import infodoc.core.dto.ClassificationValue;
import infodoc.core.dto.Notification;
import infodoc.core.dto.Numeration;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.HqlReport;
import infodoc.core.dto.NotificationInstance;
import infodoc.core.dto.HqlReportParameter;
import infodoc.core.dto.Classification;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.ActivityInstance;
import infodoc.core.dto.User;
import infodoc.core.dto.Validation;
import enterpriseapp.hibernate.ContainerFactory;
import enterpriseapp.hibernate.DefaultHbnContainer;
import enterpriseapp.hibernate.dto.AuditLog;

public class InfodocContainerFactory extends ContainerFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public DefaultHbnContainer getContainer(Class<?> clazz) {
		if(clazz.equals(Property.class)) {
			return getPropertyContainer();
			
		} else if(clazz.equals(ClassificationValue.class)) {
			return getClassificationValueContainer();
			
		} else if(clazz.equals(Numeration.class)) {
			return getNumerationContainer();
			
		} else if(clazz.equals(PropertyValue.class)) {
			return getPropertyValueContainer();
			
		} else if(clazz.equals(ProcessInstance.class)) {
			return getProcessInstanceContainer();
			
		} else if(clazz.equals(NotificationInstance.class)) {
			return getNotificationInstanceContainer();
			
		} else if(clazz.equals(UserGroup.class)) {
			return getUserGroupContainer();
			
		} else if(AuditLog.class.isAssignableFrom(clazz)) {
			return getAuditLogContainer();
			
		} else if(clazz.equals(Notification.class)) {
			return getNotificationContainer();
			
		} else if(clazz.equals(Classification.class)) {
			return getClassificationContainer();
			
		} else if(clazz.equals(Process.class)) {
			return getProcessContainer();
			
		} else if(clazz.equals(Activity.class)) {
			return getActivityContainer();
			
		} else if(clazz.equals(ActivityInstance.class)) {
			return getActivityInstanceContainer();
			
		} else if(clazz.equals(User.class)) {
			return getUserContainer();
			
		} else if(clazz.equals(JavaReport.class)) {
			return getJavaReportContainer();
			
		} else if(clazz.equals(HqlReport.class)) {
			return getHqlReportContainer();
			
		} else if(clazz.equals(HqlReportParameter.class)) {
			return getHqlReportParameterContainer();
			
		} else if(clazz.equals(Validation.class)) {
			return getValidationContainer();
		}
		
		return getDefaultFactory().getContainer(clazz);
	}
	
	public static PropertyContainer getPropertyContainer() {
		return new PropertyContainer();
	}

	public static ClassificationValueContainer getClassificationValueContainer() {
		return new ClassificationValueContainer();
	}

	public static NumerationContainer getNumerationContainer() {
		return new NumerationContainer();
	}

	public static PropertyValueContainer getPropertyValueContainer() {
		return new PropertyValueContainer();
	}

	public static ProcessInstanceContainer getProcessInstanceContainer() {
		return new ProcessInstanceContainer();
	}

	public static NotificationInstanceContainer getNotificationInstanceContainer() {
		return new NotificationInstanceContainer();
	}

	public static UserGroupContainer getUserGroupContainer() {
		return new UserGroupContainer();
	}

	public static AuditLogContainer getAuditLogContainer() {
		return new AuditLogContainer();
	}

	public static NotificationContainer getNotificationContainer() {
		return new NotificationContainer();
	}

	public static ClassificationContainer getClassificationContainer() {
		return new ClassificationContainer();
	}

	public static ProcessContainer getProcessContainer() {
		return new ProcessContainer();
	}

	public static ActivityContainer getActivityContainer() {
		return new ActivityContainer();
	}

	public static ActivityInstanceContainer getActivityInstanceContainer() {
		return new ActivityInstanceContainer();
	}

	public static UserContainer getUserContainer() {
		return new UserContainer();
	}

	public static JavaReportContainer getJavaReportContainer() {
		return new JavaReportContainer();
	}

	public static HqlReportContainer getHqlReportContainer() {
		return new HqlReportContainer();
	}

	public static HqlReportParameterContainer getHqlReportParameterContainer() {
		return new HqlReportParameterContainer();
	}

	public static ValidationContainer getValidationContainer() {
		return new ValidationContainer();
	}

}
