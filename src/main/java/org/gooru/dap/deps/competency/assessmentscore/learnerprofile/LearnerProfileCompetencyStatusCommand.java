package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusCommand {

  private String txSubjectCode;
  private String userId;
  private String gutCode;
  private long activityTime;

  public LearnerProfileCompetencyStatusCommand(String txSubjectCode, String userId, String gutCode,
      long activityTime) {
    this.txSubjectCode = txSubjectCode;
    this.userId = userId;
    this.gutCode = gutCode;
    this.activityTime = activityTime;
  }

  public String getTxSubjectCode() {
    return txSubjectCode;
  }

  public String getUserId() {
    return userId;
  }

  public String getGutCode() {
    return gutCode;
  }

  public long getActivityTime() {
    return activityTime;
  }

}
