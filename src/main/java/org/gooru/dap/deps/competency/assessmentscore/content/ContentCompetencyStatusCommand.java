package org.gooru.dap.deps.competency.assessmentscore.content;

import java.util.regex.Pattern;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyStatusCommand {

	private static final Pattern PERIOD_PATTERN = Pattern.compile("\\.");

	private String userId;
	private String competencyCode;
	private String frameworkCode;
	private long activityTime;

	public ContentCompetencyStatusCommand(String userId, String competencyCode, long activityTime) {
		this.userId = userId;
		this.competencyCode = competencyCode;
		this.frameworkCode = PERIOD_PATTERN.split(competencyCode)[0];
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
