package org.gooru.dap.deps.competency.status;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyStatusCommand {

	private String userId;
	private String competencyCode;
	private String frameworkCode;
	private long activityTime;

	public CompetencyStatusCommand(String userId, String competencyCode, String frameworkCode, long activityTime) {
		this.userId = userId;
		this.competencyCode = competencyCode;
		this.frameworkCode = frameworkCode;
		this.activityTime = activityTime;
	}

	public String getUserId() {
		return userId;
	}

	public String getCompetencyCode() {
		return competencyCode;
	}

	public String getFrameworkCode() {
		return frameworkCode;
	}

	public long getActivityTime() {
		return activityTime;
	}

}
