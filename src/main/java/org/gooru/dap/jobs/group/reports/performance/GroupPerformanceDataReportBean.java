
package org.gooru.dap.jobs.group.reports.performance;

/**
 * @author szgooru Created On 11-Apr-2019
 */
public class GroupPerformanceDataReportBean {

  private Double assessmentPerformance;
  private Long groupId;
  private Long schoolId;
  private Long stateId;
  private Long countryId;
  private Integer month;
  private Integer year;
  private String contentSource;
  private String tenant;

  public Double getAssessmentPerformance() {
    return assessmentPerformance;
  }

  public void setAssessmentPerformance(Double assessmentPerformance) {
    this.assessmentPerformance = assessmentPerformance;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
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

  public String getContentSource() {
    return contentSource;
  }

  public void setContentSource(String contentSource) {
    this.contentSource = contentSource;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

}
