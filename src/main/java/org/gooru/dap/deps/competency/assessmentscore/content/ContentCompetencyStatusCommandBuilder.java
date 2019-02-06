package org.gooru.dap.deps.competency.assessmentscore.content;

import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.gooru.dap.deps.competency.events.mapper.CollectionStartEventMapper;

/**
 * @author gooru on 14-May-2018
 */
public final class ContentCompetencyStatusCommandBuilder {

  private ContentCompetencyStatusCommandBuilder() {
    throw new AssertionError();
  }

  public static ContentCompetencyStatusCommand build(AssessmentScoreEventMapper assessmentScore,
      String competencyCode) {

    String userId = assessmentScore.getUserId();
    long activityTime = assessmentScore.getActivityTime();

    ContentCompetencyStatusCommand command =
        new ContentCompetencyStatusCommand(userId, competencyCode, activityTime);
    return command;
  }

}
