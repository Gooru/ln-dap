package org.gooru.dap.constants;

/**
 * @author szgooru on 10-Jul-2018
 */
public final class StatusConstants {

  private StatusConstants() {
    throw new AssertionError();
  }

  public final static int NOT_STARTED = 0;
  public final static int IN_PROGRESS = 1;
  public final static int INFERRED = 2;
  public final static int ASSERTED = 3;
  public final static int COMPLETED = 4;
  public final static int MASTERED = 5;
}
