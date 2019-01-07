package org.gooru.dap.deps.competency.assessmentscore.atc.competency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ashish.
 */
class CompetencyLineImpl implements CompetencyLine {

  private final List<DomainCode> domains;
  private final Map<DomainCode, Competency> domainCodeCompetencyMap;

  CompetencyLineImpl(CompetencyMap competencyMap, boolean ceiling) {
    Objects.requireNonNull(competencyMap);

    domains = Collections.unmodifiableList(new ArrayList<>(competencyMap.getDomains()));
    domainCodeCompetencyMap = new HashMap<>();

    for (DomainCode domainCode : domains) {
      List<Competency> competencies = competencyMap.getCompetenciesForDomain(domainCode);
      if (ceiling) {
        // Mukul
        domainCodeCompetencyMap.put(domainCode, competencies.get(competencies.size() - 1));
      } else {
        domainCodeCompetencyMap.put(domainCode, competencies.get(0));
      }
    }

  }

  @Override
  public List<DomainCode> getDomains() {
    return domains;
  }

  @Override
  public Competency getCompetencyForDomain(DomainCode domainCode) {
    if (domainCode != null) {
      return domainCodeCompetencyMap.get(domainCode);
    }
    return null;
  }

  @Override
  public CompetencyCollector getAllCompetenciesTillCompetencyLine(CompetencyLine competencyLine) {
    if (competencyLine != null) {
      return CompetencyCollector.build(this, competencyLine);
    }
    return null;
  }

  @Override
  public String toString() {
    return "CompetencyLine{" + "domains=" + domains + ", domainCodeCompetencyMap="
        + domainCodeCompetencyMap + '}';
  }
}
