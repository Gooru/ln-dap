package org.gooru.dap.deps.competency.assessmentscore.content;

import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.ContextMapper;
import org.gooru.dap.deps.competency.events.mapper.ResultMapper;

/**
 * @author gooru on 14-May-2018
 */
public final class ContentCompetencyEvidenceCommandBuilder {

  private ContentCompetencyEvidenceCommandBuilder() {
    throw new AssertionError();
  }

  public static ContentCompetencyEvidenceCommand build(
      AssessmentScoreEventMapper assessmentScoreEvent) {
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
    String contentSource = context.getContentSource();

    ResultMapper result = assessmentScoreEvent.getResult();
    Double score = null;
    if (result != null) {
      score = result.getScore();
    }

    ContentCompetencyEvidenceCommand command =
        new ContentCompetencyEvidenceCommand(userId, classId, courseId, unitId, lessonId, sessionId,
            collectionId, pathId, score, collectionType, activityTime, contentSource);
    // TODO: validation for the collection type ??
    return command;
  }
}
