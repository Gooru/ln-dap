package org.gooru.dap.components;

/**
 * @author ashish on 17/4/18. This is a manager class to initialize the utilities, Utilities
 *         initialized may depend on the DB or application state. Thus their initialization sequence
 *         may matter. It is advisable to keep the utility initialization for last.
 */
public final class UtilityManager
    implements InitializationAwareComponent, FinalizationAwareComponent {
  private static final UtilityManager ourInstance = new UtilityManager();

  static UtilityManager getInstance() {
    return ourInstance;
  }

  private UtilityManager() {}

  @Override
  public void finalizeComponent() {

  }

  @Override
  public void initializeComponent() {
    // No op
  }
}
