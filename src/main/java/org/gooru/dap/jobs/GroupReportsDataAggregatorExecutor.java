
package org.gooru.dap.jobs;

import java.util.List;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.group.dbhelpers.GroupReortsAggregationQueueModel;
import org.gooru.dap.deps.group.dbhelpers.GroupReortsAggregationQueueService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public class GroupReportsDataAggregatorExecutor implements Job {
  
  private final static Logger LOGGER = LoggerFactory.getLogger("job.execution.logs");

  private final GroupReortsAggregationQueueService queueService = new GroupReortsAggregationQueueService(DBICreator.getDbiForDefaultDS());

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.debug("job execution started");
    List<GroupReortsAggregationQueueModel> dataModels = this.queueService.fetchClassesForProcessing();
    
    dataModels.forEach(model -> {
      LOGGER.debug("processing class '{}' ", model.getClassId());
      
      // Execution for the class has been finished, remove the record from the queue
    });
  }
  
  private void removeFromQueue(String classId, String tenant) {
    this.queueService.removeFromQueue(classId, tenant);
  }

}
