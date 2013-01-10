package infodoc.core.ui.activity;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Form;
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
	private Form form;
	private Activity activity;
	private Case uniqueCase;
	
	public ActivityExecutor() { }
	
	public ActivityExecutor(Activity activity, User user) {
		this.form = activity.getForm();
		this.activity = activity;
		this.user = user;
	}
	
	public abstract void initLayout();
	
	public abstract Long countAvailableCases();
	
	public abstract String getHelp();
	
	public abstract Resource getIcon();
	
	@Override
	public void attach() {
		initLayout();
	}
	
	public ActivityInstance getNewActivityInstance(Case caseDto, String comments, Set<User> assignedUsers, Set<UserGroup> assignedUserGroups) {
		ActivityInstance mewInstance = new ActivityInstance();
		mewInstance.setCaseDto(caseDto);
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

	public Form getForm() {
		return form;
	}

	public Activity getActivity() {
		return activity;
	}

	public Case getUniqueCase() {
		if(uniqueCase != null) {
			uniqueCase = InfodocContainerFactory.getCaseContainer().getEntity(uniqueCase.getId());
		}
		
		return uniqueCase;
	}

	public void setUniqueCase(Case uniqueCase) {
		this.uniqueCase = uniqueCase;
	}
	
}