
package org.gooru.dap.jobs.http.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru Created On 04-Apr-2019
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassPerformanceResponse {

  @JsonProperty("usageData")
  private List<UsageData> usageData;

  @JsonProperty("userId")
  private String userId;

  public ClassPerformanceResponse() {}

  public ClassPerformanceResponse(List<UsageData> usageData, String userId) {
    super();
    this.usageData = usageData;
    this.userId = userId;
  }

  public List<UsageData> getUsageData() {
    return usageData;
  }

  public void setUsageData(List<UsageData> usageData) {
    this.usageData = usageData;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}
