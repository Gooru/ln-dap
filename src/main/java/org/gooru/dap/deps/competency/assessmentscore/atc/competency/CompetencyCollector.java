package org.gooru.dap.deps.competency.assessmentscore.atc.competency;

import java.util.List;

public interface CompetencyCollector {

  /**
   * Get the domains present in this route
   *
   * @return List of domains in no predefined order
   */
  List<DomainCode> getDomains();

  /**
   * Gets the competency path for the specified domain.
   *
   * @param domainCode the domain for which competency path needs to be fetched
   * @return Competency path
   */
  CompetencyPath getPathForDomain(DomainCode domainCode);

  static CompetencyCollector build(CompetencyLine sourceLine, CompetencyLine destinationLine) {
    return new CompetencyCollectorImpl(sourceLine, destinationLine);
  }

}
