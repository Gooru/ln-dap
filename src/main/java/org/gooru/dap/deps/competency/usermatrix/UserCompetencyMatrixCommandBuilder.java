package org.gooru.dap.deps.competency.usermatrix;

import java.util.regex.Pattern;

/**
 * @author gooru on 09-May-2018
 */
public final class UserCompetencyMatrixCommandBuilder {

	private static final Pattern HYPEN_PATTERN = Pattern.compile("-");
	
	private UserCompetencyMatrixCommandBuilder() {
		throw new AssertionError();
	}
	
	public static UserCompetencyMatrixCommand build(String userId, String gutCode, long activityTime) {
		
		String subjectCode = HYPEN_PATTERN.split(gutCode)[0];
		
		UserCompetencyMatrixCommand command = new UserCompetencyMatrixCommand(subjectCode, userId, gutCode, activityTime);
		return command;
	}
}

