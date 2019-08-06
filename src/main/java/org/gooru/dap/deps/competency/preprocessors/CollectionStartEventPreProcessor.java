package org.gooru.dap.deps.competency.preprocessors;

import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author mukul@gooru
 */
public class CollectionStartEventPreProcessor implements EventPreProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(CollectionStartEventPreProcessor.class);
  private AssessmentScoreEventMapper event;

  public CollectionStartEventPreProcessor(AssessmentScoreEventMapper event) {
    this.event = event;
  }

  @Override
  public DCAContentModel process() {
    try {
      String eventName = this.event.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "usage.assessment.score":
          return processCollectionStartEvent();
        default:
          LOGGER.warn("This processor cannot parse this event");
          return null;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return null;
    }
  }

  private DCAContentModel processCollectionStartEvent() {
    return new AdditionalContextProcessor(event).process();
  }
}
