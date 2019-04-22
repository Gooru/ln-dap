
package org.gooru.dap.jobs.schedular.init;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author mukul@gooru
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"jobId", "fetchClassCMPerfReqUri", "fetchClassCAPerfReqUri"})
public class JobConfig {

  @JsonProperty("jobId")
  private String jobId;

  @JsonProperty("fetchClassCMPerfReqUri")
  private String fetchClassCMPerfReqUri;
  
  @JsonProperty("fetchClassCAPerfReqUri")
  private String fetchClassCAPerfReqUri;

  public JobConfig() {}

  public JobConfig(String jobId, String fetchClassCMPerfReqUri, String fetchClassCAPerfReqUri) {
    super();
    this.jobId = jobId;
    this.fetchClassCMPerfReqUri = fetchClassCMPerfReqUri;
    this.fetchClassCAPerfReqUri = fetchClassCAPerfReqUri;
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

  @JsonProperty("fetchClassCMPerfReqUri")
  public String getFetchClassCMPerfReqUri() {
    return fetchClassCMPerfReqUri;
  }

  @JsonProperty("fetchClassCMPerfReqUri")
  public void setFetchClassCMPerfReqUri(String fetchClassCMPerfReqUri) {
    this.fetchClassCMPerfReqUri = fetchClassCMPerfReqUri;
  }

  @JsonProperty("fetchClassCMPerfReqUri")
  public JobConfig withFetchClassCMPerfReqUri(String fetchClassCMPerfReqUri) {
    this.fetchClassCMPerfReqUri = fetchClassCMPerfReqUri;
    return this;
  }

  public String getFetchClassCAPerfReqUri() {
    return fetchClassCAPerfReqUri;
  }

  public void setFetchClassCAPerfReqUri(String fetchClassCAPerfReqUri) {
    this.fetchClassCAPerfReqUri = fetchClassCAPerfReqUri;
  }

  public JobConfig withFetchClassCAPerfReqUri(String fetchClassCAPerfReqUri) {
    this.fetchClassCAPerfReqUri = fetchClassCAPerfReqUri;
    return this;
  }
}
