package org.gooru.dap.deps.competency.status;

import java.util.regex.Pattern;

import org.gooru.dap.deps.competency.AssessmentScoreConsumer;
import org.gooru.dap.deps.competency.score.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 10-May-2018
 */
public final class CompetencyStatusCommandBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentScoreConsumer.Constants.LOGGER_NAME);
	private static final Pattern PERIOD_PATTERN = Pattern.compile("\\.");

	private CompetencyStatusCommandBuilder() {
		throw new AssertionError();
	}

	public static CompetencyStatusCommand build(AssessmentScoreEventMapper assessmentScore, String competencyCode) {
		LOGGER.debug("processing competency status for {}", competencyCode);
		String frameworkCode = PERIOD_PATTERN.split(competencyCode)[0];
		CompetencyStatusCommand command = new CompetencyStatusCommand(assessmentScore.getUserId(), competencyCode,
				frameworkCode, assessmentScore.getActivityTime());
		return command;
	}
}
