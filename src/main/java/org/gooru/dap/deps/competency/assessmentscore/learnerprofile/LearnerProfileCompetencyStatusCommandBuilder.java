package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import java.util.regex.Pattern;
import org.gooru.dap.deps.competency.events.mapper.ActivityDataEventMapper;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;

/**
 * @author gooru on 14-May-2018
 */
public final class LearnerProfileCompetencyStatusCommandBuilder {

  private static final Pattern HYPEN_PATTERN = Pattern.compile("-");

  private LearnerProfileCompetencyStatusCommandBuilder() {
    throw new AssertionError();
  }

  public static LearnerProfileCompetencyStatusCommand build(
      AssessmentScoreEventMapper assessmentScore, String gutCode) {
    String subjectCode = HYPEN_PATTERN.split(gutCode)[0];
    LearnerProfileCompetencyStatusCommand command = new LearnerProfileCompetencyStatusCommand(
        subjectCode, assessmentScore.getUserId(), gutCode, assessmentScore.getActivityTime());
    return command;
  }

  public static LearnerProfileCompetencyStatusCommand build(
      ActivityDataEventMapper activityDataEvent) {
    String gutCode = activityDataEvent.getContext().getGutCode();
    String subjectCode = HYPEN_PATTERN.split(gutCode)[0];
    LearnerProfileCompetencyStatusCommand command = new LearnerProfileCompetencyStatusCommand(
        subjectCode, activityDataEvent.getUserId(), gutCode, activityDataEvent.getContext().getContentSource());
    return command;
  }
}
