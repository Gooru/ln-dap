
package org.gooru.dap.deps.group.dbhelpers;

/**
 * @author szgooru Created On 04-Apr-2019
 */
public class GroupReortsAggregationQueueModel {
  private String classId;
  private String courseId;
  private String contentSource;
  private String tenant;
  private String status;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }
  
  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }
  
  public String getContentSource() {
    return contentSource;
  }

  public void setContentSource(String contentSource) {
    this.contentSource = contentSource;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  
}
