
package org.gooru.dap.jobs.group.reports.competency;

/**
 * @author szgooru Created On 30-Apr-2019
 */
public class GroupCompetencyStatsModel {

  private Long groupId;
  private Long completedCount;
  private Long inprogressCount;
  private Long cumulativeCompletedCount;

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getCompletedCount() {
    return completedCount;
  }

  public void setCompletedCount(Long completedCount) {
    this.completedCount = completedCount;
  }

  public Long getInprogressCount() {
    return inprogressCount;
  }

  public void setInprogressCount(Long inprogressCount) {
    this.inprogressCount = inprogressCount;
  }

  public Long getCumulativeCompletedCount() {
    return cumulativeCompletedCount;
  }

  public void setCumulativeCompletedCount(Long cumulativeCompletedCount) {
    this.cumulativeCompletedCount = cumulativeCompletedCount;
  }

}
