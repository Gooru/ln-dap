package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyStatusCommand {

  private String txSubjectCode;
  private String userId;
  private String gutCode;
  private long activityTime;
  private String profileSource;

  public LearnerProfileCompetencyStatusCommand(String txSubjectCode, String userId, String gutCode,
      long activityTime) {
    this.txSubjectCode = txSubjectCode;
    this.userId = userId;
    this.gutCode = gutCode;
    this.activityTime = activityTime;
  }

  public LearnerProfileCompetencyStatusCommand(String txSubjectCode, String userId, String gutCode,
      String profileSource) {
    this.txSubjectCode = txSubjectCode;
    this.userId = userId;
    this.gutCode = gutCode;
    this.profileSource = profileSource;
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

  public String getProfileSource() {
    return profileSource;
  }

  public void setProfileSource(String profileSource) {
    this.profileSource = profileSource;
  }

}
