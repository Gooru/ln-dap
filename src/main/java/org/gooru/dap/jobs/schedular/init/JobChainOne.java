package org.gooru.dap.jobs.schedular.init;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.gooru.dap.jobs.GroupPerformanceReportsJobExecutor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobChainingJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 03-Apr-2019
 * 
 */
public class JobChainOne implements JobChainRunner {

  private final static Logger LOGGER = LoggerFactory.getLogger(JobChainOne.class);

  private static final String JOBCONFIG = "jobConfig";

  @Override
  public void ruun(JobChain config) throws SchedulerException {
    try {
      JobChainingJobListener jobListener1 = new JobChainingJobListener("ChainListenerOne");

      JobDataMap performanceJobDataMap = new JobDataMap();
      List<JobConfig> jobConfigs = config.getConfig().getJobConfigs();
      jobConfigs.forEach(jc -> {
        LOGGER.debug("jobId := ", jc.getJobId());

        if (StringUtils.equalsIgnoreCase(jc.getJobId(),
            GroupPerformanceReportsJobExecutor.class.getCanonicalName())) {
          performanceJobDataMap.put(JOBCONFIG, jc);
        }
      });

      JobKey jobKey = JobKey.jobKey("perfReportsJob", "group1");
      JobDetail performanceReportJob = JobBuilder.newJob(GroupPerformanceReportsJobExecutor.class)
          .withIdentity(jobKey).storeDurably(true).setJobData(performanceJobDataMap).build();

      TriggerKey triggerKey = TriggerKey.triggerKey("perfReportsJobTrigger", "group1");
      Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
          .withSchedule(CronScheduleBuilder.cronSchedule(config.getConfig().getCronExpression()))
          .build();

      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.scheduleJob(performanceReportJob, jobTrigger);
      scheduler.getListenerManager().addJobListener(jobListener1);
      scheduler.start();
      LOGGER.debug("Performance Job has been scheduled successfully");
    } catch (Throwable t) {
      LOGGER.error("exception while scheduling the performance job", t);
      throw new SchedulerException(t.getMessage());
    }
  }
}
