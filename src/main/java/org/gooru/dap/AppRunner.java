package org.gooru.dap;

import java.util.Iterator;
import java.util.Map;

import org.gooru.dap.components.AppInitializer;
import org.gooru.dap.configuration.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * This is the main class to bootstrap the whole application, deploy different listeners and let them work.
 *
 * @author ashish on 16/4/18.
 */
public class AppRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);

    public static void main(String[] args) {
        new AppRunner().run();
    }

    private void run() {
        initializeApplication();
    }

    private void setupSystemProperties() {
        JsonNode systemPropsConfig = AppConfiguration.fetchSystemPropsConfig();
        for (Iterator<Map.Entry<String, JsonNode>> it = systemPropsConfig.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> property = it.next();
            final String propValue = property.getValue().textValue();
            LOGGER.debug("Property: '{}' is set to value '{}'", property.getKey(), propValue);
            if (propValue != null) {
                System.setProperty(property.getKey(), propValue);
            } else {
                throw new IllegalStateException("Invalid system property key value in config");
            }
        }
    }

    private void setupLoggerMachinery() {
        String logbackFile = System.getProperty("logback.configurationFile");
        if (logbackFile != null && !logbackFile.isEmpty()) {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(context);
                context.reset();
                configurator.doConfigure(logbackFile);
            } catch (JoranException je) {
                StatusPrinter.printInCaseOfErrorsOrWarnings(context);
                throw new IllegalStateException("Error while initialising Logger machinery");
            }
        } else {
            LOGGER.warn("Not able to find logback config file");
            throw new IllegalArgumentException("Invalid logback config file");
        }

    }

    private void initializeApplication() {
        setupSystemProperties();
        setupLoggerMachinery();
        AppInitializer.initializeApp();
    }

    private void finalizeApplication() {
        // NO OP right now
    }

    private void deployListeners() {

    }

}
