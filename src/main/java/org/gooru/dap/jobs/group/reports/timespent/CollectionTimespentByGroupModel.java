
package org.gooru.dap.jobs.group.reports.timespent;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class CollectionTimespentByGroupModel {
  private Long timespent;
  private Long groupId;

  public Long getTimespent() {
    return timespent;
  }

  public void setTimespent(Long timespent) {
    this.timespent = timespent;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

}
