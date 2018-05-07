package org.gooru.dap.processors.events.resource.timespent;

import java.util.Date;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.repositories.jdbi.shared.Dao.ContentBean;

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

    public static ResourceContentTypeTimeSpentBean createInstance(JsonNode jsonNode, ContentBean contentBean) {
        ResourceContentTypeTimeSpentBean resourceContentTimeSpentEntity = new ResourceContentTypeTimeSpentBean();
        resourceContentTimeSpentEntity
            .setActivityDate(new Date(jsonNode.get(EventMessageConstant.ACTIVITY_TIME).longValue()));
        resourceContentTimeSpentEntity.setContentType(contentBean.getContentType());
        resourceContentTimeSpentEntity.setTimeSpent(jsonNode.at(EventMessageConstant.METRICS_TIMESPENT).longValue());
        return resourceContentTimeSpentEntity;

    }

}
