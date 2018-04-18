package org.gooru.dap.infra;

import java.util.List;

/**
 * This stores all the deployment names which are specified in JSON files which are meant to be deployed.
 * The caller can use this to ask question about a specific deployment name to ascertain if the deployable is
 * supposed to be deployed based on current config.
 *
 * @author ashish on 17/4/18.
 */

interface DeployableRegistry {

    boolean canDeploy(Deployable deployable);

    static DeployableRegistry build(List<String> deploymentNames) {
        return new DeployableRegistryImpl(deploymentNames);
    }
}
