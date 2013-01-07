package infodoc.core.dto;

import infodoc.core.container.PropertyValueContainer;
import infodoc.core.container.InfodocContainerFactory;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="process_instance")
@CrudTable(filteringPropertyName="number")
public class ProcessInstance extends Dto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="number")
	private Long number;
	
	@ManyToOne
	@JoinColumn(name="process_id", nullable=false)
	private Process process;
	
	@OneToMany(mappedBy="processInstance", orphanRemoval=true)
    @Basic(fetch=FetchType.EAGER)
    @Sort(type=SortType.COMPARATOR, comparator=PropertyValue.class)
	private SortedSet<PropertyValue> propertyValues;
	
    @OneToMany(mappedBy="processInstance")
    @OrderBy(value="executionTime")
    private Set<ActivityInstance> activityInstances;
	
	@Override
	public String toString() {
		String string = "";
		
		if(number != null) {
			if(process.getNumeration() != null) {
				if(process.getNumeration().getPrefix() != null) {
					string += process.getNumeration().getPrefix();
				}
				
				string += number;
			}
		} else {
			string += id;
		}
		
		if(propertyValues != null) {
			boolean found = false;
			PropertyValueContainer propertyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
			SortedSet<PropertyValue> propertyValues = InfodocContainerFactory.getProcessInstanceContainer().getEntity(id).getPropertyValues();
			
			for(PropertyValue propertyValue : propertyValues) {
				if(propertyValue.getProperty().getShowInTitle()) {
					String value = propertyValueContainer.getStringValue(propertyValue);
					
					if(value != null && !value.isEmpty()) {
						if(!found) {
							string += " ";
							found = true;
						} else {
							string += ", ";
						}
						
						string += value;
					}
				}
			}
			
			if(found) {
				if(string.endsWith(", ")) {
					string = string.substring(0, string.length());
				}
			}
		}
		
		return string;
	}

	public Long getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = (Long) id;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long numero) {
		this.number = numero;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public SortedSet<PropertyValue> getPropertyValues() {
		return propertyValues;
	}
	
	public void setPropertyValues(SortedSet<PropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
	}
	
	public Set<ActivityInstance> getActivityInstances() {
		return activityInstances;
	}

	public void setActivityInstances(Set<ActivityInstance> activityInstances) {
		this.activityInstances = activityInstances;
	}

}
