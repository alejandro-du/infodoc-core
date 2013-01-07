package infodoc.core.ui.activity;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.ActivityInstance;
import infodoc.core.dto.User;

import java.util.Calendar;
import java.util.Set;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.CustomComponent;

public abstract class ActivityExecutor extends CustomComponent {

	private static final long serialVersionUID = 1L;

	private User user;
	private Process process;
	private Activity activity;
	private ProcessInstance uniqueProcessInstance;
	
	public ActivityExecutor() { }
	
	public ActivityExecutor(Activity activity, User user) {
		this.process = activity.getProcess();
		this.activity = activity;
		this.user = user;
	}
	
	public abstract void initLayout();
	
	public abstract Long countAvailableProcessInstances();
	
	public abstract String getHelp();
	
	public abstract Resource getIcon();
	
	@Override
	public void attach() {
		initLayout();
	}
	
	public ActivityInstance getNewActivityInstance(ProcessInstance processInstance, String comments, Set<User> assignedUsers, Set<UserGroup> assignedUserGroups) {
		ActivityInstance mewInstance = new ActivityInstance();
		mewInstance.setProcessInstance(processInstance);
		mewInstance.setActivity(getActivity());
		mewInstance.setUser(getUser());
		mewInstance.setAssignedUsers(assignedUsers);
		mewInstance.setAssignedUserGroups(assignedUserGroups);
		mewInstance.setExecutionTime(Calendar.getInstance().getTime());
		mewInstance.setComments(comments);
		
		return mewInstance;
	}
	
	public User getUser() {
		return InfodocContainerFactory.getUserContainer().getEntity(user.getId());
	}

	public Process getProcess() {
		return process;
	}

	public Activity getActivity() {
		return activity;
	}

	public ProcessInstance getUniqueProcessInstance() {
		if(uniqueProcessInstance != null) {
			uniqueProcessInstance = InfodocContainerFactory.getProcessInstanceContainer().getEntity(uniqueProcessInstance.getId());
		}
		
		return uniqueProcessInstance;
	}

	public void setUniqueProcessInstance(ProcessInstance uniqueProcessInstance) {
		this.uniqueProcessInstance = uniqueProcessInstance;
	}
	
}