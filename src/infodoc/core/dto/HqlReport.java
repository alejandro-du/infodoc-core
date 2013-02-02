package infodoc.core.dto;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="hql_report")
@CrudTable(filteringPropertyName="name")
public class HqlReport extends Dto {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name", nullable=false, unique=true)
	private String name;
	
	@Column(name="hql_query", nullable=false, length=2024)
	private String hqlQuery;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;
	
	@ManyToOne
	@JoinColumn(name="form_id")
	private Form form;
	
    @ManyToMany
	@JoinTable(
		name="user_group_has_hql_report"
		, joinColumns={
			@JoinColumn(name="hql_report_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="user_group_id")
		}
	)
	private Set<UserGroup> userGroups;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@CrudField(embedded=true)
    @JoinTable(
    	name = "hql_report_has_hql_parameter"
    	, joinColumns={
    		@JoinColumn(name = "hql_report_id")
    	}, inverseJoinColumns={
    		@JoinColumn(name = "hql_report_parameter_id")
    	}
    )
	@OrderBy("position")
	private Set<HqlReportParameter> hqlParameters;
	
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

	public void setName(String nombre) {
		this.name = nombre;
	}

	public String getHqlQuery() {
		return hqlQuery;
	}
	
	public void setHqlQuery(String hqlQuery) {
		this.hqlQuery = hqlQuery;
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
	
	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public Set<HqlReportParameter> getHqlParameters() {
		return hqlParameters;
	}

	public void setHqlParameters(Set<HqlReportParameter> hqlParameters) {
		this.hqlParameters = hqlParameters;
	}

}
