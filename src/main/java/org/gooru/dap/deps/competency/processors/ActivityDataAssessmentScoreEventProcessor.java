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
    LOGGER.debug("activity assessment question score event processing start");
    // Expecting the activity assessment score event should have both the gutcode and compcode, here
    // not doing DB validation, Since such validation will be done in ASDP where the entry point of
    // data in.
    final String gutCode = assessmentScoreEvent.getContext().getGutCompCode();
    final String compCode = assessmentScoreEvent.getContext().getCompCode();
    if (gutCode != null && compCode != null) {
      new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gutCode, false).process();
      new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gutCode, false).process();

      new ContentCompetencyStatusProcessor(assessmentScoreEvent, compCode).process();
      new ContentCompetencyEvidenceProcessor(assessmentScoreEvent, compCode, gutCode).process();
    }
  }
}
