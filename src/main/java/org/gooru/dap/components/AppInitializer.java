package org.gooru.dap.components;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ashish on 17/4/18.
 */
public final class AppInitializer {

    private AppInitializer() {
        throw new AssertionError();
    }

    private static final List<InitializationAwareComponent> initializers = new ArrayList<>();

    static {
        initializers.add(DataSourceRegistry.getInstance());
        initializers.add(UtilityManager.getInstance());
    }

    public static void initializeApp() {
        for (InitializationAwareComponent initializer : initializers) {
            initializer.initializeComponent();
        }

    }
}
