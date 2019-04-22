package org.gooru.dap.deps.group.processors;

/**
 * @author gooru on 10-May-2018
 */
public class ResultObject {

  private Double score;

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "ResultMapper [score=" + score + "]";
  }

}
