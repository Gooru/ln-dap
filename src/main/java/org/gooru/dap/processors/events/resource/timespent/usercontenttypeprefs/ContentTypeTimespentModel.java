package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

/**
 * @author ashish.
 */

public class ContentTypeTimespentModel {
  private String contentType;
  private Long timeSpent;

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Long getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(Long timeSpent) {
    this.timeSpent = timeSpent;
  }

}
