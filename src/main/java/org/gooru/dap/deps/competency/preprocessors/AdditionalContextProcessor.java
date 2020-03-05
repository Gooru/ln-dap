package org.gooru.dap.deps.competency.preprocessors;

import java.io.IOException;
import java.util.Base64;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mukul@gooru
 */
public class AdditionalContextProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(AdditionalContextProcessor.class);
  private final static String DCA = "dailyclassactivity";
  private AssessmentScoreEventMapper event;
  private String additionalContext = null;
  private String additionalContextEncoded;
  private AdditionalContextObj additionalContextObj = null;
  private DCAContentModel dcaContentModel;

  public AdditionalContextProcessor(AssessmentScoreEventMapper event) {
    this.event = event;
  }

  public DCAContentModel process() {
    try {
      String eventName = this.event.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      return processAssessmentScoreEvent();
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return null;
    }
  }

  private DCAContentModel processAssessmentScoreEvent() {
    try {
      LOGGER.debug("Get encoded Additional Context from the event");
      getAdditionalContext();
      LOGGER.debug("Decode Additional Context");
      decodeAdditionalContext();
      LOGGER.debug("Parse Additional Context Json");
      parseAdditionalContext();
      LOGGER.debug("Fetch DCA Assessment/Collection info");
      fetchDCAContentInfo();
      LOGGER.debug("Validate DCA Assessment/Collection info obtained");
      if (validateDCAContentInfo()) {
        return dcaContentModel;
      } else {
        return null;
      }
    } catch (IllegalStateException ex) {
      LOGGER.warn("Caught IllegalStateException ");
      return null;
    }
  }

  private void getAdditionalContext() {
    additionalContextEncoded = this.event.getContext().getAdditionalContext();
    LOGGER.info("Encoded Additional Context is {}", additionalContextEncoded);
  }

  private void decodeAdditionalContext() {
    try {
      additionalContext = new String(Base64.getDecoder().decode(additionalContextEncoded));
      LOGGER.info("Decoded Additional Context is {}", additionalContext);
    } catch (IllegalArgumentException e) {
      LOGGER.error("Unable to decode Additional Context ", e);
    }
  }

  private void parseAdditionalContext() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      if (additionalContext != null) {
        additionalContextObj = mapper.readValue(additionalContext, AdditionalContextObj.class);
        LOGGER.info("Additional Context Object {}", additionalContext.toString());
      }
    } catch (IOException e) {
      LOGGER.error("Unable to parse the additionalContext Json", e);
    }
  }

  private void fetchDCAContentInfo() {
    if (additionalContextObj != null) {
      DCAContentService service = new DCAContentService(DBICreator.getDbiForCoreDS());
      dcaContentModel = service.getDCAContentId(additionalContextObj.getDcaContentId());
    }
  }

  private boolean validateDCAContentInfo() {
    if ((dcaContentModel == null)
        || (dcaContentModel != null && (!dcaContentModel.getAllowMasteryAccrual()
            || !dcaContentModel.getContentType().equalsIgnoreCase(event.getCollectionType())
            || !event.getContext().getContentSource().equalsIgnoreCase(DCA)))) {
      LOGGER.error("DCA Content Validation unsuccessful");
      return false;
    } else {
      return true;
    }
  }

}
