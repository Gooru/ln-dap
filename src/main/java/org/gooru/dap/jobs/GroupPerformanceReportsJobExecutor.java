
package org.gooru.dap.jobs;

import java.util.ArrayList;
import java.util.List;
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

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupPerformanceReportsJobExecutor.class);

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

        if (dataModels == null || dataModels.isEmpty()) {
          LOGGER.debug("no more records to process, exit");
          break;
        }

        List<ClassJson> classesForCM = new ArrayList<>(dataModels.size());
        List<ClassJson> classesForCA = new ArrayList<>(dataModels.size());

        // Iterate on records from queue to prepare request json
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

        ClassPerformanceRequest cmRequest = new ClassPerformanceRequest();
        cmRequest.setClasses(classesForCM);

        ClassPerformanceRequest caRequest = new ClassPerformanceRequest();
        caRequest.setClasses(classesForCA);

        HttpRequestHelper httpHelper = new HttpRequestHelper();
        ClassPerformanceResponse cmClassPerformances = httpHelper.fetchClassPerformances(
            config.getFetchClassCMPerfReqUri(), objectMapper.writeValueAsString(cmRequest));

        ClassPerformanceResponse caClassPerformances = httpHelper.fetchClassPerformances(
            config.getFetchClassCAPerfReqUri(), objectMapper.writeValueAsString(caRequest));
        
        List<UsageData> allUsageData = new ArrayList<>(
            cmClassPerformances.getUsageData().size() + caClassPerformances.getUsageData().size());
        
        List<UsageData> cmUsageData = cmClassPerformances.getUsageData();
        cmUsageData.forEach(usage -> {
          usage.setContentSource(EventMessageConstant.CONTENT_SOURCE_CM);
          usage.setProcessed(false);
          allUsageData.add(usage);
        });

        List<UsageData> caUsageData = caClassPerformances.getUsageData();
        caUsageData.forEach(usage -> {
          usage.setContentSource(EventMessageConstant.CONTENT_SOURCE_CA);
          usage.setProcessed(false);
          allUsageData.add(usage);
        });

        new GroupPerformanceReportsProcessor(allUsageData).process();

        // Queue cleanup
        // Execution for the class has been finished, remove the record from the queue
        // removeFromQueue(model.getClassId(), model.getTenant());

        // Increase offset count
        offset = offset + limit;
      }
    } catch (Throwable t) {
      
    }
  }

  private void removeFromQueue(List<String> classIds) {
    this.queueService.removeFromQueue(classIds);
  }

}
