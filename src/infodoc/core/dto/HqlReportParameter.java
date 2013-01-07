package infodoc.core.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="hql_parameter")
@CrudTable(filteringPropertyName="label")
public class HqlReportParameter extends Dto {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="label")
	private String label;
	
	@Column(name="placeholder", nullable = false)
	private String placeholder;
	
	@Column(name="java_class", nullable = false)
	private String javaClass;
	
	@Column(name="position")
	private Integer position;
	
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

	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
