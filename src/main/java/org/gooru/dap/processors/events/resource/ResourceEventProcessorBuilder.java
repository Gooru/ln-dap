package org.gooru.dap.processors.events.resource;

import java.util.HashMap;
import java.util.Map;

import org.gooru.dap.processors.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ResourceEventProcessorBuilder {

    DEFAULT("default") {
        private final Logger LOGGER = LoggerFactory.getLogger(ResourceEventProcessorBuilder.class);

        @Override
        public void build(ProcessorContext context) {
            LOGGER.error("Invalid operation type passed in, not able to handle");
            
        }
    },
    RESOURCE_TIMESPENT("resource.timespent") {

        @Override
        public void build(ProcessorContext context) {
            new UserStatsResourceTimeSpentProcessor(context).process();
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

    public abstract void build(ProcessorContext context);
}
