
package org.gooru.dap.jobs.group.reports.competency;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class GroupCompetencyDataReportsBean {

  private Long groupId;
  private Long completedCount;
  private Long inprogressCount;
  private Long cumulativeCompletedCount;
  private Long schoolId;
  private Long stateId;
  private Long countryId;
  private String subject;
  private String framework;
  private Integer week;
  private Integer month;
  private Integer year;
  private String tenant;

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

  public Long getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
  }

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getFramework() {
    return framework;
  }

  public void setFramework(String framework) {
    this.framework = framework;
  }

  public Integer getWeek() {
    return week;
  }

  public void setWeek(Integer week) {
    this.week = week;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

}
