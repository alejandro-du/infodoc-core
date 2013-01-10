package infodoc.core.dto;

import java.io.Serializable;
import java.util.Date;
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

import enterpriseapp.hibernate.annotation.CrudTable;
import enterpriseapp.hibernate.dto.Dto;

@Entity
@Table(name="activity_instance")
@CrudTable(filteringPropertyName="id")
public class ActivityInstance extends Dto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="execution_time", nullable=false)
	private Date executionTime;
	
	@Column(name="comments")
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="activity_id", nullable=false)
	private Activity activity;
	
	@ManyToOne
	@JoinColumn(name="case_id", nullable=false)
	private Case caseDto;
	
	@ManyToMany
	@JoinTable(
		name="activity_instance_assigns_user_group"
		, joinColumns={
			@JoinColumn(name="activity_instance_id")
		}
		, inverseJoinColumns={
			@JoinColumn(name="user_group_id")
		}
	)
	private Set<UserGroup> assignedUserGroups;
	
	@ManyToMany
	@JoinTable(
			name="activity_instance_assigns_user"
			, joinColumns={
				@JoinColumn(name="activity_instance_id")
			}
			, inverseJoinColumns={
				@JoinColumn(name="user_id")
			}
		)
	private Set<User> assignedUsers;

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

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Case getCaseDto() {
		return caseDto;
	}

	public void setCaseDto(Case caseDto) {
		this.caseDto = caseDto;
	}

	public Set<UserGroup> getAssignedUserGroups() {
		return assignedUserGroups;
	}

	public void setAssignedUserGroups(Set<UserGroup> assignedUserGroups) {
		this.assignedUserGroups = assignedUserGroups;
	}

	public Set<User> getAssignedUsers() {
		return assignedUsers;
	}

	public void setAssignedUsers(Set<User> users) {
		this.assignedUsers = users;
	}

}
