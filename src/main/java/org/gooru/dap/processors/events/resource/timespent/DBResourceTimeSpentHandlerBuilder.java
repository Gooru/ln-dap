package org.gooru.dap.processors.events.resource.timespent;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;

public final class DBResourceTimeSpentHandlerBuilder {

  private DBResourceTimeSpentHandlerBuilder() {
    throw new AssertionError();
  }

  public static DBHandler buildUserStatsResourceTimeSpent(ProcessorContext context) {
    return new UserStatsResourceTimeSpentHandler(context);
  }

  public static DBHandler buildUserStatsResourceContentTypeTimeSpent(ProcessorContext context) {
    return new UserStatsResourceContentTypeTimeSpentHandler(context);
  }

  public static DBHandler buildResourceContentTypeTimeSpent(ProcessorContext context) {
    return new ResourceContentTypeTimeSpentHandler(context);
  }

  public static DBHandler buildUserStatsOriginalResourceTimeSpent(ProcessorContext context) {
    return new UserStatsOriginalResourceTimeSpentHandler(context);
  }

  public static DBHandler buildUserStatsCCULCResourceTimeSpent(ProcessorContext context) {
    return new UserStatsCCULCResourceTimeSpentHandler(context);
  }
}
