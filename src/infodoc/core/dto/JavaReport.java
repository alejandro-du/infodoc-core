package infodoc.core.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="java_report", uniqueConstraints={@UniqueConstraint(columnNames={"name", "user_group_id", "form_id"})})
@CrudTable(filteringPropertyName="name")
public class JavaReport extends Dto {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="java_class", nullable=false)
	private String javaClass;
	
	@Column(name="icon", nullable=false)
	private String icon;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;
	
	@ManyToOne
	@JoinColumn(name="user_group_id")
	private UserGroup userGroup;
	
	@ManyToOne
	@JoinColumn(name="form_id")
	private Form form;
	
	@Override
	public String toString() {
		return userGroup == null ? name : userGroup.getName() + "-" + name;
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
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}
	
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
	
}
