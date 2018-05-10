package org.gooru.dap.deps.competency.score;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyCollectionScoreCommand {

	private String userId;
	private String classId;
	private String courseId;
	private String unitId;
	private String lessonId;
	private String latestSessionId;
	private String collectionId;
	private long collectionPathId;
	private double collectionScore;
	private String collectionType;
	private long createdAt;
	private long updatedAt;

	public CompetencyCollectionScoreCommand() {
	}

	public CompetencyCollectionScoreCommand(String userId, String classId, String courseId, String unitId,
			String lessonId, String latestSessionId, String collectionId, long collectionPathId, double collectionScore,
			String collectionType, long activityTime) {
		this.userId = userId;
		this.classId = classId;
		this.courseId = courseId;
		this.unitId = unitId;
		this.lessonId = lessonId;
		this.latestSessionId = latestSessionId;
		this.collectionId = collectionId;
		this.collectionPathId = collectionPathId;
		this.collectionScore = collectionScore;
		this.collectionType = collectionType;
		this.createdAt = activityTime;
		this.updatedAt = activityTime;
	}

	public String getUserId() {
		return userId;
	}

	public String getClassId() {
		return classId;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getUnitId() {
		return unitId;
	}

	public String getLessonId() {
		return lessonId;
	}

	public String getLatestSessionId() {
		return latestSessionId;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public long getCollectionPathId() {
		return collectionPathId;
	}

	public double getCollectionScore() {
		return collectionScore;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

}