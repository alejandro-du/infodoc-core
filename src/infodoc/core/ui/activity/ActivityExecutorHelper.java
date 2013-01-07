package infodoc.core.ui.activity;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocTheme;

import java.lang.reflect.Constructor;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.ui.window.MDIWindow;
import enterpriseapp.ui.window.MDIWindow.CloseListener;

public class ActivityExecutorHelper {
	
	public static final Logger logger = LoggerFactory.getLogger(ActivityExecutorHelper.class);
	
	public static void addExecuutorComponent(MDIWindow mdiWindow, Activity activity, Process process, User user) {
		try {
			addExecutorComponent(mdiWindow, activity, process, user, null);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void addExecutorComponent(MDIWindow mdiWindow, Activity activity, final Process process, User user, ProcessInstance uniqueProcessInstance) {
		ActivityExecutor component = getActivityExecutorComponent(activity, user);
		component.setSizeFull();
		
		if(uniqueProcessInstance != null) {
			component.setUniqueProcessInstance(InfodocContainerFactory.getProcessInstanceContainer().getEntity(uniqueProcessInstance.getId()));
		}
		
		mdiWindow.closeAllWindows(ActivityExecutor.class, new CloseListener() {
			@Override
			public boolean close(Component component) {
				if(ActivityExecutor.class.isAssignableFrom(component.getClass())) {
					ActivityExecutor ae = (ActivityExecutor) component;
					return ae.getProcess().equals(process);
				}
				
				return false;
			}
		});
		
		mdiWindow.addWorkbenchContent(component, activity.getName() + " (" + activity.getProcess().getName() + ")", null, true, false);
	}

	public static ActivityExecutor getActivityExecutorComponent(Activity activity, User user) {
		try {
			Constructor<?> constructor = Class.forName(activity.getJavaClass()).getConstructor(Activity.class, User.class);
			ActivityExecutor component = (ActivityExecutor) constructor.newInstance(activity, user);
			
			return component;
			
		} catch (Exception e) {
			logger.error("Error creating ActivityExecutor " + activity.getJavaClass(), e);
			throw new RuntimeException(e);
		}
	}

	public static Resource getIcon(Activity activity, User user) {
		return ActivityExecutorHelper.getActivityExecutorComponent(activity, user).getIcon();
	}

	public static Layout getAvailableActivitiesLayout(final ProcessInstance processInstance, final User user) {
		List<Activity> activities = InfodocContainerFactory.getActivityContainer().findByProcessInstanceIdAndUserId(processInstance.getId(), user.getId());
		HorizontalLayout layout = new HorizontalLayout();
		
		for(final Activity activity : activities) {
			Button button = new Button(activity.getName());
			button.setStyleName(InfodocTheme.BUTTON_LINK);
			button.setIcon(ActivityExecutorHelper.getIcon(activity, user));
			
			button.addListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ActivityExecutorHelper.addExecutorComponent((MDIWindow) EnterpriseApplication.getInstance().getMainWindow(), activity, processInstance.getProcess(), user, processInstance);
				}
			});
			
			layout.addComponent(button);
		}
		
		return layout;
	}

}
