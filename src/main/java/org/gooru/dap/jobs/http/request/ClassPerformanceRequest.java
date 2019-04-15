
package org.gooru.dap.jobs.http.request;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru Created On 04-Apr-2019
 */
public class ClassPerformanceRequest {

  @JsonProperty("classes")
  private List<ClassJson> classes;

  public ClassPerformanceRequest() {}

  public ClassPerformanceRequest(List<ClassJson> classes) {
    this.classes = classes;
  }

  @JsonProperty("classes")
  public List<ClassJson> getClasses() {
    return classes;
  }

  @JsonProperty("classes")
  public void setClasses(List<ClassJson> classes) {
    this.classes = classes;
  }

}
