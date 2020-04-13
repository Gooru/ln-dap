package org.gooru.dap.deps.competency.processors;

import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityDataAssessmentScoreEventProcessor implements EventProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScoreEvent;

  public ActivityDataAssessmentScoreEventProcessor(
      AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  @Override
  public void process() {
    try {
      String eventName = this.assessmentScoreEvent.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "activity.learners.assessment.score":
          processActivityAssessmentScore();
          break;

        default:
          LOGGER.warn("invalid event passed in");
          return;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return;
    }
  }

  private void processActivityAssessmentScore() {
    LOGGER.debug("activity assessment score event processing start");

    new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent,
        assessmentScoreEvent.getContext().getGutCompCode(), false).process();
    new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent,
        assessmentScoreEvent.getContext().getGutCompCode(), false).process();

    new ContentCompetencyStatusProcessor(assessmentScoreEvent,
        assessmentScoreEvent.getContext().getGutCompCode()).process();
    new ContentCompetencyEvidenceProcessor(assessmentScoreEvent,
        assessmentScoreEvent.getContext().getGutCompCode(),
        assessmentScoreEvent.getContext().getGutCompCode()).process();
    
  }
}
