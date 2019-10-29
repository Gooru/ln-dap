
package org.gooru.dap.deps.struggling;

import java.time.LocalDate;

/**
 * @author szgooru Created On 16-Oct-2019
 */
public class StrugglingCompetencyCommand {

  private String compCode;
  private String userId;
  private Integer month;
  private Integer year;

  public String getCompCode() {
    return compCode;
  }

  public String getUserId() {
    return userId;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public static StrugglingCompetencyCommand build(String compCode, String userId) {
    StrugglingCompetencyCommand command = new StrugglingCompetencyCommand();
    command.compCode = compCode;
    command.userId = userId;

    LocalDate now = LocalDate.now();
    command.month = now.getMonthValue();
    command.year = now.getYear();

    return command;
  }

  public StrugglingCompetencyCommandBean asBean() {
    StrugglingCompetencyCommandBean bean = new StrugglingCompetencyCommandBean();
    bean.compCode = compCode;
    bean.userId = userId;
    bean.month = month;
    bean.year = year;

    return bean;
  }

  public static class StrugglingCompetencyCommandBean {
    private String compCode;
    private String userId;
    private Integer month;
    private Integer year;

    public String getCompCode() {
      return compCode;
    }

    public void setCompCode(String compCode) {
      this.compCode = compCode;
    }

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public Integer getMonth() {
      return month;
    }

    public void setMonth(Integer month) {
      this.month = month;
    }

    public Integer getYear() {
      return year;
    }

    public void setYear(Integer year) {
      this.year = year;
    }
  }
}
