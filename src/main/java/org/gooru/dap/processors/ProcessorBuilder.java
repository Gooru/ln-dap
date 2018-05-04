package org.gooru.dap.processors;

public final class ProcessorBuilder {

    private ProcessorBuilder() {
        throw new AssertionError();
    }

    public static Processor build(String message) {
        return new EventProcessor(message);
    }
}
