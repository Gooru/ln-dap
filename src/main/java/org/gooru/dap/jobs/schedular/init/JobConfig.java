
package org.gooru.dap.jobs.schedular.init;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author mukul@gooru
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"jobId"})
public class JobConfig {

  @JsonProperty("jobId")
  private String jobId;

  public JobConfig() {}

  public JobConfig(String jobId) {
    super();
    this.jobId = jobId;
  }

  @JsonProperty("jobId")
  public String getJobId() {
    return jobId;
  }

  @JsonProperty("jobId")
  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public JobConfig withJobId(String jobId) {
    this.jobId = jobId;
    return this;
  }
}
