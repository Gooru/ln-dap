package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;
import com.fasterxml.jackson.databind.JsonNode;

public class UserStatsCCULCResourceTimeSpentBean {

  private String userId;
  private String courseId;
  private String unitId;
  private String lessonId;
  private String collectionId;
  private String resourceId;
  private String collectionType;
  private String contentType;
  private String classId;
  private long timeSpent;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public String getCollectionType() {
    return collectionType;
  }

  public void setCollectionType(String collectionType) {
    this.collectionType = collectionType;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public long getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(long timeSpent) {
    this.timeSpent = timeSpent;
  }

  public static UserStatsCCULCResourceTimeSpentBean createInstance(JsonNode jsonNode,
      ContentBean contentBean) {
    UserStatsCCULCResourceTimeSpentBean userStatsResourceTimeSpentBean =
        new UserStatsCCULCResourceTimeSpentBean();
    userStatsResourceTimeSpentBean
        .setCourseId(jsonNode.at(EventMessageConstant.CTX_COURSE_ID).textValue());
    userStatsResourceTimeSpentBean
        .setUnitId(jsonNode.at(EventMessageConstant.CTX_UNIT_ID).textValue());
    userStatsResourceTimeSpentBean
        .setLessonId(jsonNode.at(EventMessageConstant.CTX_LESSON_ID).textValue());
    userStatsResourceTimeSpentBean
        .setCollectionId(jsonNode.at(EventMessageConstant.CTX_COLLECTION_ID).textValue());
    final String classId = jsonNode.at(EventMessageConstant.CTX_CLASS_ID).textValue();
    if (classId != null) {
      userStatsResourceTimeSpentBean.setClassId(classId);
    } else {
      userStatsResourceTimeSpentBean.setClassId("");
    }
    userStatsResourceTimeSpentBean
        .setResourceId(jsonNode.get(EventMessageConstant.RESOURCE_ID).textValue());
    userStatsResourceTimeSpentBean.setContentType(contentBean.getContentType());
    userStatsResourceTimeSpentBean
        .setCollectionType(jsonNode.at(EventMessageConstant.CTX_COLLECTION_TYPE).textValue());
    userStatsResourceTimeSpentBean
        .setUserId(jsonNode.get(EventMessageConstant.USER_ID).textValue());
    userStatsResourceTimeSpentBean
        .setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
    return userStatsResourceTimeSpentBean;

  }

}
