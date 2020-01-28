
package org.gooru.dap.jobs.group.reports;

/**
 * @author szgooru Created On 09-Dec-2019
 */
public class ClassModel {

  private String id;
  private String title;
  private Long gradeCurrent;
  private String subject;
  private String framework;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getGradeCurrent() {
    return gradeCurrent;
  }

  public void setGradeCurrent(Long gradeCurrent) {
    this.gradeCurrent = gradeCurrent;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getFramework() {
    return framework;
  }

  public void setFramework(String framework) {
    this.framework = framework;
  }

}
