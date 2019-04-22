
package org.gooru.dap.deps.group.processors;

/**
 * @author szgooru Created On 02-Apr-2019
 */
public class GroupReportsEventMapper {
  private String userId;
  private String eventId;
  private String eventName;
  private String collectionId;
  private String collectionType;
  private long activityTime;
  private String version;

  private ContextObject context;
  private ResultObject result;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getCollectionType() {
    return collectionType;
  }

  public void setCollectionType(String collectionType) {
    this.collectionType = collectionType;
  }

  public long getActivityTime() {
    return activityTime;
  }

  public void setActivityTime(long activityTime) {
    this.activityTime = activityTime;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public ContextObject getContext() {
    return context;
  }

  public void setContext(ContextObject context) {
    this.context = context;
  }

  public ResultObject getResult() {
    return result;
  }

  public void setResult(ResultObject result) {
    this.result = result;
  }

}
