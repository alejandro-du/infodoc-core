package infodoc.core.dto;

import java.io.Serializable;
import java.util.Comparator;
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
@Table(name="property", uniqueConstraints={@UniqueConstraint(columnNames={"name", "user_group_id"})})
@CrudTable(filteringPropertyName="name")
public class Property extends Dto implements Serializable, Comparator<Property> {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="user_group_id")
	private UserGroup userGroup;
	
	@Column(name="java_class", nullable=false)
	private String javaClass;
	
	@Column(name="parameter")
	private String parameter;
	
	@Column(name="position")
	private Integer position;
	
	@Column(name="icon")
	private String icon;
	
	@Column(name="color")
	private String color;
	
	@Column(name="bold", nullable=false)
	boolean bold;
	
	@Column(name="italic", nullable=false)
	boolean italic;
	
	@Column(name="required", nullable=false)
	private boolean required;
	
	@Column(name="external", nullable=false)
	private boolean external;
	
	@Column(name="show_in_title", nullable=false)
	private boolean showInTitle;
	
	@Column(name="show_in_reports", nullable=false)
	private boolean showInReports;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;
	
	@ManyToMany
	@JoinTable(
		name="property_has_validation"
		, joinColumns={
			@JoinColumn(name="property_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="validation_id")
		}
	)
	private Set<Validation> validations;

    @ManyToMany
	@JoinTable(
		name="user_group_has_property"
		, joinColumns={
			@JoinColumn(name="property_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="user_group_id")
		}
	)
	private Set<UserGroup> userGroups;
    
    @ManyToMany
	@JoinTable(
		name="process_has_property"
		, joinColumns={
			@JoinColumn(name="property_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="process_id")
		}
	)
	private Set<Process> processes;
    
    @ManyToMany
	@JoinTable(
		name="activity_has_property"
		, joinColumns={
			@JoinColumn(name="property_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="activity_id")
		}
	)
	private Set<Activity> activities;
    
	@Override
	public int compare(Property property1, Property property2) {
		if(property1.getPosition() == null) {
			return Integer.MAX_VALUE;
		}
		
		if(property2.getPosition() == null) {
			return Integer.MIN_VALUE;
		}
		
		int i = property1.getPosition() - property2.getPosition();
		
		if(i == 0) {
			i = (int) (property1.getId() - property2.getId());
		}
		
		return i;
	}

	@Override
	public String toString() {
		return id + "-" + name;
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

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
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

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean getBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean getItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean getExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public boolean getShowInTitle() {
		return showInTitle;
	}
	
	public void setShowInTitle(boolean showInTitle) {
		this.showInTitle = showInTitle;
	}
	
	public boolean getShowInReports() {
		return showInReports;
	}

	public void setShowInReports(boolean howInReports) {
		this.showInReports = howInReports;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Set<Validation> getValidations() {
		return validations;
	}

	public void setValidations(Set<Validation> validations) {
		this.validations = validations;
	}

	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public Set<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(Set<Process> processes) {
		this.processes = processes;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

}
