package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyEvidenceCommand {

  private String userId;
  private String classId;
  private String courseId;
  private String unitId;
  private String lessonId;
  private String latestSessionId;
  private String collectionId;
  private Long collectionPathId;
  private Double collectionScore;
  private String collectionType;
  private long activityTime;
  private String contentSource;
  private String gutCode;

  public LearnerProfileCompetencyEvidenceCommand(String userId, String classId, String courseId,
      String unitId, String lessonId, String latestSessionId, String collectionId,
      Long collectionPathId, Double collectionScore, String collectionType, long activityTime,
      String contentSource) {
    this.userId = userId;
    this.classId = classId;
    this.courseId = courseId;
    this.unitId = unitId;
    this.lessonId = lessonId;
    this.latestSessionId = latestSessionId;
    this.collectionId = collectionId;
    this.collectionPathId = collectionPathId;
    this.collectionScore = collectionScore;
    this.collectionType = collectionType;
    this.activityTime = activityTime;
    this.contentSource = contentSource;
  }

  public LearnerProfileCompetencyEvidenceCommand(String userId, String gutCode,
      String contentSource) {
    this.userId = userId;
    this.gutCode = gutCode;
    this.contentSource = contentSource;
  }

  public String getUserId() {
    return userId;
  }

  public String getClassId() {
    return classId;
  }

  public String getCourseId() {
    return courseId;
  }

  public String getUnitId() {
    return unitId;
  }

  public String getLessonId() {
    return lessonId;
  }

  public String getLatestSessionId() {
    return latestSessionId;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public Long getCollectionPathId() {
    return collectionPathId;
  }

  public Double getCollectionScore() {
    return collectionScore;
  }

  public String getCollectionType() {
    return collectionType;
  }

  public long getActivityTime() {
    return activityTime;
  }

  public String getContentSource() {
    return contentSource;
  }

  public String getGutCode() {
    return gutCode;
  }

  public void setGutCode(String gutCode) {
    this.gutCode = gutCode;
  }

}
