package org.gooru.dap.processors.events.question.timespent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

class UserStatsQuestionTimeSpentHandler implements DBHandler {

    public static final List<String> MANDATORY_FIELDS =
        new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME, EventMessageConstant.RESOURCE_ID,
            EventMessageConstant.USER_ID, EventMessageConstant.METRICS_TIMESPENT));

    private final ProcessorContext context;

    public UserStatsQuestionTimeSpentHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionStatus checkSanity() {
        return validateRequiredFields(MANDATORY_FIELDS);
    }

    @Override
    public Class<? extends Repository> getRepository() {
        return UserStatsQuestionTimeSpentDaoImpl.class;
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }

}
