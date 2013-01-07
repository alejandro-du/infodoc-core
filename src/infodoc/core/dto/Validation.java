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
@Table(name="validation", uniqueConstraints={@UniqueConstraint(columnNames={"name", "user_group_id"})})
@CrudTable(filteringPropertyName="name")
public class Validation extends Dto implements Serializable {
	
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
	
	@Column(name="error_message", nullable=false)
	private String errorMessage;
	
	@ManyToMany
	@JoinTable(
		name="property_has_validation"
		, joinColumns={
			@JoinColumn(name="validation_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="property_id")
		}
	)
	private Set<Property> properties;
	
	@Override
	public String toString() {
		return name;
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

	public void setName(String nombre) {
		this.name = nombre;
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
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

}
