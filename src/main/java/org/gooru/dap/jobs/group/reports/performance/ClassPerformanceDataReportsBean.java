
package org.gooru.dap.jobs.group.reports.performance;

/**
 * @author szgooru Created On 09-Apr-2019
 */
public class ClassPerformanceDataReportsBean {

  private String classId;
  private Long assessmentTimespent;
  private Double assessmentPerformance;
  private String groupSubtype;
  private Long schoolDistrictId;
  private Long districtId;
  private Long blockId;
  private Long clusterId;
  private Long schoolId;
  private Long stateId;
  private Long countryId;
  private Integer week;
  private Integer month;
  private Integer year;
  private String subject;
  private String framework;
  private String contentSource;
  private String tenant;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Long getAssessmentTimespent() {
    return assessmentTimespent;
  }

  public void setAssessmentTimespent(Long assessmentTimespent) {
    this.assessmentTimespent = assessmentTimespent;
  }

  public Double getAssessmentPerformance() {
    return assessmentPerformance;
  }

  public void setAssessmentPerformance(Double assessmentPerformance) {
    this.assessmentPerformance = assessmentPerformance;
  }

  public String getGroupSubtype() {
    return groupSubtype;
  }

  public void setGroupSubtype(String groupSubtype) {
    this.groupSubtype = groupSubtype;
  }

  public Long getSchoolDistrictId() {
    return schoolDistrictId;
  }

  public void setSchoolDistrictId(Long schoolDistrictId) {
    this.schoolDistrictId = schoolDistrictId;
  }

  public Long getDistrictId() {
    return districtId;
  }

  public void setDistrictId(Long districtId) {
    this.districtId = districtId;
  }

  public Long getBlockId() {
    return blockId;
  }

  public void setBlockId(Long blockId) {
    this.blockId = blockId;
  }

  public Long getClusterId() {
    return clusterId;
  }

  public void setClusterId(Long clusterId) {
    this.clusterId = clusterId;
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
