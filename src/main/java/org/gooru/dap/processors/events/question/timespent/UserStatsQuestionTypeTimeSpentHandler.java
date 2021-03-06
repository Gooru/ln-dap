package org.gooru.dap.processors.events.question.timespent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

class UserStatsQuestionTypeTimeSpentHandler implements DBHandler {

  public static final List<String> MANDATORY_FIELDS =
      new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME,
          EventMessageConstant.USER_ID, EventMessageConstant.METRICS_TIMESPENT));

  private final ProcessorContext context;

  public UserStatsQuestionTypeTimeSpentHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionStatus checkSanity() {
    return validateRequiredFields(MANDATORY_FIELDS);
  }

  @Override
  public Repository getRepository() {
    return new UserStatsQuestionTypeTimeSpentDaoImpl(context);
  }

  @Override
  public ProcessorContext getContext() {
    return this.context;
  }

}
