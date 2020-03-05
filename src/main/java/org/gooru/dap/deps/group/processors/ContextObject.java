package org.gooru.dap.deps.group.processors;

/**
 * @author gooru on 10-May-2018
 */
public class ContextObject {

  private String courseId;
  private String classId;
  private String unitId;
  private String lessonId;
  private String sessionId;
  private String tenantId;
  private String partnerId;
  private int questionCount;
  private long pathId;
  private String pathType;
  private String contextCollectionType;
  private String contextCollectionId;
  private String contentSource;
  private String additionalContext;

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getLessonId() {
    return lessonId;
  }

  public void setLessonId(String lessonId) {
    this.lessonId = lessonId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public int getQuestionCount() {
    return questionCount;
  }

  public void setQuestionCount(int questionCount) {
    this.questionCount = questionCount;
  }

  public long getPathId() {
    return pathId;
  }

  public void setPathId(long pathId) {
    this.pathId = pathId;
  }

  public String getPathType() {
    return pathType;
  }

  public void setPathType(String pathType) {
    this.pathType = pathType;
  }

  public String getContextCollectionType() {
    return contextCollectionType;
  }

  public void setContextCollectionType(String contextCollectionType) {
    this.contextCollectionType = contextCollectionType;
  }

  public String getContextCollectionId() {
    return contextCollectionId;
  }

  public void setContextCollectionId(String contextCollectionId) {
    this.contextCollectionId = contextCollectionId;
  }

  public String getContentSource() {
    return contentSource;
  }

  public void setContentSource(String contentSource) {
    this.contentSource = contentSource;
  }

  public String getAdditionalContext() {
    return additionalContext;
  }

  public void setAdditionalContext(String additionalContext) {
    this.additionalContext = additionalContext;
  }

  @Override
  public String toString() {
    return "ContextMapper [courseId=" + courseId + ", classId=" + classId + ", unitId=" + unitId
        + ", lessonId=" + lessonId + ", sessionId=" + sessionId + ", tenantId=" + tenantId
        + ", partnerId=" + partnerId + ", questionCount=" + questionCount + ", pathId=" + pathId
        + ", contentSource=" + contentSource + ", contextCollectionType=" + contextCollectionType
        + ", " + "contextCollectionId=" + contextCollectionId + ", " + "contextCollectionId="
        + "+ contextCollectionId + ]";
  }
}
