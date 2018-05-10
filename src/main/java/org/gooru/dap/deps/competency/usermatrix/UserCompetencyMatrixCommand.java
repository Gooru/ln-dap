package org.gooru.dap.deps.competency.usermatrix;

/**
 * @author gooru on 04-May-2018
 */
public class UserCompetencyMatrixCommand {

	private String txSubjectCode;
	private String userId;
	private String gutCode;
	private long activityTime;

	public UserCompetencyMatrixCommand(String txSubjectCode, String userId, String gutCode, long activityTime) {
		this.txSubjectCode = txSubjectCode;
		this.userId = userId;
		this.gutCode = gutCode;
		this.activityTime = activityTime;
	}

	public String getTxSubjectCode() {
		return txSubjectCode;
	}

	public String getUserId() {
		return userId;
	}

	public String getGutCode() {
		return gutCode;
	}

	public long getActivityTime() {
		return activityTime;
	}

}
