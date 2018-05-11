package org.gooru.dap.deps.competency.status;

import java.sql.Timestamp;

/**
 * @author gooru on 10-May-2018
 */
public class CompetencyStatusBean {

	private String userId;
	private String competencyCode;
	private String frameworkCode;
	private String status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public CompetencyStatusBean() {
	}

	public CompetencyStatusBean(CompetencyStatusCommand command) {
		this.userId = command.getUserId();
		this.competencyCode = command.getCompetencyCode();
		this.frameworkCode = command.getFrameworkCode();
		Timestamp ts = new Timestamp(command.getActivityTime());
		this.createdAt = ts;
		this.updatedAt = ts;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompetencyCode() {
		return competencyCode;
	}

	public void setCompetencyCode(String competencyCode) {
		this.competencyCode = competencyCode;
	}

	public String getFrameworkCode() {
		return frameworkCode;
	}

	public void setFrameworkCode(String frameworkCode) {
		this.frameworkCode = frameworkCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}
