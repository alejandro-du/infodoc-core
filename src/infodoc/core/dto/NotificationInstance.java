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

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="notification_instance")
@CrudTable(filteringPropertyName="id")
public class NotificationInstance extends Dto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="activity_instance_id")
	private ActivityInstance activityInstance;
	
	@ManyToOne
	@JoinColumn(name="notification_id", nullable=false)
	private Notification notification;
	
	@Column(name="send_time", nullable=false)
	private Date sendTime;
	
	@Column(name="sent", nullable=false)
	private boolean sent;
	
	@Column(name="canceled", nullable=false)
	private boolean canceled;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Long) id;
	}

	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}

	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public boolean getSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public boolean getCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

}
