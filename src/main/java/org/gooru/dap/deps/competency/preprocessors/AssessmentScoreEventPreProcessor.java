package org.gooru.dap.deps.competency.preprocessors;

import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author mukul@gooru
 */
public class AssessmentScoreEventPreProcessor implements EventPreProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(AssessmentScoreEventPreProcessor.class);
  private final static String DCA = "dailyclassactivity";
  private AssessmentScoreEventMapper assessmentScoreEvent;

  public AssessmentScoreEventPreProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  @Override
  public DCAContentModel process() {
    try {
      String eventName = this.assessmentScoreEvent.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "usage.assessment.score":
          return processAssessmentScoreEvent();
        default:
          LOGGER.warn("This processor cannot parse this event");
          return null;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return null;
    }
  }

  private DCAContentModel processAssessmentScoreEvent() {
    return new AdditionalContextProcessor(assessmentScoreEvent).process();
  }

}
