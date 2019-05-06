
package org.gooru.dap.deps.group.processors;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.processors.EventProcessor;
import org.gooru.dap.deps.group.dbhelpers.GroupCompetencyReportsQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class GroupCompetencyReportsEventProcessor implements EventProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportsEventProcessor.class);
  private final GroupReportsEventMapper eventMapper;
  private final GroupCompetencyReportsQueueService queueService =
      new GroupCompetencyReportsQueueService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportsEventProcessor(GroupReportsEventMapper eventMapper) {
    this.eventMapper = eventMapper;
  }

  @Override
  public void process() {
    try {
      String eventName = this.eventMapper.getEventName();
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
    ContextObject context = this.eventMapper.getContext();
    if (context.getClassId() != null) {
      this.queueService.insertIntoQueue(context.getClassId(), context.getTenantId());
    }
  }

}
