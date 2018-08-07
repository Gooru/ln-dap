package org.gooru.dap.deps.competency.assessmentscore.content;

import java.sql.Timestamp;

/**
 * @author gooru on 14-May-2018
 */
public class ContentCompetencyEvidenceBean {
	private String userId;
	private String competencyCode;
	private String frameworkCode;
	private boolean microCompetency;
	private String gutCode;
	private String classId;
	private String courseId;
	private String unitId;
	private String lessonId;
	private String latestSessionId;
	private String collectionId;
	private Long collectionPathId;
	private Double collectionScore;
	private String collectionType;
	private int status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public ContentCompetencyEvidenceBean() {
	}

	public ContentCompetencyEvidenceBean(ContentCompetencyEvidenceCommand command) {
		this.userId = command.getUserId();
		this.classId = command.getClassId();
		this.courseId = command.getCourseId();
		this.unitId = command.getUnitId();
		this.lessonId = command.getLessonId();
		this.latestSessionId = command.getLatestSessionId();
		this.collectionId = command.getCollectionId();
		this.collectionPathId = command.getCollectionPathId();
		this.collectionScore = command.getCollectionScore();
		this.collectionType = command.getCollectionType();

		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.createdAt = now;
		this.updatedAt = now;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompetencyCode() {
		return competencyCode;
	}

	public void setCompetencyCode(String competencyCode) {
		this.competencyCode = competencyCode;
	}

	public String getFrameworkCode() {
		return frameworkCode;
	}

	public void setFrameworkCode(String frameworkCode) {
		this.frameworkCode = frameworkCode;
	}

	public boolean isMicroCompetency() {
		return microCompetency;
	}

	public void setMicroCompetency(boolean microCompetency) {
		this.microCompetency = microCompetency;
	}

	public String getGutCode() {
		return gutCode;
	}

	public void setGutCode(String gutCode) {
		this.gutCode = gutCode;
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

	public String getLatestSessionId() {
		return latestSessionId;
	}

	public void setLatestSessionId(String latestSessionId) {
		this.latestSessionId = latestSessionId;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public Long getCollectionPathId() {
		return collectionPathId;
	}

	public void setCollectionPathId(Long collectionPathId) {
		this.collectionPathId = collectionPathId;
	}

	public Double getCollectionScore() {
		return collectionScore;
	}

	public void setCollectionScore(Double collectionScore) {
		this.collectionScore = collectionScore;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}
