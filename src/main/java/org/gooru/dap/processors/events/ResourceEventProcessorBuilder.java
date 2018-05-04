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

public enum ResourceEventProcessorBuilder {

    DEFAULT("default") {
        private final Logger LOGGER = LoggerFactory.getLogger(ResourceEventProcessorBuilder.class);
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

    ResourceEventProcessorBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, ResourceEventProcessorBuilder> LOOKUP = new HashMap<>();

    static {
        for (ResourceEventProcessorBuilder builder : values()) {
            LOOKUP.put(builder.getName(), builder);
        }
    }

    public static ResourceEventProcessorBuilder lookupBuilder(String name) {
        ResourceEventProcessorBuilder builder = LOOKUP.get(name);
        if (builder == null) {
            return DEFAULT;
        }
        return builder;
    }

    public abstract List<Processor> build(ProcessorContext context);
}
