package infodoc.core.dto;

import infodoc.core.InfodocConstants;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.Type;

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="form", uniqueConstraints={@UniqueConstraint(columnNames={"name", "user_group_id"})})
@CrudTable(filteringPropertyName="name")
public class Form extends Dto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_group_id")
	private UserGroup userGroup;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="disabled", nullable=false)
	private boolean disabled;
	
	@ManyToOne
	@JoinColumn(name="numeration_id")
	private Numeration numeration;
	
	@OneToMany(mappedBy="form")
	private Set<Activity> activities;
	
    @ManyToMany
    @Sort(type=SortType.COMPARATOR, comparator=Property.class)
	@JoinTable(
		name="form_has_property"
		, joinColumns={
			@JoinColumn(name="form_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="property_id")
		}
	)
	private Set<Property> properties;
    
    @Column(name="hide_activity_history", nullable=false)
    private boolean hideActivityHistory;
    
	@Type(type="text")
	@Column(name="print_template")
    private String printTemplate;
    
	@Column(name="widht")
    private Integer width;
    
	@Column(name="height")
    private Integer height;
    
	@Column(name="horizontal_margin")
    private Integer horizontalMargin;
	
	@Column(name="vertical_margin")
    private Integer verticalMargin;
    
	@Column(name="icon", nullable=false)
    private String icon;
	
	@Column(name="position")
	private Integer position;
	
	@Override
	public String toString() {
		return userGroup == null ? name : userGroup.getName() + "-" + name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = (Long) id;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Numeration getNumeration() {
		return numeration;
	}
	
	public void setNumeration(Numeration numeration) {
		this.numeration = numeration;
	}
	
	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

	public boolean isHideActivityHistory() {
		return hideActivityHistory;
	}

	public void setHideActivityHistory(boolean hideActivityHistory) {
		this.hideActivityHistory = hideActivityHistory;
	}

	public String getPrintTemplate() {
		if(printTemplate == null) {
			printTemplate = InfodocConstants.infodocDefaultPrintTemplate;
		}
		
		return printTemplate;
	}

	public void setPrintTemplate(String formatoImpresion) {
		this.printTemplate = formatoImpresion;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer altura) {
		this.height = altura;
	}

	public Integer getHorizontalMargin() {
		return horizontalMargin;
	}

	public void setHorizontalMargin(Integer horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}

	public Integer getVerticalMargin() {
		return verticalMargin;
	}

	public void setVerticalMargin(Integer verticalMargin) {
		this.verticalMargin = verticalMargin;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
