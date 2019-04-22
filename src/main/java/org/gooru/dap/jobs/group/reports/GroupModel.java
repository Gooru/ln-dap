
package org.gooru.dap.jobs.group.reports;

/**
 * @author szgooru Created On 08-Apr-2019
 */
public class GroupModel {
  private Long id;
  private String type;
  private String subType;
  private Long parentId;
  private Long stateId;
  private Long countryId;
  private String tenant;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  @Override
  public String toString() {
    return "GroupModel [id=" + id + ", type=" + type + ", subType=" + subType + ", parentId="
        + parentId + ", stateId=" + stateId + ", countryId=" + countryId + ", tenant=" + tenant
        + "]";
  }
  

}
