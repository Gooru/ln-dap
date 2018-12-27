package org.gooru.dap.deps.competency.assessmentscore.content;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyEvidenceCommand {
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

  public ContentCompetencyEvidenceCommand(String userId, String classId, String courseId,
      String unitId, String lessonId, String latestSessionId, String collectionId,
      long collectionPathId, Double collectionScore, String collectionType, long activityTime) {
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

}
