package org.gooru.dap.deps.competency.status;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.AssessmentScoreConsumer;
import org.gooru.dap.deps.competency.score.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.score.mapper.ResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyStatusProcessor {

	// TODO: Move this to the config
	private final int COMPLETION_SCORE = 80;

	private final AssessmentScoreEventMapper assessmentScoreEvent;
	private final String competencyCode;

	private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentScoreConsumer.Constants.LOGGER_NAME);
	
	CompetencyStatusService service = new CompetencyStatusService(DBICreator.getDbiForDefaultDS());

	public CompetencyStatusProcessor(AssessmentScoreEventMapper assessmentScoreEvent, String competencyCode) {
		this.assessmentScoreEvent = assessmentScoreEvent;
		this.competencyCode = competencyCode;
	}

	public void process() {
		CompetencyStatusCommand command = CompetencyStatusCommandBuilder.build(assessmentScoreEvent,
				this.competencyCode);
		CompetencyStatusBean bean = new CompetencyStatusBean(command);

		ResultMapper result = this.assessmentScoreEvent.getResult();
		if (result.getScore() >= COMPLETION_SCORE) {
			service.updateCompetencyStatusToCompleted(bean);
			return;
		}

		service.updateCompetencyStatusToInprogress(bean);
		LOGGER.debug("competency status has been persisted successfully.");
	}
}
