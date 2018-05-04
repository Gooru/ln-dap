package org.gooru.dap.processors.events;

import org.gooru.dap.processors.Processor;

public final class ProcessorBuilder {

    private ProcessorBuilder() {
        throw new AssertionError();
    }

    public static Processor buildResourceEventProcessor(String message) {
        return new ResourceEventProcessor(message);
    }
}
