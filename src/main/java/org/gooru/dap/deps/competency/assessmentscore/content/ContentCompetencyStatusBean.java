package org.gooru.dap.deps.competency.assessmentscore.content;

import java.sql.Timestamp;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyStatusBean {

	private String userId;
	private String competencyCode;
	private String frameworkCode;
	private int status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public ContentCompetencyStatusBean() {
	}

	public ContentCompetencyStatusBean(ContentCompetencyStatusCommand command) {
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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
