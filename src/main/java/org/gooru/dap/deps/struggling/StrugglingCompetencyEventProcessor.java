
package org.gooru.dap.deps.struggling;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.common.CompetencyAssessmentService;
import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.processors.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 11-Oct-2019
 */
public class StrugglingCompetencyEventProcessor implements EventProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(StrugglingCompetencyEventProcessor.class);

  private final AssessmentScoreEventMapper assessmentScoreEvent;
  private CompetencyAssessmentService service =
      new CompetencyAssessmentService(DBICreator.getDbiForCoreDS());

  public StrugglingCompetencyEventProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  @Override
  public void process() {
    try {
      String eventName = this.assessmentScoreEvent.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "usage.assessment.score":
          processAssessmentScore();
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

  private void processAssessmentScore() {
    LOGGER.debug("Struggling competency processing start");
    String collectionId = this.assessmentScoreEvent.getCollectionId();

    // Fetch competencies tagged with assessment
    LOGGER.debug("fetching competency for assessment: '{}'", collectionId);
    AssessmentCompetency competency = service.getAssessmentCompetency(collectionId);

    new StrugglingCompetencyProcessor(assessmentScoreEvent, competency.getGutCodes()).process();
  }
}
