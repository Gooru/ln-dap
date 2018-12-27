package org.gooru.dap.processors.repositories.jdbi;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.question.timespent.DBQuestionTimeSpentHandlerBuilder;
import org.gooru.dap.processors.repositories.QuestionTimeSpentRepo;
import org.gooru.dap.processors.repositories.jdbi.transactions.TransactionExecutor;

public class JdbiQuestionTimeSpentRepo implements QuestionTimeSpentRepo {

  private final ProcessorContext context;

  public JdbiQuestionTimeSpentRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public void userStatsQuestionTimeSpent() {
    TransactionExecutor
        .execute(DBQuestionTimeSpentHandlerBuilder.buildUserStatsQuestionTimeSpent(context));
  }

  @Override
  public void userStatsQuestionTypeTimeSpent() {
    TransactionExecutor
        .execute(DBQuestionTimeSpentHandlerBuilder.buildUserStatsQuestionTypeTimeSpent(context));
  }

  @Override
  public void questionTypeTimeSpent() {
    TransactionExecutor
        .execute(DBQuestionTimeSpentHandlerBuilder.buildQuestionTypeTimeSpent(context));
  }

  @Override
  public void userStatsCCULCQuestionTimeSpent() {
    TransactionExecutor
        .execute(DBQuestionTimeSpentHandlerBuilder.buildUserStatsCCULCQuestionTimeSpent(context));

  }

}
