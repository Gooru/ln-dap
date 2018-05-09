package org.gooru.dap.processors.events.resource.timespent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gooru.dap.constants.EventMessageConstant;
import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.repositories.jdbi.DBHandler;
import org.gooru.dap.processors.repositories.jdbi.Repository;

class ResourceContentTypeTimeSpentHandler implements DBHandler {

    public static final List<String> RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS =
        new ArrayList<>(Arrays.asList(EventMessageConstant.ACTIVITY_TIME, EventMessageConstant.METRICS_TIMESPENT));

    private final ProcessorContext context;

    public ResourceContentTypeTimeSpentHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionStatus checkSanity() {
        return validateRequiredFields(RESOURCE_CONTENT_TYPE_MANDATORY_FIELDS);
    }

    @Override
    public Repository getRepository() {
        return new ResourceContentTypeTimeSpentDaoImpl(context);
    }

    @Override
    public ProcessorContext getContext() {
        return this.context;
    }

}
