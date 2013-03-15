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
import javax.persistence.UniqueConstraint;

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="user_group", uniqueConstraints={@UniqueConstraint(columnNames={"name", "parent_user_group_id"})})
@CrudTable(filteringPropertyName="name")
public class UserGroup extends Dto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="access_admin_module", nullable=false)
	private boolean accessAdminModule;

	@Column(name="access_admin_users", nullable=false)
	private boolean accessAdminUsers;
	
	@Column(name="access_admin_user_groups", nullable=false)
	private boolean accessAdminUserGroups;
	
	@Column(name="access_admin_forms", nullable=false)
	private boolean accessAdminCases;
	
	@Column(name="access_admin_property_values", nullable=false)
	private boolean accessAdminPropertyValues;
	
	@Column(name="access_admin_activity_instances", nullable=false)
	private boolean accessAdminActivityInstances;
	
	@Column(name="can_create_delete_users", nullable=false)
	private boolean canCreateAndDeleteUsers;
	
	@Column(name="access_hql_query", nullable=false)
	private boolean accessHqlQuery;
	
	@Column(name="access_admin_hql_reports", nullable=false)
	private boolean accessAdminHqlReports;
	
	@Column(name="access_log_files", nullable=false)
	private boolean accessLogFiles;
	
	@Column(name="access_audit_log", nullable=false)
	private boolean accessAuditLog;
	
	@Column(name="access_config_module", nullable=false)
	private boolean accesoConfigModule;

	@Column(name="access_config_properties", nullable=false)
	private boolean accessConfigProperties;
	
	@Column(name="access_config_validations", nullable=false)
	private boolean accessConfigValidations;
	
	@Column(name="access_config_numeration", nullable=false)
	private boolean accessConfigNumeration;
	
	@Column(name="access_config_forms", nullable=false)
	private boolean accessConfigForms;
	
	@Column(name="access_config_activities", nullable=false)
	private boolean accessConfigActivities;
	
	@Column(name="access_config_notifications", nullable=false)
	private boolean accessConfigNotifications;
	
	@Column(name="access_config_classifications", nullable=false)
	private boolean accessConfigClassifications;
	
	@Column(name="access_config_classification_values", nullable=false)
	private boolean accessConfigClassificationValues;
	
	@Column(name="access_admin_notification_instances", nullable=false)
	private boolean accessAdminNotificationInstances;
	
	@Column(name="access_basic_module", nullable=false)
	private boolean accessBasicModule;

	@Column(name="access_last_activity_instances", nullable=false)
	private boolean accessLastActivityInstances;
	
	@Column(name="access_admin_java_reports", nullable=false)
	private boolean accessAdminJavaReports;
	
	@ManyToOne
	@JoinColumn(name="parent_user_group_id")
	private UserGroup parentUserGroup;
	
    @ManyToMany
	@JoinTable(
		name="user_group_searches_form"
		, joinColumns={
			@JoinColumn(name="user_group_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="form_id")
		}
	)
	private Set<Form> accessSearchForm;
	
    @ManyToMany
	@JoinTable(
		name="user_group_has_java_report"
		, joinColumns={
			@JoinColumn(name="user_group_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="java_report_id")
		}
	)
	private Set<JavaReport> javaReports;
	
    @ManyToMany(mappedBy="userGroup")
	private Set<User> users;
	
    @ManyToMany
	@JoinTable(
		name="user_group_has_property"
		, joinColumns={
			@JoinColumn(name="user_group_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="property_id")
		}
	)
	private Set<Property> properties;
	
    @ManyToMany
	@JoinTable(
		name="user_group_has_activity"
		, joinColumns={
			@JoinColumn(name="user_group_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="activity_id")
		}
	)
	private Set<Activity> activities;
	
    @ManyToMany
	@JoinTable(
		name="user_group_can_assign_user_group"
		, joinColumns={
			@JoinColumn(name="user_group_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="can_assign_user_group_id")
		}
	)
	private Set<UserGroup> canAssignToUserGroups;
	
    @ManyToMany
	@JoinTable(
		name="user_group_can_assign_user"
		, joinColumns={
			@JoinColumn(name="user_group_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="user_id")
		}
	)
	private Set<User> canAssignToUsers;
	
	@Override
	public String toString() {
		return parentUserGroup == null ? name : parentUserGroup.getName() + "-" + name;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAccessAdminModule() {
		return accessAdminModule;
	}

	public void setAccessAdminModule(boolean accessAdminModule) {
		this.accessAdminModule = accessAdminModule;
	}

	public boolean isAccessAdminUsers() {
		return accessAdminUsers;
	}

	public void setAccessAdminUsers(boolean accessAdminUsers) {
		this.accessAdminUsers = accessAdminUsers;
	}

	public boolean isCanCreateAndDeleteUsers() {
		return canCreateAndDeleteUsers;
	}

	public void setCanCreateAndDeleteUsers(boolean canCreateAndDeleteUsers) {
		this.canCreateAndDeleteUsers = canCreateAndDeleteUsers;
	}

	public boolean isAccessAdminUserGroups() {
		return accessAdminUserGroups;
	}

	public void setAccessAdminUserGroups(boolean accessAdminUserGroups) {
		this.accessAdminUserGroups = accessAdminUserGroups;
	}

	public boolean isAccessHqlQuery() {
		return accessHqlQuery;
	}
	
	public void setAccessHqlQuery(boolean accessHqlQuery) {
		this.accessHqlQuery = accessHqlQuery;
	}

	public boolean isAccessAdminHqlReports() {
		return accessAdminHqlReports;
	}

	public void setAccessAdminHqlReports(boolean accessAdminHqlReports) {
		this.accessAdminHqlReports = accessAdminHqlReports;
	}

	public boolean isAccessAuditLog() {
		return accessAuditLog;
	}

	public void setAccessAuditLog(boolean accessAuditLog) {
		this.accessAuditLog = accessAuditLog;
	}
	
	public boolean isAccessLogFiles() {
		return accessLogFiles;
	}

	public void setAccessLogFiles(boolean accessLogFiles) {
		this.accessLogFiles = accessLogFiles;
	}

	public boolean isAccesoConfigModule() {
		return accesoConfigModule;
	}

	public void setAccesoConfigModule(boolean accesoConfigModule) {
		this.accesoConfigModule = accesoConfigModule;
	}

	public boolean isAccessConfigProperties() {
		return accessConfigProperties;
	}

	public void setAccessConfigProperties(boolean accessConfigProperties) {
		this.accessConfigProperties = accessConfigProperties;
	}

	public boolean isAccessConfigValidations() {
		return accessConfigValidations;
	}

	public void setAccessConfigValidations(boolean accessConfigValidations) {
		this.accessConfigValidations = accessConfigValidations;
	}

	public boolean isAccessConfigNumeration() {
		return accessConfigNumeration;
	}

	public void setAccessConfigNumeration(boolean accessConfigNumeration) {
		this.accessConfigNumeration = accessConfigNumeration;
	}

	public boolean isAccessConfigForms() {
		return accessConfigForms;
	}

	public void setAccessConfigForms(boolean accessConfigForms) {
		this.accessConfigForms = accessConfigForms;
	}

	public boolean isAccessConfigActivities() {
		return accessConfigActivities;
	}

	public void setAccessConfigActivities(boolean accessConfigActivities) {
		this.accessConfigActivities = accessConfigActivities;
	}

	public boolean isAccessConfigNotifications() {
		return accessConfigNotifications;
	}

	public void setAccessConfigNotifications(boolean accessConfigNotifications) {
		this.accessConfigNotifications = accessConfigNotifications;
	}

	public boolean isAccessConfigClassifications() {
		return accessConfigClassifications;
	}

	public void setAccessConfigClassifications(boolean accessConfigClassifications) {
		this.accessConfigClassifications = accessConfigClassifications;
	}

	public boolean isAccessConfigClassificationValues() {
		return accessConfigClassificationValues;
	}

	public void setAccessConfigClassificationValues(boolean accessConfigClassificationValues) {
		this.accessConfigClassificationValues = accessConfigClassificationValues;
	}

	public boolean isAccessAdminCases() {
		return accessAdminCases;
	}

	public void setAccessAdminCases(boolean accessAdminCases) {
		this.accessAdminCases = accessAdminCases;
	}

	public boolean isAccessAdminPropertyValues() {
		return accessAdminPropertyValues;
	}

	public void setAccessAdminPropertyValues(boolean accessAdminPropertyValues) {
		this.accessAdminPropertyValues = accessAdminPropertyValues;
	}

	public boolean isAccessAdminActivityInstances() {
		return accessAdminActivityInstances;
	}

	public void setAccessAdminActivityInstances(boolean accessAdminActivityInstances) {
		this.accessAdminActivityInstances = accessAdminActivityInstances;
	}

	public boolean isAccessAdminNotificationInstances() {
		return accessAdminNotificationInstances;
	}

	public void setAccessAdminNotificationInstances(boolean accessAdminNotificationInstances) {
		this.accessAdminNotificationInstances = accessAdminNotificationInstances;
	}

	public boolean isAccessBasicModule() {
		return accessBasicModule;
	}

	public void setAccessBasicModule(boolean accessBasicModule) {
		this.accessBasicModule = accessBasicModule;
	}

	public boolean isAccessLastActivityInstances() {
		return accessLastActivityInstances;
	}

	public void setAccessLastActivityInstances(boolean accessLastActivityInstances) {
		this.accessLastActivityInstances = accessLastActivityInstances;
	}

	public boolean isAccessAdminJavaReports() {
		return accessAdminJavaReports;
	}
	
	public void setAccessAdminJavaReports(boolean accessAdminJavaReports) {
		this.accessAdminJavaReports = accessAdminJavaReports;
	}
	
	public UserGroup getParentUserGroup() {
		return parentUserGroup;
	}

	public void setParentUserGroup(UserGroup parentUserGroup) {
		this.parentUserGroup = parentUserGroup;
	}

	public Set<Form> getAccessSearchForm() {
		return accessSearchForm;
	}
	
	public void setAccessSearchForm(Set<Form> accessSearchForm) {
		this.accessSearchForm = accessSearchForm;
	}
	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<UserGroup> getCanAssignToUserGroups() {
		return canAssignToUserGroups;
	}

	public void setCanAssignToUserGroups(Set<UserGroup> canAssignToUserGroups) {
		this.canAssignToUserGroups = canAssignToUserGroups;
	}

	public Set<User> getCanAssignToUsers() {
		return canAssignToUsers;
	}

	public void setCanAssignToUsers(Set<User> canAssignToUsers) {
		this.canAssignToUsers = canAssignToUsers;
	}

	public Set<JavaReport> getJavaReports() {
		return javaReports;
	}

	public void setJavaReports(Set<JavaReport> javaReports) {
		this.javaReports = javaReports;
	}

}
