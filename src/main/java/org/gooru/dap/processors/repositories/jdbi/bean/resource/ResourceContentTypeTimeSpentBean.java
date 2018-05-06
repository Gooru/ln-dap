package org.gooru.dap.processors.repositories.jdbi.bean.resource;

import java.util.Date;

import org.gooru.dap.constants.EventMessageConstant;

import com.fasterxml.jackson.databind.JsonNode;

public class ResourceContentTypeTimeSpentBean {

    private String contentType;
    private Date activityDate;
    private long timeSpent;


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

    public static ResourceContentTypeTimeSpentBean fromJsonNode(JsonNode jsonNode) {
        ResourceContentTypeTimeSpentBean resourceContentTimeSpentEntity = new ResourceContentTypeTimeSpentBean();
        resourceContentTimeSpentEntity
            .setActivityDate(new Date(jsonNode.get(EventMessageConstant.ACTIVITY_TIME).longValue()));
        resourceContentTimeSpentEntity.setContentType(jsonNode.get(EventMessageConstant.CONTENT_TYPE).textValue());
        resourceContentTimeSpentEntity.setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
        return resourceContentTimeSpentEntity;

    }

}
