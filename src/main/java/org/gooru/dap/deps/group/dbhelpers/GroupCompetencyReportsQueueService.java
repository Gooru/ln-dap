
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class GroupCompetencyReportsQueueService {

  private final GroupCompetencyReportsQueueDao dao;

  public GroupCompetencyReportsQueueService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportsQueueDao.class);
  }

  public void insertIntoQueue(String classId, String tenant) {
    this.dao.insertIntoQueue(classId, tenant);
  }

  public List<String> fetchClassesForProcessing(int limit, int offset) {
    return this.dao.fetchClassFromQueueForProcessing(limit, offset);
  }

  public void updateQueueStatusToCompleted(String classId) {
    this.dao.updateQueueStatusToCompleted(classId);
  }

  public void deleteFromQueue() {
    this.dao.deleteFromQueue();
  }
}
