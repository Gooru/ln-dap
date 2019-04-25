
package org.gooru.dap.jobs;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.group.dbhelpers.GroupPerfTSReortsQueueService;
import org.gooru.dap.jobs.group.reports.timespent.CollectionPerformanceService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class GroupTimespentReportsJobExecutor implements Job {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupTimespentReportsJobExecutor.class);

  private static final String JOBCONFIG = "jobConfig";

  private final GroupPerfTSReortsQueueService queueService =
      new GroupPerfTSReortsQueueService(DBICreator.getDbiForDefaultDS());

  private final CollectionPerformanceService perfService =
      new CollectionPerformanceService(DBICreator.getDbiForReportsDS());

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {

  }

}
