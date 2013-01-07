package infodoc.core.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="audit_log")
@CrudTable(filteringPropertyName="id")
public class AuditLog extends Dto implements enterpriseapp.hibernate.dto.AuditLog {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;

	@Column(name="entity_type", nullable=false, length=255)
	private String entityType;
	
	@Column(name="date", nullable=false)
	private Date date;
	
	@Column(name="action", nullable=false, length=255)
	private String action;
	
	@Column(name="dto_id")
	private String dtoId;
	
	@Column(name="details", nullable=false, length=1023)
	private String details;
	
	@Column(name="user", nullable=false, length=255)
	private String user;

	@Column(name="ip", nullable=false, length=255)
	private String ip;

	@Override
	public String toString() {
		return "" + id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDtoId() {
		return dtoId;
	}

	public void setDtoId(String dtoId) {
		this.dtoId = dtoId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
