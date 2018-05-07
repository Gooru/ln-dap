package org.gooru.dap.processors.events.resource.timespent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

class UserStatsOriginalResourceTimeSpentHandler implements DBHandler {

    public static final List<String> USER_STATS_ORIGINAL_RESOURCE_MANDATORY_FIELDS =
        new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME, EventMessageConstant.RESOURCE_ID,
            EventMessageConstant.USER_ID, EventMessageConstant.METRICS_TIMESPENT));

    private final ProcessorContext context;

    public UserStatsOriginalResourceTimeSpentHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionStatus checkSanity() {
        return validateRequiredFields(USER_STATS_ORIGINAL_RESOURCE_MANDATORY_FIELDS);
    }

    @Override
    public Class<? extends Repository> getRepository() {
        return UserStatsOriginalResourceTimeSpentDto.class;
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }

}
