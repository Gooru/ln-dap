package org.gooru.dap.deps.competency.db.mapper;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 08-May-2018
 */
public class AssessmentCompetency {

  private JsonNode taxonomy;
  private List<String> gutCodes;

  public JsonNode getTaxonomy() {
    return taxonomy;
  }

  public void setTaxonomy(JsonNode taxonomy) {
    this.taxonomy = taxonomy;
  }

  public List<String> getGutCodes() {
    return gutCodes;
  }

  public void setGutCodes(List<String> gutCodes) {
    this.gutCodes = gutCodes;
  }
}
