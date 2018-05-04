package org.gooru.dap.processors.events;

import java.util.List;

import org.gooru.dap.processors.ExecutionStatus;
import org.gooru.dap.processors.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResourceEventProcessor extends EventProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceEventProcessor.class);
    private final String message;

    public ResourceEventProcessor(String message) {
        this.message = message;
    }

    @Override
    public void process() {
        try {
            ExecutionStatus executionStatus = validateAndInitialize();
            if (executionStatus.isSuccessFul()) {
               List<Processor> processors =  ResourceEventProcessorBuilder.lookupBuilder(getEventName()).build(createContext());
               processors.forEach(processor -> {
                   processor.process();
               });
            }
        } catch (Throwable e) {
            LOGGER.error("Unhandled exception in processing", e);
        }
    }

    @Override
    String message() {
        return this.message;
    }

}
