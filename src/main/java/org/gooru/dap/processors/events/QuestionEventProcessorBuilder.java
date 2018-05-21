package org.gooru.dap.processors.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gooru.dap.processors.Processor;
import org.gooru.dap.processors.ProcessorContext;
import org.gooru.dap.processors.events.question.timespent.QuestionTypeTimeSpentProcessor;
import org.gooru.dap.processors.events.question.timespent.UserStatsCCULCQuestionTimeSpentProcessor;
import org.gooru.dap.processors.events.question.timespent.UserStatsQuestionTimeSpentProcessor;
import org.gooru.dap.processors.events.question.timespent.UserStatsQuestionTypeTimeSpentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum QuestionEventProcessorBuilder {

    DEFAULT("default") {
        private final Logger LOGGER = LoggerFactory.getLogger(QuestionEventProcessorBuilder.class);
        private List<Processor> processors = new ArrayList<>();

        @Override
        public List<Processor> build(ProcessorContext context) {
            LOGGER.error("Invalid operation type passed in, not able to handle");
            return processors;
        }
    },
    QUESTION_TIMESPENT("org.gooru.da.sink.gep.question.timespent") {
        
        @Override
        public List<Processor> build(ProcessorContext context) {
            final List<Processor> processors = new ArrayList<>();
            processors.add(new UserStatsQuestionTimeSpentProcessor(context));
            processors.add(new UserStatsQuestionTypeTimeSpentProcessor(context));
            processors.add(new QuestionTypeTimeSpentProcessor(context));
            processors.add(new UserStatsCCULCQuestionTimeSpentProcessor(context));
            return processors;
        }
    };

    private String name;

    QuestionEventProcessorBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, QuestionEventProcessorBuilder> LOOKUP = new HashMap<>();

    static {
        for (QuestionEventProcessorBuilder builder : values()) {
            LOOKUP.put(builder.getName(), builder);
        }
    }

    public static QuestionEventProcessorBuilder lookupBuilder(String name) {
        QuestionEventProcessorBuilder builder = LOOKUP.get(name);
        if (builder == null) {
            return DEFAULT;
        }
        return builder;
    }

    public abstract List<Processor> build(ProcessorContext context);
}
