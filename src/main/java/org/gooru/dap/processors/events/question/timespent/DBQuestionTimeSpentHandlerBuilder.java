package org.gooru.dap.processors.events.question.timespent;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;

public final class DBQuestionTimeSpentHandlerBuilder {

  private DBQuestionTimeSpentHandlerBuilder() {
    throw new AssertionError();
  }

  public static DBHandler buildUserStatsQuestionTimeSpent(ProcessorContext context) {
    return new UserStatsQuestionTimeSpentHandler(context);
  }

  public static DBHandler buildUserStatsQuestionTypeTimeSpent(ProcessorContext context) {
    return new UserStatsQuestionTypeTimeSpentHandler(context);
  }

  public static DBHandler buildQuestionTypeTimeSpent(ProcessorContext context) {
    return new QuestionTypeTimeSpentHandler(context);
  }

  public static DBHandler buildUserStatsCCULCQuestionTimeSpent(ProcessorContext context) {
    return new UserStatsCCULCQuestionTimeSpentHandler(context);
  }
}
