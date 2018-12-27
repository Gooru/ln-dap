package org.gooru.dap.processors.events.question.timespent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

class UserStatsCCULCQuestionTimeSpentHandler implements DBHandler {

  public static final List<String> MANDATORY_FIELDS =
      new ArrayList<>(Arrays.asList(EventMessageConstant.RESOURCE_ID, EventMessageConstant.USER_ID,
          EventMessageConstant.CTX_COURSE_ID, EventMessageConstant.CTX_UNIT_ID,
          EventMessageConstant.CTX_LESSON_ID, EventMessageConstant.CTX_COLLECTION_ID,
          EventMessageConstant.CTX_COLLECTION_TYPE, EventMessageConstant.METRICS_TIMESPENT));

  private final ProcessorContext context;

  public UserStatsCCULCQuestionTimeSpentHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionStatus checkSanity() {
    return validateRequiredFields(MANDATORY_FIELDS);
  }

  @Override
  public Repository getRepository() {
    return new UserStatsCCULCQuestionTimeSpentDaoImpl(context);
  }

  @Override
  public ProcessorContext getContext() {
    return this.context;
  }

}
