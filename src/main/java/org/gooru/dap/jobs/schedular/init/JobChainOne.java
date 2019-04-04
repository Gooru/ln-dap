package org.gooru.dap.jobs.schedular.init;

import org.gooru.dap.jobs.GroupReportsDataAggregatorExecutor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
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

  @Override
  public void ruun(JobChain config) throws SchedulerException {
    try {
      JobChainingJobListener jobListener1 = new JobChainingJobListener("ChainListenerOne");

      JobDetail timeSpentJob = JobBuilder.newJob(GroupReportsDataAggregatorExecutor.class)
          .withIdentity("groupReportsJob", "group1").storeDurably(true).build();

      Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity("trig", "group1")
          .withSchedule(CronScheduleBuilder.cronSchedule(config.getConfig().getCronExpression()))
          .build();

      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.scheduleJob(timeSpentJob, jobTrigger);

      scheduler.getListenerManager().addJobListener(jobListener1);
      scheduler.start();
      LOGGER.debug("Job has been scheduled successfully");
    } catch (Throwable t) {
      LOGGER.error("exception while scheduling the job", t);
      throw new SchedulerException(t.getMessage());
    }
  }
}
