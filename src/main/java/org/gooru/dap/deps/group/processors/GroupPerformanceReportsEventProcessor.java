
package org.gooru.dap.deps.group.processors;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.deps.competency.processors.EventProcessor;
import org.gooru.dap.deps.group.dbhelpers.GroupPerfTSReortsQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 02-Apr-2019
 */
public class GroupPerformanceReportsEventProcessor implements EventProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerformanceReportsEventProcessor.class);
  private final GroupReportsEventMapper eventMapper;
  private final GroupPerfTSReortsQueueService queueService =
      new GroupPerfTSReortsQueueService(DBICreator.getDbiForDefaultDS());

  public GroupPerformanceReportsEventProcessor(GroupReportsEventMapper eventMapper) {
    this.eventMapper = eventMapper;
  }

  @Override
  public void process() {
    try {
      String eventName = this.eventMapper.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "activity.learners.assessment.score":
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
    // Fetch group hierarchy based on the class id from the event
    ContextObject context = this.eventMapper.getContext();
    if (context.getClassId() != null && EventMessageConstant.VALID_CONTENT_SOURCE_FOR_GROUP_REPORTS
        .contains(context.getContentSource())) {
      this.queueService.insertIntoPerfQueue(context);
    }
  }

}
