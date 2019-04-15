
package org.gooru.dap.jobs.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru Created On 05-Apr-2019
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassJson {

  @JsonProperty("classId")
  private String classId;

  @JsonProperty("courseId")
  private String courseId;

  public ClassJson() {}

  public ClassJson(String classId, String courseId) {
    super();
    this.classId = classId;
    this.courseId = courseId;
  }

  @JsonProperty("classId")
  public String getClassId() {
    return classId;
  }

  @JsonProperty("classId")
  public void setClassId(String classId) {
    this.classId = classId;
  }

  @JsonProperty("courseId")
  public String getCourseId() {
    return courseId;
  }

  @JsonProperty("courseId")
  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

}
