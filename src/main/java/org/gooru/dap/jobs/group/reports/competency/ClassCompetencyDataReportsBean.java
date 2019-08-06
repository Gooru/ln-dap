
package org.gooru.dap.jobs.group.reports.competency;

/**
 * @author szgooru Created On 26-Apr-2019
 */
public class ClassCompetencyDataReportsBean {

  private String classId;
  private Long completedCount;
  private Long inprogressCount;
  private Long cumulativeCompletedCount;
  private Long schoolId;
  private Long schoolDistrictId;
  private Long districtId;
  private Long blockId;
  private Long clusterId;
  private Long stateId;
  private Long countryId;
  private Integer month;
  private Integer year;
  private String tenant;

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

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

}
