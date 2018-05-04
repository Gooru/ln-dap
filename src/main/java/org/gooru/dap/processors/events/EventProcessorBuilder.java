package org.gooru.dap.processors.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gooru.dap.processors.Processor;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.resource.UserStatsResourceTimeSpentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum EventProcessorBuilder {

    DEFAULT("default") {
        private final Logger LOGGER = LoggerFactory.getLogger(EventProcessorBuilder.class);
        private List<Processor> processors = new ArrayList<>();
        @Override
        public List<Processor> build(ProcessorContext context) {
            LOGGER.error("Invalid operation type passed in, not able to handle");
            return processors;     
        }
    },
    RESOURCE_TIMESPENT("resource.timespent") {
        private List<Processor> processors = new ArrayList<>();
        @Override
        public List<Processor> build(ProcessorContext context) {
            processors.add(new UserStatsResourceTimeSpentProcessor(context));
            
            return processors;
        }
    };

    private String name;

    EventProcessorBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, EventProcessorBuilder> LOOKUP = new HashMap<>();

    static {
        for (EventProcessorBuilder builder : values()) {
            LOOKUP.put(builder.getName(), builder);
        }
    }

    public static EventProcessorBuilder lookupBuilder(String name) {
        EventProcessorBuilder builder = LOOKUP.get(name);
        if (builder == null) {
            return DEFAULT;
        }
        return builder;
    }

    public abstract List<Processor> build(ProcessorContext context);
}
