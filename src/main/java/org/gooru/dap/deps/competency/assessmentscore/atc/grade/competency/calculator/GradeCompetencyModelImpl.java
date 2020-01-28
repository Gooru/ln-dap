package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.CompetencyModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.DomainCode;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.DomainModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.SubjectCode;

/**
 * @author ashish.
 */
class GradeCompetencyModelImpl implements GradeCompetencyModel {
  private final SubjectCode subjectCode;
  private final List<DomainModel> domains;
  private final Map<DomainCode, List<CompetencyModel>> domainCodeCompetencyModelMap;

  GradeCompetencyModelImpl(SubjectCode subjectCode, List<DomainModel> domains,
      Map<DomainCode, List<CompetencyModel>> domainCodeCompetencyModelMap) {
    this.subjectCode = subjectCode;
    this.domains = Collections.unmodifiableList(domains);
    this.domainCodeCompetencyModelMap = domainCodeCompetencyModelMap;
  }

  @Override
  public SubjectCode getSubject() {
    return subjectCode;
  }

  @Override
  public List<DomainModel> getDomainsOrdered() {
    return domains;
  }

  @Override
  public List<CompetencyModel> getPathForDomain(DomainCode domainCode) {
    final List<CompetencyModel> result = domainCodeCompetencyModelMap.get(domainCode);
    if (result != null) {
      return Collections.unmodifiableList(result);
    }
    return Collections.emptyList();
  }

}
