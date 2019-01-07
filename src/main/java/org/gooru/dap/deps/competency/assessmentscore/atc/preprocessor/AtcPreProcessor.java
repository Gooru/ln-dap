package org.gooru.dap.deps.competency.assessmentscore.atc.preprocessor;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcProcessor;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.processors.AssessmentScoreEventProcessor;
import org.gooru.dap.deps.competency.processors.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class AtcPreProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
  private String classId;
  private String courseId;
  private String userId;

  private final AssessmentScoreEventMapper assessmentScoreEvent;
  private AtcPreProcessorService service = new AtcPreProcessorService(DBICreator.getDbiForCoreDS());

  public AtcPreProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  public void process() {
    processAssessmentScore();
  }

  private void processAssessmentScore() {
    try {
      classId = this.assessmentScoreEvent.getContext().getClassId();
      courseId = this.assessmentScoreEvent.getContext().getCourseId();
      userId = this.assessmentScoreEvent.getUserId();

      // Check if Class == Premium. If class != Premium - Don't go any further.
      // To reduce Processing, we might also put a check of score > 80.
      if (classId != null && courseId != null & userId != null) {
        new AtcProcessor(assessmentScoreEvent).compute();
      }

    } catch (Throwable t) {
      LOGGER.error("Exception while processing event", t);
      return;
    }

  }

}
