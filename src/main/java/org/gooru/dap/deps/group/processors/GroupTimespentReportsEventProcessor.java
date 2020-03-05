
package org.gooru.dap.deps.group.processors;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.deps.competency.processors.EventProcessor;
import org.gooru.dap.deps.group.dbhelpers.GroupPerfTSReortsQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 23-Apr-2019
 */
public class GroupTimespentReportsEventProcessor implements EventProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupTimespentReportsEventProcessor.class);
  private final GroupReportsEventMapper eventMapper;

  private final GroupPerfTSReortsQueueService queueService =
      new GroupPerfTSReortsQueueService(DBICreator.getDbiForDefaultDS());

  public GroupTimespentReportsEventProcessor(GroupReportsEventMapper eventMapper) {
    this.eventMapper = eventMapper;
  }

  @Override
  public void process() {
    try {
      String eventName = this.eventMapper.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "usage.collection.start":
          processCollectionStart();
          break;

        default:
          LOGGER.warn("invalid event passed in");
          return;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing group timespent report event");
      return;
    }
  }

  private void processCollectionStart() {
    String collectionType = this.eventMapper.getCollectionType();
    ContextObject context = this.eventMapper.getContext();

    // Check if the type of the collection is of valid type, class is not
    // null and valid content
    // source. If all of these conditions satisfies then only the record
    // will be inserted into
    // queue.
    if (collectionType != null
        && EventMessageConstant.VALID_COLLECTION_TYPES.contains(collectionType)) {
      if (context.getClassId() != null
          && EventMessageConstant.VALID_CONTENT_SOURCE_FOR_GROUP_REPORTS
              .contains(context.getContentSource())) {
        this.queueService.insertIntoTimespentQueue(context);
      }
    } else {
      LOGGER
          .warn("collection type is not present or not valid type, skipping inserting into queue");
    }
  }
}
