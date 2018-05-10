package org.gooru.dap.deps.competency.score;

import org.gooru.dap.deps.competency.score.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.score.mapper.ContextMapper;
import org.gooru.dap.deps.competency.score.mapper.ResultMapper;

/**
 * @author gooru on 07-May-2018
 */
public final class CompetencyCollectionScoreCommandBuilder {

	private CompetencyCollectionScoreCommandBuilder() {
		throw new AssertionError();
	}

	public static CompetencyCollectionScoreCommand build(AssessmentScoreEventMapper assessmentScoreEvent) {

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

		CompetencyCollectionScoreCommand command = new CompetencyCollectionScoreCommand(userId, classId, courseId,
				unitId, lessonId, sessionId, collectionId, pathId, score, collectionType, activityTime);
		new CompetencyCollectionScoreCommandValidator(command).validate();
		return command;
	}
}
