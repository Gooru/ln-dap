package org.gooru.dap.processors.repositories.jdbi.common.Dao;

public class ContentBean {

  private String originalContentId;

  private String contentType;

  public String getOriginalContentId() {
    return originalContentId;
  }

  public void setOriginalContentId(String originalContentId) {
    this.originalContentId = originalContentId;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

}
