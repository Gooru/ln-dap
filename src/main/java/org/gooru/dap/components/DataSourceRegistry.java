package org.gooru.dap.components;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.gooru.dap.configuration.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author ashish on 20/2/18.
 */
public final class DataSourceRegistry implements FinalizationAwareComponent, InitializationAwareComponent {

    private static final String DEFAULT_DATA_SOURCE = "defaultDataSource";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceRegistry.class);
    private final Map<String, DataSource> registry = new HashMap<>();
    private volatile boolean initialized;

    private DataSourceRegistry() {
    }

    public static DataSourceRegistry getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void initializeComponent() {
        LOGGER.debug("Initialization called upon.");
        if (!this.initialized) {
            LOGGER.debug("May have to do initialization");
            synchronized (Holder.INSTANCE) {
                LOGGER.debug("Will initialize after double checking");
                if (!this.initialized) {
                    LOGGER.debug("Double checking done. Initializing now.");
                    JsonNode config = AppConfiguration.fetchDataSources();
                    if (config == null || !config.isContainerNode()) {
                        throw new IllegalStateException("No valid configuaration for datasouces found");
                    }
                    for (Iterator<String> it = config.fieldNames(); it.hasNext(); ) {
                        String datasource = it.next();
                        JsonNode dbConfig = config.get(datasource);
                        if (dbConfig != null) {
                            DataSource ds = DatasourceHelper.initializeDataSource(dbConfig);
                            this.registry.put(datasource, ds);
                        }
                    }
                    this.initialized = true;
                }
            }
        }
    }

    public DataSource getDefaultDataSource() {
        return this.registry.get(DEFAULT_DATA_SOURCE);
    }

    public DataSource getDataSourceByName(String name) {
        if (name != null) {
            return this.registry.get(name);
        }
        return null;
    }

    @Override
    public void finalizeComponent() {
        registry.entrySet().forEach(entry -> {
            DataSource ds = entry.getValue();
            if (ds != null) {
                if (ds instanceof HikariDataSource) {
                    ((HikariDataSource) ds).close();
                }
            }
        });
    }

    private static final class Holder {
        private static final DataSourceRegistry INSTANCE = new DataSourceRegistry();
    }

}
