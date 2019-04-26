
package org.gooru.dap.jobs.group.reports.timespent;

/**
 * @author szgooru Created On 25-Apr-2019
 */
public class CollectionTimespentModel {

  private String classId;
  private Long timespent;
  private String contentSource;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Long getTimespent() {
    return timespent;
  }

  public void setTimespent(Long timespent) {
    this.timespent = timespent;
  }

  public String getContentSource() {
    return contentSource;
  }

  public void setContentSource(String contentSource) {
    this.contentSource = contentSource;
  }

}
