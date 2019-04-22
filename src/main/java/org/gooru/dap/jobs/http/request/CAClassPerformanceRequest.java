
package org.gooru.dap.jobs.http.request;

import java.util.List;

/**
 * @author szgooru Created On 22-Apr-2019
 */
public class CAClassPerformanceRequest {

  private List<String> classIds;

  public List<String> getClassIds() {
    return classIds;
  }

  public void setClassIds(List<String> classIds) {
    this.classIds = classIds;
  }

}
