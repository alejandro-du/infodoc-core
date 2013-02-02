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
@Table(name="activity", uniqueConstraints={@UniqueConstraint(columnNames={"name", "form_id"})})
@CrudTable(filteringPropertyName="name")
public class Activity extends Dto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="java_class", nullable=false)
	private String javaClass;
	
	@Column(name="parameter")
	private String parameter;
	
	@Column(name="allow_comments", nullable=false)
	private boolean allowComments;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;
	
	@ManyToOne
	@JoinColumn(name="form_id", nullable=false)
	private Form form;
	
    @ManyToMany
	@JoinTable(
		name="activity_has_next_activity"
		, joinColumns={
			@JoinColumn(name="activity_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="next_activity_id")
		}
	)
	private Set<Activity> nextActivities;
	
	@ManyToMany
	@JoinTable(
		name="activity_has_property"
		, joinColumns={
			@JoinColumn(name="activity_id")
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
			@JoinColumn(name="activity_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="user_group_id")
		}
	)
	private Set<UserGroup> userGroups;
    
	@Override
	public String toString() {
		return form.getName() + "-" + name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = (Long) id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(boolean allowComments) {
		this.allowComments = allowComments;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

    public Set<Activity> getNextActivities() {
		return nextActivities;
	}

	public void setNextActivities(Set<Activity> nextActivities) {
		this.nextActivities = nextActivities;
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

}
