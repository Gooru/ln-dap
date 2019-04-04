
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public class GroupReortsAggregationQueueService {

  private final GroupReortsAggregationQueueDao queueDao;

  public GroupReortsAggregationQueueService(DBI dbi) {
    this.queueDao = dbi.onDemand(GroupReortsAggregationQueueDao.class);
  }
  
  public void insertIntoQueue(String classId, String tenant) {
    this.queueDao.insertIntoQueue(classId, tenant);
  }

  public List<GroupReortsAggregationQueueModel> fetchClassesForProcessing() {
    return this.queueDao.fetchClassesForProcessing();
  }

  public void removeFromQueue(String classId, String tenant) {
    this.queueDao.removeFromQueue(classId, tenant);
    
  }
}
