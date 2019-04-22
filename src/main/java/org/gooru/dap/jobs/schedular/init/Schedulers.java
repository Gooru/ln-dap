package org.gooru.dap.jobs.schedular.init;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author mukul@gooru
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"jobChain"})
public class Schedulers {

  @JsonProperty("jobChain")
  private List<JobChain> jobChain = null;

  public Schedulers() {}

  public Schedulers(List<JobChain> jobChain) {
    super();
    this.jobChain = jobChain;
  }

  @JsonProperty("jobChain")
  public List<JobChain> getJobChain() {
    return jobChain;
  }

  @JsonProperty("jobChain")
  public void setJobChain(List<JobChain> jobChain) {
    this.jobChain = jobChain;
  }

  public Schedulers withJobChain(List<JobChain> jobChain) {
    this.jobChain = jobChain;
    return this;
  }
}
