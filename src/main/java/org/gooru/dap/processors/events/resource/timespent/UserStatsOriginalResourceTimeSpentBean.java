package org.gooru.dap.processors.events.resource.timespent;

import java.util.Date;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.repositories.jdbi.common.Dao.ContentBean;

import com.fasterxml.jackson.databind.JsonNode;

public class UserStatsOriginalResourceTimeSpentBean {

    private String userId;
    private String originalResourceId;
    private String contentType;
    private Date activityDate;
    private long timeSpent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getOriginalResourceId() {
        return originalResourceId;
    }

    public void setOriginalResourceId(String originalResourceId) {
        this.originalResourceId = originalResourceId;
    }

    public static UserStatsOriginalResourceTimeSpentBean createInstance(JsonNode jsonNode, ContentBean contentBean) {
        UserStatsOriginalResourceTimeSpentBean userStatsResourceTimeSpentEntity =
            new UserStatsOriginalResourceTimeSpentBean();
        userStatsResourceTimeSpentEntity
            .setActivityDate(new Date(jsonNode.get(EventMessageConstant.ACTIVITY_TIME).longValue()));
        userStatsResourceTimeSpentEntity.setOriginalResourceId(contentBean.getOriginalContentId());
        userStatsResourceTimeSpentEntity.setContentType(contentBean.getContentType());
        userStatsResourceTimeSpentEntity.setUserId(jsonNode.get(EventMessageConstant.USER_ID).textValue());
        userStatsResourceTimeSpentEntity.setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
        return userStatsResourceTimeSpentEntity;

    }

}
