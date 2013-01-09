package infodoc.core.container;

import infodoc.core.dto.Notification;

import java.util.List;

@SuppressWarnings("unchecked")
public class NotificationContainer extends UserGroupFilteredContainer<Notification> {

	private static final long serialVersionUID = 1L;
	
	public NotificationContainer() {
		super(Notification.class, "activity.form.userGroup.id");
	}
	
	public List<Notification> findByActivityId(Long activityId) {
		return query(
			"select n from" +
			" Notification n where" +
			" n.activity.id = ?" +
			" and n.disabled is false",
			new Object[] {activityId});
	}

}
