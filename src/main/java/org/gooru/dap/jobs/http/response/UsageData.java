
package org.gooru.dap.jobs.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru Created On 04-Apr-2019
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsageData {

  @JsonProperty("classId")
  private String classId;

  @JsonProperty("timeSpent")
  private Long timeSpent;

  @JsonProperty("totalCount")
  private Integer totalCount;

  @JsonProperty("completedCount")
  private Integer completedCount;

  @JsonProperty("scoreInPercentage")
  private Double scoreInPercentage;

  private String contentSource;

  private Boolean processed;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Long getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(Long timeSpent) {
    this.timeSpent = timeSpent;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getCompletedCount() {
    return completedCount;
  }

  public void setCompletedCount(Integer completedCount) {
    this.completedCount = completedCount;
  }

  public Double getScoreInPercentage() {
    return scoreInPercentage;
  }

  public void setScoreInPercentage(Double scoreInPercentage) {
    this.scoreInPercentage = scoreInPercentage;
  }

  public String getContentSource() {
    return contentSource;
  }

  public void setContentSource(String contentSource) {
    this.contentSource = contentSource;
  }

  public Boolean isProcessed() {
    return processed;
  }

  public void setProcessed(Boolean processed) {
    this.processed = processed;
  }

}
