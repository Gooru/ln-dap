package org.gooru.dap.processors.events.question.timespent;

import java.util.Date;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;

import com.fasterxml.jackson.databind.JsonNode;

public class UserStatsQuestionTimeSpentBean {

    private String userId;
    private String questionId;
    private String questionType;
    private Date activityDate;
    private long timeSpent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public static UserStatsQuestionTimeSpentBean createInstance(JsonNode jsonNode, ContentBean contentBean) {
        UserStatsQuestionTimeSpentBean userStatsQuestionTimeSpentBean = new UserStatsQuestionTimeSpentBean();
        userStatsQuestionTimeSpentBean
            .setActivityDate(new Date(jsonNode.get(EventMessageConstant.ACTIVITY_TIME).longValue()));
        userStatsQuestionTimeSpentBean.setQuestionId(jsonNode.get(EventMessageConstant.RESOURCE_ID).textValue());
        userStatsQuestionTimeSpentBean.setQuestionType(contentBean.getContentType());
        userStatsQuestionTimeSpentBean.setUserId(jsonNode.get(EventMessageConstant.USER_ID).textValue());
        userStatsQuestionTimeSpentBean.setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
        return userStatsQuestionTimeSpentBean;

    }

}
