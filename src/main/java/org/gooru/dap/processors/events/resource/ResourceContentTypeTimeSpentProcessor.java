package org.gooru.dap.processors.events.resource;

import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.AbstractEventProcessor;
import org.gooru.dap.processors.repositories.RepoBuilder;

public class ResourceContentTypeTimeSpentProcessor extends AbstractEventProcessor {

    public ResourceContentTypeTimeSpentProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void processEvent() {
       RepoBuilder.buildResourceRepo(context).resourceContentTypeTimeSpent();
    }

}
