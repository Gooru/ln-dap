package org.gooru.dap.processors.events.resource.timespent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

class UserStatsResourceContentTypeTimeSpentHandler implements DBHandler {

    public static final List<String> USER_STATS_RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS =
        new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME, EventMessageConstant.USER_ID,
            EventMessageConstant.METRICS_TIMESPENT));

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
        return UserStatsResourceContentTypeTimeSpentDaoImpl.class;
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }

}
