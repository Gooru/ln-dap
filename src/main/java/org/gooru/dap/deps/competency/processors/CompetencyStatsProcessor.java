package org.gooru.dap.deps.competency.processors;

import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.atc.preprocessor.AtcPreProcessor;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mukul@gooru
 */
public class CompetencyStatsProcessor implements EventProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScoreEvent;

  public CompetencyStatsProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  @Override
  public void process() {
    try {
      String eventName = this.assessmentScoreEvent.getEventName();
      LOGGER.debug("Processing event Relayed by Assessment Score Consumer: {}", eventName);
      switch (eventName) {
        case "usage.assessment.score":
          new AtcPreProcessor(assessmentScoreEvent).process();
          break;
        default:
          LOGGER.warn("Invalid event passed in");
          return;
      }
    } catch (Throwable t) {
      LOGGER.error("Exception while processing event", t);
      return;
    }

  }

}
