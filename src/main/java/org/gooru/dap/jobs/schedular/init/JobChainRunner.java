package org.gooru.dap.jobs.schedular.init;

import org.quartz.SchedulerException;

/**
 * @author mukul@gooru
 */
public interface JobChainRunner {

  public void ruun(JobChain config) throws SchedulerException;
}
