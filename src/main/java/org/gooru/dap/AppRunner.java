package org.gooru.dap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.gooru.dap.components.AppFinalizer;
import org.gooru.dap.components.AppInitializer;
import org.gooru.dap.configuration.AppConfiguration;
import org.gooru.dap.infra.ConsumersDeployer;
import org.gooru.dap.jobs.schedular.init.JobChain;
import org.gooru.dap.jobs.schedular.init.JobChainOne;
import org.gooru.dap.jobs.schedular.init.JobChainRunner;
import org.gooru.dap.jobs.schedular.init.JobChainThree;
import org.gooru.dap.jobs.schedular.init.JobChainTwo;
import org.gooru.dap.jobs.schedular.init.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * This is the main class to bootstrap the whole application, deploy different listeners and let
 * them work.
 *
 * @author ashish on 16/4/18.
 */
public class AppRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);

  public static void main(String[] args) {
    new AppRunner().run();
  }

  private void run() {
    initializeApplication();
    runApplication();
    finalizeApplication();
  }

  private void initializeApplication() {
    setupSystemProperties();
    setupLoggerMachinery();
    AppInitializer.initializeApp();
    createSchedulers();
  }

  private void finalizeApplication() {
    AppFinalizer.finalizeApp();
  }

  private void runApplication() {
    new ConsumersDeployer(AppConfiguration.fetchConsumersToDeploy(),
        AppConfiguration.fetchConsumerConfigForDeployment()).deploy();
  }

  private void setupSystemProperties() {
    JsonNode systemPropsConfig = AppConfiguration.fetchSystemPropsConfig();
    for (Iterator<Map.Entry<String, JsonNode>> it = systemPropsConfig.fields(); it.hasNext();) {
      Map.Entry<String, JsonNode> property = it.next();
      final String propValue = property.getValue().textValue();
      LOGGER.debug("Property: '{}' is set to value '{}'", property.getKey(), propValue);
      if (propValue != null) {
        System.setProperty(property.getKey(), propValue);
      } else {
        throw new IllegalStateException("Invalid system property key value in config");
      }
    }
  }

  private void setupLoggerMachinery() {
    String logbackFile = System.getProperty("logback.configurationFile");
    if (logbackFile != null && !logbackFile.isEmpty()) {
      LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

      try {
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();
        configurator.doConfigure(logbackFile);
      } catch (JoranException je) {
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
        throw new IllegalStateException("Error while initialising Logger machinery");
      }
    } else {
      LOGGER.warn("Not able to find logback config file");
      throw new IllegalArgumentException("Invalid logback config file");
    }

  }

  private void createSchedulers() {
    try {
      JsonNode schedulerConfig = AppConfiguration.fetchJobsConfig();
      LOGGER.debug(schedulerConfig.toString());
      Schedulers schedulers =
          new ObjectMapper().readValue(schedulerConfig.toString(), Schedulers.class);
      List<JobChain> jobChainConfigs = schedulers.getJobChain();
      if (jobChainConfigs != null && !jobChainConfigs.isEmpty()) {
        for (JobChain jobChainConfig : jobChainConfigs) {
          JobChainRunner jobChain = getJobChainInstance(jobChainConfig.getChainId());
          jobChain.ruun(jobChainConfig);
        }
      }
    } catch (Exception e) {
      LOGGER.error("Invalid Schedule Configuration", e);
    }
  }

  private JobChainRunner getJobChainInstance(String jobChainId) {
    JobChainRunner jobChain = null;
    if (StringUtils.equalsIgnoreCase(jobChainId, JobChainOne.class.getCanonicalName())) {
      jobChain = new JobChainOne();
    } else if (StringUtils.equalsIgnoreCase(jobChainId, JobChainTwo.class.getCanonicalName())) {
      jobChain = new JobChainTwo();
    } else if (StringUtils.equalsIgnoreCase(jobChainId, JobChainThree.class.getCanonicalName())) {
      jobChain = new JobChainThree();
    }
    return jobChain;
  }

}
