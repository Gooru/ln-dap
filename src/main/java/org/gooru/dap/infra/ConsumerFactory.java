package org.gooru.dap.infra;

import java.util.ArrayList;
import java.util.List;
import org.gooru.dap.configuration.KafkaConsumerConfig;
import org.gooru.dap.deps.competency.AssessmentScoreEventConsumer;
import org.gooru.dap.deps.competency.CollectionStartEventConsumer;
import org.gooru.dap.deps.competency.CompetencyStatsEventConsumer;
import org.gooru.dap.deps.group.GroupPerformanceReportsEventConsumer;
import org.gooru.dap.deps.group.GroupTimespentReportsEventConsumer;
import org.gooru.dap.deps.question.QuestionConsumer;
import org.gooru.dap.deps.resource.ResourceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will maintain the logic of creating concrete listener instances which are deployable
 * based on the name. The name would be coming from config file. Currently the name is just
 * canonical class names and we could have used reflection to create them, but this way it becomes
 * more flexible. All the consumers need to be registered here. Else they won't be subject to
 * deployment.
 *
 * @author ashish on 17/4/18.
 */
final class ConsumerFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerFactory.class);

  public static List<ConsumerTemplate> manufacture(String deploymentName,
      KafkaConsumerConfig config) {
    List<ConsumerTemplate> result = new ArrayList<>();
    int instances = config.getInstances();
    if (instances <= 0) {
      LOGGER.warn("Invalid instances asked to be created for '{}'", deploymentName);
      throw new IllegalStateException(
          "Invalid instances asked to be created for: " + deploymentName);
    }
    for (int index = 0; index < instances; index++) {
      result.add(createConsumer(index, deploymentName, config));
    }
    return result;
  }

  private static ConsumerTemplate createConsumer(int id, String deploymentName,
      KafkaConsumerConfig config) {
    switch (deploymentName) {

      case "org.gooru.dap.deps.ResourceConsumer":
        return new ResourceConsumer(id, config);

      case "org.gooru.dap.deps.QuestionConsumer":
        return new QuestionConsumer(id, config);

      case "org.gooru.dap.deps.competency.AssessmentScoreEventConsumer":
        return new AssessmentScoreEventConsumer(id, config);

      case "org.gooru.dap.deps.competency.CollectionStartEventConsumer":
        return new CollectionStartEventConsumer(id, config);

      case "org.gooru.dap.deps.competency.CompetencyStatsEventConsumer":
        return new CompetencyStatsEventConsumer(id, config);
        
      case "org.gooru.dap.deps.group.GroupPerformanceReportsEventConsumer":
        return new GroupPerformanceReportsEventConsumer(id, config);
        
      case "org.gooru.dap.deps.group.GroupTimespentReportsEventConsumer":
        return new GroupTimespentReportsEventConsumer(id, config);

      default:
        LOGGER.warn("Factory does not know to initiate the deployment for '{}'", deploymentName);
        throw new IllegalStateException("Invalid deployment descriptor: " + deploymentName);
    }
  }
}
