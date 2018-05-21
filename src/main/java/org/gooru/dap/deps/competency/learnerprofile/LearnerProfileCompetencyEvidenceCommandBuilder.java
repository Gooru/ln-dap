package org.gooru.dap.deps.competency.learnerprofile;

import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.ContextMapper;
import org.gooru.dap.deps.competency.events.mapper.ResultMapper;

/**
 * @author gooru on 14-May-2018
 */
public final class LearnerProfileCompetencyEvidenceCommandBuilder {

	private LearnerProfileCompetencyEvidenceCommandBuilder() {
		throw new AssertionError();
	}

	public static LearnerProfileCompetencyEvidenceCommand build(AssessmentScoreEventMapper assessmentScoreEvent) {
		String userId = assessmentScoreEvent.getUserId();
		String collectionId = assessmentScoreEvent.getCollectionId();
		String collectionType = assessmentScoreEvent.getCollectionType();
		long activityTime = assessmentScoreEvent.getActivityTime();

		ContextMapper context = assessmentScoreEvent.getContext();
		String classId = context.getClassId();
		String courseId = context.getCourseId();
		String unitId = context.getUnitId();
		String lessonId = context.getLessonId();
		String sessionId = context.getSessionId();
		long pathId = context.getPathId();

		ResultMapper result = assessmentScoreEvent.getResult();
		double score = result.getScore();

		LearnerProfileCompetencyEvidenceCommand command = new LearnerProfileCompetencyEvidenceCommand(userId, classId,
				courseId, unitId, lessonId, sessionId, collectionId, pathId, score, collectionType, activityTime);
		// TODO: validation for the collection type ??
		return command;

	}
}
