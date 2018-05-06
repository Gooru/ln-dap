package org.gooru.dap.processors.repositories.jdbi.dbhandlers.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.gooru.dap.processors.repositories.jdbi.dto.resource.UserStatsResourceContentTypeTimeSpentDto;

class UserStatsResourceContentTypeTimeSpentHandler implements DBHandler {

    public static final List<String> USER_STATS_RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS =
        new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME,
            EventMessageConstant.USER_ID, EventMessageConstant.METRICS_TIMESPENT, EventMessageConstant.CONTENT_TYPE));

    private final ProcessorContext context;

    public UserStatsResourceContentTypeTimeSpentHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionStatus checkSanity() {
        return validateRequiredFields(USER_STATS_RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS);
    }

    @Override
    public Class<? extends Repository> getRepository() {
        return UserStatsResourceContentTypeTimeSpentDto.class;
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }

}
