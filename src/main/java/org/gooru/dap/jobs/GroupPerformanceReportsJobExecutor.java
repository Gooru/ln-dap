
package org.gooru.dap.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.deps.group.dbhelpers.GroupReortsAggregationQueueModel;
import org.gooru.dap.deps.group.dbhelpers.GroupPerformanceReortsQueueService;
import org.gooru.dap.jobs.http.HttpRequestHelper;
import org.gooru.dap.jobs.http.request.ClassJson;
import org.gooru.dap.jobs.http.request.ClassPerformanceRequest;
import org.gooru.dap.jobs.http.response.ClassPerformanceResponse;
import org.gooru.dap.jobs.http.response.UsageData;
import org.gooru.dap.jobs.processors.GroupPerformanceReportsProcessor;
import org.gooru.dap.jobs.schedular.init.JobConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 03-Apr-2019
 */
public class GroupPerformanceReportsJobExecutor implements Job {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerformanceReportsJobExecutor.class);

  private static final String JOBCONFIG = "jobConfig";

  private final GroupPerformanceReortsQueueService queueService =
      new GroupPerformanceReortsQueueService(DBICreator.getDbiForDefaultDS());

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.debug("job execution started");
    try {
      JobConfig config = (JobConfig) context.getMergedJobDataMap().get(JOBCONFIG);
      ObjectMapper objectMapper = new ObjectMapper();

      Integer limit = 10;
      Integer offset = 0;

      while (true) {
        List<GroupReortsAggregationQueueModel> dataModels =
            this.queueService.fetchClassesForProcessing(limit, offset);

        // If there are no more records to process, exit the job.
        if (dataModels == null || dataModels.isEmpty()) {
          LOGGER.debug("no more records to process, exit");
          break;
        }

        // These lists are used to store the request data for coursemap and classactivities
        // separately.
        List<ClassJson> classesForCM = new ArrayList<>(dataModels.size());
        List<ClassJson> classesForCA = new ArrayList<>(dataModels.size());

        // Iterate on records from queue and separate the coursemap and classactivities source
        // classes to prepare the request using specific classes. The APIs to fetch performance data
        // for coursemap and classactivities are different
        dataModels.forEach(model -> {
          if (model.getContentSource().equalsIgnoreCase(EventMessageConstant.CONTENT_SOURCE_CM)) {
            ClassJson clsJson = new ClassJson(model.getClassId(), model.getCourseId());
            classesForCM.add(clsJson);
          } else if (model.getContentSource()
              .equalsIgnoreCase(EventMessageConstant.CONTENT_SOURCE_CA)) {
            ClassJson clsJson = new ClassJson(model.getClassId(), null);
            classesForCA.add(clsJson);
          }
        });

        LOGGER.debug("number of class to process for CM={} and CA={}", classesForCM.size(),
            classesForCA.size());
        HttpRequestHelper httpHelper = new HttpRequestHelper();

        // This list will be used to accumulate performance data for both coursemap and
        // classactivities which will be then used for further processing of aggregations at class
        // and group levels
        List<UsageData> allUsageData = new ArrayList<>();

        // If there are any classes for coursemap data then prepare the request and fetch the
        // performance data from analytics read API. Use the performance data returned by the API
        // for further processing.
        if (classesForCM != null && !classesForCM.isEmpty()) {
          ClassPerformanceRequest cmRequest = new ClassPerformanceRequest();
          cmRequest.setClasses(classesForCM);

          LOGGER.debug("request url:{}", config.getFetchClassCMPerfReqUri());
          ClassPerformanceResponse cmClassPerformances = httpHelper.fetchClassPerformances(
              config.getFetchClassCMPerfReqUri(), objectMapper.writeValueAsString(cmRequest));

          // ClassPerformanceResponse cmClassPerformances = getDummyResponseForCM(classesForCM);

          if (cmClassPerformances != null && cmClassPerformances.getUsageData() != null) {
            List<UsageData> cmUsageData = cmClassPerformances.getUsageData();
            cmUsageData.forEach(usage -> {
              usage.setContentSource(EventMessageConstant.CONTENT_SOURCE_CM);
              usage.setProcessed(false);
              allUsageData.add(usage);
            });
          } else {
            LOGGER.debug("No performance data for CM classes returned by the upstream API");
          }
        } else {
          LOGGER.debug("no classes to process for CM data");
        }

        // If there are any classes for the classactivities found in queue then prepare the request
        // and fetch the performance data from analytics read API. Use the performance data returned
        // by the API for further processing.
        if (classesForCA != null && !classesForCA.isEmpty()) {
          ClassPerformanceRequest caRequest = new ClassPerformanceRequest();
          caRequest.setClasses(classesForCA);

          ClassPerformanceResponse caClassPerformances = httpHelper.fetchClassPerformances(
              config.getFetchClassCAPerfReqUri(), objectMapper.writeValueAsString(caRequest));
          // ClassPerformanceResponse caClassPerformances = getDummyResponseForCA(classesForCA);

          if (caClassPerformances != null && caClassPerformances.getUsageData() != null) {
            List<UsageData> caUsageData = caClassPerformances.getUsageData();
            caUsageData.forEach(usage -> {
              usage.setContentSource(EventMessageConstant.CONTENT_SOURCE_CA);
              usage.setProcessed(false);
              allUsageData.add(usage);
            });
          } else {
            LOGGER.debug("No performance data for CA classes returned by the upstream API");
          }
        } else {
          LOGGER.debug("no classes to process for CA data");
        }

        // Now finally verify that we really have some data to process and send it for further
        // processing.
        if (!allUsageData.isEmpty()) {
          new GroupPerformanceReportsProcessor(allUsageData).process();
        }

        // Queue cleanup
        // Execution for the current set of classes has been finished, now we can delete the records
        // from the queue which are marked as completed by the report processor
        this.queueService.deleteCompletedFromQueue();

        // Increase offset count to fetch next set of records from the queue
        offset = offset + limit;
      }
    } catch (Throwable t) {
      LOGGER.error("error occured while executing group performance reports job", t);
    }
  }

  // Test purpose method
  private ClassPerformanceResponse getDummyResponseForCM(List<ClassJson> classes) {
    ClassPerformanceResponse response = new ClassPerformanceResponse();
    List<UsageData> usageData = new ArrayList<>();

    classes.forEach(cls -> {
      UsageData ud1 = new UsageData();
      ud1.setClassId(cls.getClassId());
      ud1.setContentSource(EventMessageConstant.CONTENT_SOURCE_CM);
      ud1.setScoreInPercentage(ThreadLocalRandom.current().nextDouble(1, 100));
      ud1.setTimeSpent(34656l);
      usageData.add(ud1);
    });

    response.setUsageData(usageData);
    return response;
  }

  // Test purpose method
  private ClassPerformanceResponse getDummyResponseForCA(List<ClassJson> classes) {
    ClassPerformanceResponse response = new ClassPerformanceResponse();
    List<UsageData> usageData = new ArrayList<>();

    classes.forEach(cls -> {
      UsageData ud1 = new UsageData();
      ud1.setClassId(cls.getClassId());
      ud1.setContentSource(EventMessageConstant.CONTENT_SOURCE_CA);
      ud1.setScoreInPercentage(ThreadLocalRandom.current().nextDouble(1, 100));
      usageData.add(ud1);
    });

    response.setUsageData(usageData);
    return response;
  }

}
