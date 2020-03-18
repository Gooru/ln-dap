package org.gooru.dap.deps.competency.preprocessors;

/**
 * @author mukul@gooru
 */
public class DCAContentModel {

  private Integer id;
  private String classId;
  private Boolean allowMasteryAccrual;
  private String contentId;
  private String contentType;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Boolean getAllowMasteryAccrual() {
    return allowMasteryAccrual;
  }

  public void setAllowMasteryAccrual(Boolean allowMasteryAccrual) {
    this.allowMasteryAccrual = allowMasteryAccrual;
  }

  public String getContentId() {
    return contentId;
  }

  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

}
