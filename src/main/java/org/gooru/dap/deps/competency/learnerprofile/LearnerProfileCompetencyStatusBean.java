package org.gooru.dap.deps.competency.learnerprofile;

import java.sql.Timestamp;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusBean {

	private String txSubjectCode;
	private String userId;
	private String gutCode;
	private int status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public LearnerProfileCompetencyStatusBean() {
	}

	public LearnerProfileCompetencyStatusBean(LearnerProfileCompetencyStatusCommand command) {
		this.txSubjectCode = command.getTxSubjectCode();
		this.userId = command.getUserId();
		this.gutCode = command.getGutCode();
		Timestamp ts = new Timestamp(command.getActivityTime());
		this.createdAt = ts;
		this.updatedAt = ts;
	}

	public String getTxSubjectCode() {
		return txSubjectCode;
	}

	public void setTxSubjectCode(String txSubjectCode) {
		this.txSubjectCode = txSubjectCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGutCode() {
		return gutCode;
	}

	public void setGutCode(String gutCode) {
		this.gutCode = gutCode;
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
