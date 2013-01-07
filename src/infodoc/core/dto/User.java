package infodoc.core.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="user")
@CrudTable(filteringPropertyName="login")
public class User extends Dto implements enterpriseapp.hibernate.dto.User, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@CrudField(isEmail=true)
	@Column(name="login", nullable = false, unique=true, length=255)
	private String login;
	
	@CrudField(showInTable=true, isPassword=true)
	@Column(name="password", nullable = false, length=255)
	private String password;
	
	@Column(name="expire_password", nullable=false)
	private boolean expirePassword = true;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;
	
	@Column(name="name", nullable=false, length=255)
	private String name;
	
	@Column(name="last_name", nullable=false, length=255)
	private String lastName;
	
	@ManyToOne
	@JoinColumn(name="user_group_id", nullable=false)
	private UserGroup userGroup;
	
	@Column(name="dashboard_url")
	private String dashboardUrl;
	
	@Column(name="time_zone")
	private String timeZone;
	
	@Override
	public String toString() {
		return login;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;
	}
   
	public void setId(Long id) {
		this.id = id;
	}
   
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getExpirePassword() {
		return expirePassword;
	}

	public void setExpirePassword(boolean expirePassword) {
		this.expirePassword = expirePassword;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public String getDashboardUrl() {
		return dashboardUrl;
	}

	public void setDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
