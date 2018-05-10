package org.gooru.dap.deps.competency.score.mapper;

/**
 * @author gooru on 10-May-2018
 */
public class ContextMapper {

	private String courseId;
	private String classId;
	private String unitId;
	private String lessonId;
	private String sessionId;
	private String tenantId;
	private String partnerId;
	private int questionCount;
	private long pathId;

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public long getPathId() {
		return pathId;
	}

	public void setPathId(long pathId) {
		this.pathId = pathId;
	}

	@Override
	public String toString() {
		return "ContextMapper [courseId=" + courseId + ", classId=" + classId + ", unitId=" + unitId + ", lessonId="
				+ lessonId + ", sessionId=" + sessionId + ", tenantId=" + tenantId + ", partnerId=" + partnerId
				+ ", questionCount=" + questionCount + ", pathId=" + pathId + "]";
	}

}