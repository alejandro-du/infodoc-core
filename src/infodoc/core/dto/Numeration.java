package infodoc.core.dto;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="numeration", uniqueConstraints={@UniqueConstraint(columnNames={"name", "user_group_id"})})
@CrudTable(filteringPropertyName="name")
public class Numeration extends Dto implements Serializable {
	
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
	
	@Column(name="prefix")
	private String prefix;
	
	@Column(name="next_value", nullable=false)
	private Long nextValue;
	
	@Column(name="initial_value", nullable=false)
	private Long initialValue;
	
	@Column(name="annual_restart")
	private boolean annualRestart;
	
	@Column(name="monthly_restart")
	private boolean monthlyRestart;
	
	@Column(name="daily_restart")
	private boolean dailyRestart;
	
	@Column(name="next_restart_date")
	private Date nextRestartDate;
	
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Long getNextValue() {
		return nextValue;
	}

	public void setNextValue(Long nextValue) {
		this.nextValue = nextValue;
	}

	public Long getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Long valorInicial) {
		this.initialValue = valorInicial;
	}

	public boolean isAnnualRestart() {
		return annualRestart;
	}

	public void setAnnualRestart(boolean annualRestart) {
		this.annualRestart = annualRestart;
	}

	public boolean isMonthlyRestart() {
		return monthlyRestart;
	}

	public void setMonthlyRestart(boolean monthlyRestart) {
		this.monthlyRestart = monthlyRestart;
	}

	public boolean isDailyRestart() {
		return dailyRestart;
	}

	public void setDailyRestart(boolean dailyRestart) {
		this.dailyRestart = dailyRestart;
	}

	public Date getNextRestartDate() {
		return nextRestartDate;
	}

	public void setNextRestartDate(Date nextRestartDate) {
		this.nextRestartDate = nextRestartDate;
	}

}
