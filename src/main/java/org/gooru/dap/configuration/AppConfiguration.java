package org.gooru.dap.configuration;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class is supposed to make use of {@link Config} class and provide the required configuration to caller. The
 * idea of putting this class is to make sure that different hard code keys for configuration are not scattered across
 * different source files, rather they are confined to one class, which is this one.
 *
 * @author ashish on 17/4/18.
 */
public final class AppConfiguration {

    private AppConfiguration() {
        throw new AssertionError();
    }

    public static JsonNode fetchSystemPropsConfig() {
        return Config.getInstance().getRootConfig().get("systemProperties");
    }

    public static JsonNode fetchDataSources() {
        return Config.getInstance().getRootConfig().get("datasources");
    }

    public static JsonNode fetchConsumersToDeploy() {
        return Config.getInstance().getRootConfig().get("deps.to.deploy");
    }

    public static JsonNode fetchConsumerConfigForDeployment() {
        return Config.getInstance().getRootConfig().get("deps.config");
    }

}
