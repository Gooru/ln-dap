package org.gooru.dap.processors.events;

import org.gooru.dap.processors.Processor;

public final class ProcessorBuilder {
    private ProcessorBuilder() {
        throw new AssertionError();
    }

    public static Processor build(String deploymentName, String eventListenerTopicName, String message) {
        return new EventProcessor(deploymentName, eventListenerTopicName, message);
    }
}
