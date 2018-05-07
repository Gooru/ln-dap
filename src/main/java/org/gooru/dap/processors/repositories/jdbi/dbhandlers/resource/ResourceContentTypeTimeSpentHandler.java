package org.gooru.dap.processors.repositories.jdbi.dbhandlers.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.dbhandlers.Repository;
import org.gooru.dap.processors.repositories.jdbi.dto.resource.ResourceContentTypeTimeSpentDto;

class ResourceContentTypeTimeSpentHandler implements DBHandler {

    public static final List<String> RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS =
        new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME, EventMessageConstant.METRICS_TIMESPENT,
            EventMessageConstant.CONTENT_TYPE));

    private final ProcessorContext context;

    public ResourceContentTypeTimeSpentHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionStatus checkSanity() {
        return validateRequiredFields(RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS);
    }

    @Override
    public Class<? extends Repository> getRepository() {
        return ResourceContentTypeTimeSpentDto.class;
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }

}
