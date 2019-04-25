
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.gooru.dap.deps.group.processors.ContextObject;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public class GroupPerfTSReortsQueueService {

  private final GroupPerfTSReortsQueueDao queueDao;

  public GroupPerfTSReortsQueueService(DBI dbi) {
    this.queueDao = dbi.onDemand(GroupPerfTSReortsQueueDao.class);
  }

  public void insertIntoPerfQueue(ContextObject context) {
    this.queueDao.insertIntoPerfQueue(context);
  }

  public List<GroupPerfTSReortsQueueModel> fetchClassesForPerfProcessing(Integer limit,
      Integer offset) {
    return this.queueDao.fetchClassesForPerfProcessing(limit, offset);
  }

  public void updatePerfQueueStatusToCompleted(String classId, String contentSource) {
    this.queueDao.updatePerfQueueStatusToCompleted(classId, contentSource);
  }

  public void deleteCompletedFromPerfQueue() {
    this.queueDao.deleteCompletedFromPerfQueue();
  }

  public void insertIntoTimespentQueue(ContextObject context) {
    this.queueDao.insertIntoTimespentQueue(context);
  }

  public List<GroupPerfTSReortsQueueModel> fetchClassesForTimespentProcessing(Integer limit,
      Integer offset) {
    return this.queueDao.fetchClassesForTimespentProcessing(limit, offset);
  }

  public void updateTimespentQueueStatusToCompleted(String classId, String contentSource) {
    this.queueDao.updateTimespentQueueStatusToCompleted(classId, contentSource);
  }

  public void deleteCompletedFromTimespentQueue() {
    this.queueDao.deleteCompletedFromTimespentQueue();
  }

}
