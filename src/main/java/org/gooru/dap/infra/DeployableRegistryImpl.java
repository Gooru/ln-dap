package org.gooru.dap.infra;

import java.util.List;

/**
 * This class stores all the deployment names which are specified in JSON files which are meant to
 * be deployed. The caller can use this to ask question about a specific deployment name to
 * ascertain if the deployable is supposed to be deployed based on current config.
 *
 * @author ashish on 17/4/18.
 */
class DeployableRegistryImpl implements DeployableRegistry {

  private final List<String> deploymentDescriptors;

  public DeployableRegistryImpl(List<String> deploymentDescriptors) {
    this.deploymentDescriptors = deploymentDescriptors;
  }

  @Override
  public boolean canDeploy(Deployable deployable) {
    return deploymentDescriptors.contains(deployable.getDeploymentName());
  }
}
