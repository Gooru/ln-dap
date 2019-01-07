package org.gooru.dap.deps.competency.assessmentscore.atc;

/**
 * @author mukul@gooru
 */
public class AtcEvent {

  private String subjectCode;
  private Integer gradeId;
  private String classId;
  private String courseId;
  private String userId;

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public Integer getGradeId() {
    return gradeId;
  }

  public void setGradeId(Integer gradeId) {
    this.gradeId = gradeId;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public AtcEvent(String classId, String courseId, String userId, Integer gradeId,
      String subjectCode) {
    this.classId = classId;
    this.courseId = courseId;
    this.userId = userId;
    this.gradeId = gradeId;
    this.subjectCode = subjectCode;
  }

}
