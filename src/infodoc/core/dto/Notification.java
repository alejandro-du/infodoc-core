package infodoc.core.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="notification")
@CrudTable(filteringPropertyName="id")
public class Notification extends Dto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="activity_id")
	private Activity activity;
	
	@Column(name="notify_current_user", nullable=false)
	private boolean notifyCurrentUser;
	
	@Column(name="notify_next_users", nullable=false)
	private boolean notifyNextUsers;
	
	@Column(name="notify_current_user_group", nullable=false)
	private boolean notifyCurrentUserGroup;
	
	@Column(name="notify_next_user_groups", nullable=false)
	private boolean notifyNextUserGroups;
	
	@ManyToMany
	@JoinTable(
		name="notification_has_users"
		, joinColumns={
			@JoinColumn(name="notification_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="user_id")
		}
	)
	private Set<User> users;
	
	@Column(name="email")
	@CrudField(isEmail=true)
	private String email;
	
	@Column(name="send_after_minutes")
	private Integer sendAfterMinutes;
	
	@Column(name="send_after_hours")
	private Integer sendAfterHours;
	
	@Column(name="send_after_days")
	private Integer sendAfterDays;
	
	@Column(name="send_after_months")
	private Integer sendAfterMonths;
	
	@Column(name="automatically_cancel", nullable=false)
	private boolean automaticallyCancel = true;
	
	@Column(name="subject", nullable=false)
	private String subject;
	
	@Column(name="message", length=1024)
	private String message;
	
	@Column(name="include_properties", nullable=false)
	private boolean includeProperties;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public boolean isNotifyCurrentUser() {
		return notifyCurrentUser;
	}

	public void setNotifyCurrentUser(boolean notifyCurrentUser) {
		this.notifyCurrentUser = notifyCurrentUser;
	}

	public boolean isNotifyNextUsers() {
		return notifyNextUsers;
	}

	public void setNotifyNextUsers(boolean notifyNextUsers) {
		this.notifyNextUsers = notifyNextUsers;
	}

	public boolean notifyCurrentUserGroup() {
		return notifyCurrentUserGroup;
	}

	public void setNotifyCurrentUserGroup(boolean notifyCurrentUserGroup) {
		this.notifyCurrentUserGroup = notifyCurrentUserGroup;
	}

	public boolean isNotifyNextUserGroups() {
		return notifyNextUserGroups;
	}

	public void setNotifyNextUserGroups(boolean notifyNextUserGroups) {
		this.notifyNextUserGroups = notifyNextUserGroups;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> userss) {
		this.users = userss;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSendAfterMinutes() {
		return sendAfterMinutes;
	}

	public void setSendAfterMinutes(Integer sendAfterMinutes) {
		this.sendAfterMinutes = sendAfterMinutes;
	}

	public Integer getSendAfterHours() {
		return sendAfterHours;
	}

	public void setSendAfterHours(Integer sendAfterHours) {
		this.sendAfterHours = sendAfterHours;
	}

	public Integer getSendAfterDays() {
		return sendAfterDays;
	}

	public void setSendAfterDays(Integer sendAfterDays) {
		this.sendAfterDays = sendAfterDays;
	}

	public Integer getSendAfterMonths() {
		return sendAfterMonths;
	}

	public void setSendAfterMonths(Integer sendAfterMonths) {
		this.sendAfterMonths = sendAfterMonths;
	}

	public boolean isAutomaticallyCancel() {
		return automaticallyCancel;
	}

	public void setAutomaticallyCancel(boolean automaticallyCancel) {
		this.automaticallyCancel = automaticallyCancel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isIncludeProperties() {
		return includeProperties;
	}

	public void setIncludeProperties(boolean includeProperties) {
		this.includeProperties = includeProperties;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
