package org.gooru.dap.processors.events.resource.timespent.usercontenttypeprefs;

/**
 * @author ashish.
 */

class EMACalculator {

  private static final double DEFAULT_ALPHA = 1 / (double) 3;

  private double alpha;

  public EMACalculator(double alpha) {
    this.alpha = alpha;
  }

  public EMACalculator() {
    this.alpha = DEFAULT_ALPHA;
  }

  public double calculateNewEma(Double oldValue, double value) {
    if (oldValue == null || oldValue == 0) {
      return value;
    }
    return oldValue + alpha * (value - oldValue);
  }
}
