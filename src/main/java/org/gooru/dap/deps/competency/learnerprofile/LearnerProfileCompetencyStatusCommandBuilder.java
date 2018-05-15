package org.gooru.dap.deps.competency.learnerprofile;

import java.util.regex.Pattern;

import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;

/**
 * @author gooru on 14-May-2018
 */
public final class LearnerProfileCompetencyStatusCommandBuilder {

	private static final Pattern HYPEN_PATTERN = Pattern.compile("-");

	private LearnerProfileCompetencyStatusCommandBuilder() {
		throw new AssertionError();
	}

	/*public static LearnerProfileCompetencyStatusCommand build(String userId, String gutCode, long activityTime) {
		String subjectCode = HYPEN_PATTERN.split(gutCode)[0];
		LearnerProfileCompetencyStatusCommand command = new LearnerProfileCompetencyStatusCommand(subjectCode, userId,
				gutCode, activityTime);
		return command;
	}*/

	public static LearnerProfileCompetencyStatusCommand build(AssessmentScoreEventMapper assessmentScore, String gutCode) {
		String subjectCode = HYPEN_PATTERN.split(gutCode)[0];
		LearnerProfileCompetencyStatusCommand command = new LearnerProfileCompetencyStatusCommand(subjectCode, assessmentScore.getUserId(),
				gutCode, assessmentScore.getActivityTime());
		return command;
	}
}
