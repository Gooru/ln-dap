package org.gooru.dap.deps.competency.assessmentscore.atc.preprocessor;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcProcessor;
import org.gooru.dap.deps.competency.assessmentscore.atc.postprocessor.AtcPostProcessor;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
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

      // If courseId obtained from the event is NULL, fallback to the courseId
      // stored in the class table
      if (courseId == null) {
        courseId = service.fetchCourseFromClass(classId);
      }

      // Check if Class == Premium. If class != Premium - Don't go any further.
      if (classId != null && courseId != null & userId != null) {
        if (service.CheckifClassPremium(classId)) {
          LOGGER.info("Class " + classId + " is Premium, Continue ..");
          new AtcProcessor(assessmentScoreEvent).compute();
          new AtcPostProcessor(assessmentScoreEvent).compute();
        } else {
          LOGGER.info("Class " + classId + " is NOT Premium. No further processing");
          return;
        }
      }

    } catch (Throwable t) {
      LOGGER.error("Exception while processing event", t);
      return;
    }

  }

}
