
package org.gooru.dap.jobs.group.reports.performance;

/**
 * @author szgooru Created On 10-Apr-2019
 */
public class AssessmentPerfByGroupModel {

  private Double performance;
  private Long groupId;

  public Double getPerformance() {
    return performance;
  }

  public void setPerformance(Double performance) {
    this.performance = performance;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

}
