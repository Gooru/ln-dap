package org.gooru.dap.processors.events.question.timespent;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;

import com.fasterxml.jackson.databind.JsonNode;

public class UserStatsCCULCQuestionTimeSpentBean {

    private String userId;
    private String courseId;
    private String unitId;
    private String lessonId;
    private String collectionId;
    private String questionId;
    private String collectionType;
    private String questionType;
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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public static UserStatsCCULCQuestionTimeSpentBean createInstance(JsonNode jsonNode, ContentBean contentBean) {
        UserStatsCCULCQuestionTimeSpentBean userStatsQuestionTimeSpentBean = new UserStatsCCULCQuestionTimeSpentBean();
        userStatsQuestionTimeSpentBean.setCourseId(jsonNode.at(EventMessageConstant.CTX_COURSE_ID).textValue());
        userStatsQuestionTimeSpentBean.setUnitId(jsonNode.at(EventMessageConstant.CTX_UNIT_ID).textValue());
        userStatsQuestionTimeSpentBean.setLessonId(jsonNode.at(EventMessageConstant.CTX_LESSON_ID).textValue());
        userStatsQuestionTimeSpentBean
            .setCollectionId(jsonNode.at(EventMessageConstant.CTX_COLLECTION_ID).textValue());
        final String classId = jsonNode.at(EventMessageConstant.CTX_CLASS_ID).textValue();
        if (classId != null) {
            userStatsQuestionTimeSpentBean.setClassId(classId);
        } else { 
            userStatsQuestionTimeSpentBean.setClassId("");
        }
        userStatsQuestionTimeSpentBean.setQuestionId(jsonNode.get(EventMessageConstant.RESOURCE_ID).textValue());
        userStatsQuestionTimeSpentBean.setQuestionType(contentBean.getContentType());
        userStatsQuestionTimeSpentBean.setCollectionType(jsonNode.at(EventMessageConstant.CTX_COLLECTION_TYPE).textValue());
        userStatsQuestionTimeSpentBean.setUserId(jsonNode.get(EventMessageConstant.USER_ID).textValue());
        userStatsQuestionTimeSpentBean.setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
        return userStatsQuestionTimeSpentBean;

    }

}
