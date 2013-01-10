package infodoc.core.dto;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import enterpriseapp.hibernate.annotation.CrudField;
import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.annotation.Downloadable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="property_value", uniqueConstraints={@UniqueConstraint(columnNames={"case_id", "property_id"})})
@CrudTable(filteringPropertyName="id")
public class PropertyValue extends Dto implements Serializable, Comparator<PropertyValue>, Comparable<PropertyValue> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="property_id", nullable=false)
	private Property property;
	
	@ManyToOne
	@JoinColumn(name="case_id", nullable=false)
	private Case caseDto;

	@Column(name="long_value")
	private Long longValue;
	
	@Column(name="string_value")
	@Type(type="text")
	private String stringValue;
	
	@Column(name="boolean_value")
	private Boolean booleanValue;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="property_value_has_classification_value"
		, joinColumns={
			@JoinColumn(name="property_value_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="classification_value_id")
		}
	)
	private Set<ClassificationValue> classificationsValueValue;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="property_value_has_case"
		, joinColumns={
			@JoinColumn(name="property_value_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="case_id")
		}
	)
	private Set<Case> caseDtosValue;
	
	@Column(name="date_value")
	@Temporal(TemporalType.DATE)
	private Date dateValue;
	
	@Column(name="byte_array_value")
	@CrudField(showInTable=false)
	@Lob
	@Downloadable(propertyFileName="stringValue")
	@Basic(fetch=FetchType.LAZY)
	private byte[] byteArrayValue;
	
	@Override
	public int compare(PropertyValue p1, PropertyValue p2) {
		return p1.getProperty().compare(p1.getProperty(), p2.getProperty());
	}

	@Override
	public int compareTo(PropertyValue p) {
		return this.getProperty().compare(this.getProperty(), p.getProperty());
	}

	@Override
	public String toString() {
		return "" + id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = (Long) id;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Case getCaseDto() {
		return caseDto;
	}

	public void setCaseDto(Case caseDto) {
		this.caseDto = caseDto;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}
	
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Set<ClassificationValue> getClassificationsValueValue() {
		return classificationsValueValue;
	}
	
	public void setClassificationsValueValue(Set<ClassificationValue> classificationsValueValue) {
		this.classificationsValueValue = classificationsValueValue;
	}
	
	public Set<Case> getCaseDtosValue() {
		return caseDtosValue;
	}
	
	public void setCaseDtosValue(Set<Case> caseDtosValue) {
		this.caseDtosValue = caseDtosValue;
	}
	
	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public byte[] getByteArrayValue() {
		return byteArrayValue;
	}

	public void setByteArrayValue(byte[] byteArrayValue) {
		this.byteArrayValue = byteArrayValue;
	}

}
