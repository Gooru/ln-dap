
package org.gooru.dap.jobs;

import java.time.LocalDate;
import java.util.List;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.group.dbhelpers.GroupCompetencyReportsQueueService;
import org.gooru.dap.jobs.group.reports.competency.ClassCompetencyStatsModel;
import org.gooru.dap.jobs.group.reports.competency.GroupCompetencyReportsProcessor;
import org.gooru.dap.jobs.group.reports.competency.GroupCompetencyReportsService;
import org.gooru.dap.jobs.schedular.init.JobConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class GroupCompetencyReportsJobExecutor implements Job {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportsJobExecutor.class);

  private static final String JOBCONFIG = "jobConfig";

  private final GroupCompetencyReportsQueueService queueService =
      new GroupCompetencyReportsQueueService(DBICreator.getDbiForDefaultDS());

  private final GroupCompetencyReportsService competencyService =
      new GroupCompetencyReportsService(DBICreator.getDbiForDefaultDS());

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.debug("competency job execution started");
    try {
      JobConfig config = (JobConfig) context.getMergedJobDataMap().get(JOBCONFIG);
      ObjectMapper objectMapper = new ObjectMapper();

      Integer limit = 10;
      Integer offset = 0;

      while (true) {
        List<String> classes = this.queueService.fetchClassesForProcessing(limit, offset);

        // If there are no more records to process, exit the job.
        if (classes == null || classes.isEmpty()) {
          LOGGER.debug("no classes to process, exit");
          break;
        }

        LocalDate now = LocalDate.now();
        List<ClassCompetencyStatsModel> currentStatsModels = this.competencyService
            .fetchClassCompletionsByMonthAndYear(classes, now.getMonthValue(), now.getYear());

        LocalDate previousMonth = now.minusMonths(1);
        List<ClassCompetencyStatsModel> previousStatsModels =
            this.competencyService.fetchClassCompletionsByMonthAndYear(classes,
                previousMonth.getMonthValue(), previousMonth.getYear());

        new GroupCompetencyReportsProcessor(currentStatsModels, previousStatsModels).process();
        
        // Queue cleanup
        // Execution for the current set of classes has been finished, now we can delete the records
        // from the queue which are marked as completed by the report processor
        this.queueService.deleteFromQueue();

        // Increase offset count to fetch next set of records from the queue
        offset = offset + limit;
      }
    } catch (Throwable t) {
      LOGGER.error("error occured while executing group performance reports job", t);
    }
  }

}
