
package org.gooru.dap.jobs.group.reports.competency;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class ClassCompetencyStatsModel {

  private String classId;
  private Long completedCount;
  private Long inprogressCount;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
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

}
