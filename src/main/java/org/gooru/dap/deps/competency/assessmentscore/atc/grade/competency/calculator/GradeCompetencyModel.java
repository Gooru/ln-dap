package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.List;

import org.gooru.dap.deps.competency.assessmentscore.atc.competency.CompetencyModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.DomainCode;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.DomainModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.SubjectCode;

/**
 * @author ashish.
 */
public interface GradeCompetencyModel {

    SubjectCode getSubject();

    List<DomainModel> getDomainsOrdered();

    List<CompetencyModel> getPathForDomain(DomainCode domainCode);
	
}
