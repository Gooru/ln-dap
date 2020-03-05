
package org.gooru.dap.jobs;

import java.util.ArrayList;
import java.util.List;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.deps.group.dbhelpers.GroupPerfTSReortsQueueModel;
import org.gooru.dap.deps.group.dbhelpers.GroupPerfTSReortsQueueService;
import org.gooru.dap.jobs.group.reports.timespent.CollectionTimespentModel;
import org.gooru.dap.jobs.group.reports.timespent.GroupTimespentReportsProcessor;
import org.gooru.dap.jobs.group.reports.timespent.CollectionPerformanceService;
import org.gooru.dap.jobs.schedular.init.JobConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class GroupTimespentReportsJobExecutor implements Job {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupTimespentReportsJobExecutor.class);

  private static final String JOBCONFIG = "jobConfig";

  private final GroupPerfTSReortsQueueService queueService =
      new GroupPerfTSReortsQueueService(DBICreator.getDbiForDefaultDS());

  private final CollectionPerformanceService collectionPerfService =
      new CollectionPerformanceService(DBICreator.getDbiForReportsDS());

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.debug("timespent job execution started");
    try {
      JobConfig config = (JobConfig) context.getMergedJobDataMap().get(JOBCONFIG);
      ObjectMapper objectMapper = new ObjectMapper();

      Integer limit = 10;
      Integer offset = 0;

      while (true) {
        List<GroupPerfTSReortsQueueModel> dataModels =
            this.queueService.fetchClassesForTimespentProcessing(limit, offset);

        // If there are no more records to process, exit the job.
        if (dataModels == null || dataModels.isEmpty()) {
          LOGGER.debug("no more records to process, exit");
          break;
        }

        // These lists are used to store the request data for coursemap
        // and classactivities
        // separately.
        List<String> classesForCM = new ArrayList<>(dataModels.size());
        List<String> classesForCA = new ArrayList<>(dataModels.size());

        // Iterate on records from queue and separate the coursemap and
        // classactivities source
        // classes to fetch timespent data separately.
        dataModels.forEach(model -> {
          if (model.getContentSource().equalsIgnoreCase(EventMessageConstant.CONTENT_SOURCE_CM)) {
            classesForCM.add(model.getClassId());
          } else if (model.getContentSource()
              .equalsIgnoreCase(EventMessageConstant.CONTENT_SOURCE_CA)) {
            classesForCA.add(model.getClassId());
          }
        });

        LOGGER.debug("number of class to process for CM={} and CA={}", classesForCM.size(),
            classesForCA.size());

        List<CollectionTimespentModel> allModels = new ArrayList<>();

        // Fetch the timespent for the coursemap and collect the results
        // for further processing
        if (classesForCM != null && !classesForCM.isEmpty()) {
          List<CollectionTimespentModel> timespentForCM = this.collectionPerfService
              .fetchTimespentByClass(classesForCM, EventMessageConstant.CONTENT_SOURCE_CM);
          if (timespentForCM != null && !timespentForCM.isEmpty()) {
            allModels.addAll(timespentForCM);
          }
        }

        // Fetch the timespent for the classactivity and collect the
        // results for further processing
        if (classesForCA != null && !classesForCA.isEmpty()) {
          List<CollectionTimespentModel> timespentForCA = this.collectionPerfService
              .fetchTimespentByClass(classesForCA, EventMessageConstant.CONTENT_SOURCE_CA);
          if (timespentForCA != null && !timespentForCA.isEmpty()) {
            allModels.addAll(timespentForCA);
          }
        }

        new GroupTimespentReportsProcessor(allModels).process();

        // Queue cleanup
        // Execution for the current set of classes has been finished,
        // now we can delete the records
        // from the queue which are marked as completed by the report
        // processor
        this.queueService.deleteCompletedFromTimespentQueue();

        // Increase offset count to fetch next set of records from the
        // queue
        offset = offset + limit;
      }
    } catch (Throwable t) {
      LOGGER.error("error occured while executing group timespent reports job", t);
    }
  }

}
