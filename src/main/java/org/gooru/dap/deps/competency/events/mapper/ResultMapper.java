package org.gooru.dap.deps.competency.events.mapper;

/**
 * @author gooru on 10-May-2018
 */
public class ResultMapper {

  private Double score;
  private Integer status;
  private Long timeSpent;

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(Long timeSpent) {
    this.timeSpent = timeSpent;
  }

  @Override
  public String toString() {
    return "ResultMapper [score=" + score + ", status=" + status + ", timeSpent=" + timeSpent + "]";
  }



}
