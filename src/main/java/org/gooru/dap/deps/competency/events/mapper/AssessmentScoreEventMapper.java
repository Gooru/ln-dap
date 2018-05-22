package org.gooru.dap.deps.competency.events.mapper;

/**
 * @author gooru on 09-May-2018
 */
public class AssessmentScoreEventMapper {

	private String userId;
	private String eventId;
	private String eventName;
	private String collectionId;
	private String collectionType;
	private long activityTime;
	private String version;

	private ContextMapper context;
	private ResultMapper result;

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

	public ContextMapper getContext() {
		return context;
	}

	public void setContext(ContextMapper context) {
		this.context = context;
	}

	public ResultMapper getResult() {
		return result;
	}

	public void setResult(ResultMapper result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "AssessmentScoreEventMapper [userId=" + userId + ", eventId=" + eventId + ", eventName=" + eventName
				+ ", collectionId=" + collectionId + ", collectionType=" + collectionType + ", activityTime="
				+ activityTime + ", version=" + version + ", context=" + context + ", result=" + result + "]";
	}

}
