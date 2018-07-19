package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import java.sql.Timestamp;

/**
 * @author gooru on 15-May-2018
 */
public class LearnerProfileMicroCompetencyEvidenceBean {
	private String userId;
	private String microCompetencyCode;
	private String classId;
	private String courseId;
	private String unitId;
	private String lessonId;
	private String latestSessionId;
	private String collectionId;
	private long collectionPathId;
	private double collectionScore;
	private String collectionType;
	private int status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public LearnerProfileMicroCompetencyEvidenceBean() {
	}

	public LearnerProfileMicroCompetencyEvidenceBean(LearnerProfileCompetencyEvidenceCommand command) {
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
		Timestamp ts = new Timestamp(command.getActivityTime());
		this.createdAt = ts;
		this.updatedAt = ts;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMicroCompetencyCode() {
		return microCompetencyCode;
	}

	public void setMicroCompetencyCode(String microCompetencyCode) {
		this.microCompetencyCode = microCompetencyCode;
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

	public long getCollectionPathId() {
		return collectionPathId;
	}

	public void setCollectionPathId(long collectionPathId) {
		this.collectionPathId = collectionPathId;
	}

	public double getCollectionScore() {
		return collectionScore;
	}

	public void setCollectionScore(double collectionScore) {
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
