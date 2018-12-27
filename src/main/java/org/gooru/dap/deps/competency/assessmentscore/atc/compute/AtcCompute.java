package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcEvent;

/**
 * @author mukul@gooru
 */
public interface AtcCompute {

  GradeCompetencyStatsModel compute(AtcEvent scoreObject);

  static AtcCompute createInstance() {
    return new AtcComputeImpl(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForCoreDS());
  }
}
