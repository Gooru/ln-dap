
package org.gooru.dap.deps.group.dbhelpers;

import java.util.List;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.gooru.dap.deps.group.processors.ContextObject;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public class GroupReortsAggregationQueueService {

  private final GroupReortsAggregationQueueDao queueDao;

  public GroupReortsAggregationQueueService(DBI dbi) {
    this.queueDao = dbi.onDemand(GroupReortsAggregationQueueDao.class);
  }
  
  public void insertIntoQueue(ContextObject context) {
    this.queueDao.insertIntoQueue(context);
  }

  public List<GroupReortsAggregationQueueModel> fetchClassesForProcessing(Integer limit, Integer offset) {
    return this.queueDao.fetchClassesForProcessing(limit, offset);
  }

  public void removeFromQueue(List<String> classIds) {
    this.queueDao.removeFromQueue(CollectionUtils.convertToSqlArrayOfString(classIds));
    
  }
}
