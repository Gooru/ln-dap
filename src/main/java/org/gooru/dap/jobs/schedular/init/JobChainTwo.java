
package org.gooru.dap.jobs.schedular.init;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.gooru.dap.jobs.GroupTimespentReportsJobExecutor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobChainingJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class JobChainTwo implements JobChainRunner {

  private final static Logger LOGGER = LoggerFactory.getLogger(JobChainTwo.class);

  private static final String JOBCONFIG = "jobConfig";

  @Override
  public void ruun(JobChain config) throws SchedulerException {
    try {
      JobChainingJobListener jobListener1 = new JobChainingJobListener("ChainListenerTwo");

      JobDataMap timespentJobDataMap = new JobDataMap();
      List<JobConfig> jobConfigs = config.getConfig().getJobConfigs();
      jobConfigs.forEach(jc -> {
        LOGGER.debug("jobId := ", jc.getJobId());

        if (StringUtils.equalsIgnoreCase(jc.getJobId(),
            GroupTimespentReportsJobExecutor.class.getCanonicalName())) {
          timespentJobDataMap.put(JOBCONFIG, jc);
        }
      });

      JobKey jobKey = JobKey.jobKey("groupTimespentReportsJob", "group2");

      JobDetail timespentReportJob = JobBuilder.newJob(GroupTimespentReportsJobExecutor.class)
          .withIdentity(jobKey).storeDurably(true).setJobData(timespentJobDataMap).build();

      Trigger jobTrigger = TriggerBuilder.newTrigger()
          .withIdentity("groupTimespentReportsJobTrigger", "group2")
          .withSchedule(CronScheduleBuilder.cronSchedule(config.getConfig().getCronExpression()))
          .build();

      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.scheduleJob(timespentReportJob, jobTrigger);
      scheduler.getListenerManager().addJobListener(jobListener1);
      scheduler.start();
      LOGGER.debug("Timespent Job has been scheduled successfully");
    } catch (Throwable t) {
      LOGGER.error("exception while scheduling the timespent job", t);
      throw new SchedulerException(t.getMessage());
    }
  }

}
