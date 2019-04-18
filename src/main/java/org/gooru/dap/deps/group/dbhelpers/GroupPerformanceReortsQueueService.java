
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.gooru.dap.deps.group.processors.ContextObject;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public class GroupPerformanceReortsQueueService {

  private final GroupPerformanceReortsQueueDao queueDao;

  public GroupPerformanceReortsQueueService(DBI dbi) {
    this.queueDao = dbi.onDemand(GroupPerformanceReortsQueueDao.class);
  }
  
  public void insertIntoQueue(ContextObject context) {
    this.queueDao.insertIntoQueue(context);
  }

  public List<GroupReortsAggregationQueueModel> fetchClassesForProcessing(Integer limit, Integer offset) {
    return this.queueDao.fetchClassesForProcessing(limit, offset);
  }
  
  public void updateQueueStatusToCompleted(String classId, String contentSource) {
    this.queueDao.updateQueueStatusToCompleted(classId, contentSource);
  }

  public void deleteCompletedFromQueue() {
    this.queueDao.deleteCompletedFromQueue();
  }
}
