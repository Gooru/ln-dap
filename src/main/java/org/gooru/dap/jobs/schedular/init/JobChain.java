
package org.gooru.dap.jobs.schedular.init;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author mukul@gooru
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"chainId", "config"})
public class JobChain {

  @JsonProperty("chainId")
  private String chainId;
  
  @JsonProperty("config")
  private Config config;

  public JobChain() {}

  public JobChain(String chainId, Config config) {
    super();
    this.chainId = chainId;
    this.config = config;
  }

  @JsonProperty("chainId")
  public String getChainId() {
    return chainId;
  }

  @JsonProperty("chainId")
  public void setChainId(String chainId) {
    this.chainId = chainId;
  }

  public JobChain withChainId(String chainId) {
    this.chainId = chainId;
    return this;
  }

  @JsonProperty("config")
  public Config getConfig() {
    return config;
  }

  @JsonProperty("config")
  public void setConfig(Config config) {
    this.config = config;
  }

  public JobChain withConfig(Config config) {
    this.config = config;
    return this;
  }
}
