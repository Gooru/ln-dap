
package org.gooru.dap.jobs.schedular.init;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author mukul@gooru
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"cronExpression", "jobConfigs"})
public class Config {

  @JsonProperty("cronExpression")
  private String cronExpression;
  
  @JsonProperty("jobConfigs")
  private List<JobConfig> jobConfigs = null;

  public Config() {}

  public Config(String cronExpression, List<JobConfig> jobConfigs) {
    super();
    this.cronExpression = cronExpression;
    this.jobConfigs = jobConfigs;
  }

  @JsonProperty("cronExpression")
  public String getCronExpression() {
    return cronExpression;
  }

  @JsonProperty("cronExpression")
  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public Config withCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
    return this;
  }

  @JsonProperty("jobConfigs")
  public List<JobConfig> getJobConfigs() {
    return jobConfigs;
  }

  @JsonProperty("jobConfigs")
  public void setJobConfigs(List<JobConfig> jobConfigs) {
    this.jobConfigs = jobConfigs;
  }

  public Config withJobConfigs(List<JobConfig> jobConfigs) {
    this.jobConfigs = jobConfigs;
    return this;
  }

}
