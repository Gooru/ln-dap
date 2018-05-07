package org.gooru.dap.processors.repositories.jdbi.bean.resource;

import java.util.Date;

import org.gooru.dap.constants.EventMessageConstant;

import com.fasterxml.jackson.databind.JsonNode;

public class UserStatsResourceTimeSpentBean {

    private String userId;
    private String resourceId;
    private String contentType;
    private Date activityDate;
    private long timeSpent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public static UserStatsResourceTimeSpentBean fromJsonNode(JsonNode jsonNode) {
        UserStatsResourceTimeSpentBean userStatsResourceTimeSpentEntity = new UserStatsResourceTimeSpentBean();
        userStatsResourceTimeSpentEntity
            .setActivityDate(new Date(jsonNode.get(EventMessageConstant.ACTIVITY_TIME).longValue()));
        userStatsResourceTimeSpentEntity.setResourceId(jsonNode.get(EventMessageConstant.RESOURCE_ID).textValue());
        userStatsResourceTimeSpentEntity.setContentType(jsonNode.get(EventMessageConstant.CONTENT_TYPE).textValue());
        userStatsResourceTimeSpentEntity.setUserId(jsonNode.get(EventMessageConstant.USER_ID).textValue());
        userStatsResourceTimeSpentEntity.setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
        return userStatsResourceTimeSpentEntity;

    }

}
