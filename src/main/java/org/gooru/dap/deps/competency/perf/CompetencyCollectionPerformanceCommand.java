package org.gooru.dap.deps.competency.perf;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyCollectionPerformanceCommand {

	private String userId;
	private String competencyCode;
	private String competencyDisplayCode;
	private String competencyTitle;
	private String frameworkCode;
	private String gutCode;
	private String classId;
	private String courseId;
	private String unitId;
	private String lessonId;
	private String latestSessionId;
	private String collectionId;
	private long collectionPathId;
	private long collectionTimeSpent;
	private double collectionScore;
	private String collectionType;

	public String getUserId() {
		return userId;
	}

	public String getCompetencyCode() {
		return competencyCode;
	}

	public String getCompetencyDisplayCode() {
		return competencyDisplayCode;
	}

	public String getCompetencyTitle() {
		return competencyTitle;
	}

	public String getFrameworkCode() {
		return frameworkCode;
	}

	public String getGutCode() {
		return gutCode;
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

	public long getCollectionTimeSpent() {
		return collectionTimeSpent;
	}

	public double getCollectionScore() {
		return collectionScore;
	}

	public String getCollectionType() {
		return collectionType;
	}

	static CompetencyCollectionPerformanceCommand builder(JsonNode request) {
		CompetencyCollectionPerformanceCommand command = CompetencyCollectionPerformanceCommand.buildFromJson(request);
		command.validate();
		return command;
	}

	private static CompetencyCollectionPerformanceCommand buildFromJson(JsonNode request) {
		CompetencyCollectionPerformanceCommand command = new CompetencyCollectionPerformanceCommand();
		return command;
	}

	private void validate() {

	}

	public CompetencyCollectionPerformanceBean asBean() {
		CompetencyCollectionPerformanceBean bean = new CompetencyCollectionPerformanceBean();
		return bean;
	}

	public static class CompetencyCollectionPerformanceBean {
		private String userId;
		private String competencyCode;
		private String competencyDisplayCode;
		private String competencyTitle;
		private String frameworkCode;
		private String gutCode;
		private String classId;
		private String courseId;
		private String unitId;
		private String lessonId;
		private String latestSessionId;
		private String collectionId;
		private long collectionPathId;
		private long collectionTimeSpent;
		private double collectionScore;
		private String collectionType;

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

		public String getCompetencyDisplayCode() {
			return competencyDisplayCode;
		}

		public void setCompetencyDisplayCode(String competencyDisplayCode) {
			this.competencyDisplayCode = competencyDisplayCode;
		}

		public String getCompetencyTitle() {
			return competencyTitle;
		}

		public void setCompetencyTitle(String competencyTitle) {
			this.competencyTitle = competencyTitle;
		}

		public String getFrameworkCode() {
			return frameworkCode;
		}

		public void setFrameworkCode(String frameworkCode) {
			this.frameworkCode = frameworkCode;
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

		public long getCollectionPathId() {
			return collectionPathId;
		}

		public void setCollectionPathId(long collectionPathId) {
			this.collectionPathId = collectionPathId;
		}

		public long getCollectionTimeSpent() {
			return collectionTimeSpent;
		}

		public void setCollectionTimeSpent(long collectionTimeSpent) {
			this.collectionTimeSpent = collectionTimeSpent;
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
	}
}
