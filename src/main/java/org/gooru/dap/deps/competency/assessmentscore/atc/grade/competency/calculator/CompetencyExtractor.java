package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.ArrayList;
import java.util.List;

import org.gooru.dap.deps.competency.assessmentscore.atc.competency.CompetencyModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.DomainModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompetencyExtractor {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyExtractor.class);    

    public List<String> getComptencyList(GradeCompetencyModel gradeCompetencyModel) {
        List<DomainModel> domainModels = gradeCompetencyModel.getDomainsOrdered();
        List<String> competencyList = new ArrayList<>();
        List<CompetencyObj> competencyModelList = new ArrayList<>();
        for (DomainModel domainModel : domainModels) {
            List<CompetencyModel> competencyModels = gradeCompetencyModel.getPathForDomain(domainModel.getDomainCode());
            for (CompetencyModel competencyModel : competencyModels) {
                competencyModelList.add(CompetencyObj.fromCompetencyModel(competencyModel));
            }
        }

        for (CompetencyObj cml : competencyModelList) {
        	competencyList.add(cml.getCompetencyCode());
        }
        
        return competencyList;
    }

    private static class CompetencyObj {
        private String competencyCode;
        private String competencyName;
        private String competencyDescription;
        private String competencyStudentDescription;
        private Integer sequence;

        static CompetencyObj fromCompetencyModel(CompetencyModel model) {
            CompetencyObj competencyJson = new CompetencyObj();
            competencyJson.competencyCode = model.getCompetencyCode().getCode();
            competencyJson.competencyName = model.getCompetencyName();
            competencyJson.competencyDescription = model.getCompetencyDescription();
            competencyJson.competencyStudentDescription = model.getCompetencyStudentDescription();
            competencyJson.sequence = model.getSequence();
            return competencyJson;
        }

        public String getCompetencyCode() {
            return competencyCode;
        }

        public void setCompetencyCode(String competencyCode) {
            this.competencyCode = competencyCode;
        }

        public String getCompetencyName() {
            return competencyName;
        }

        public void setCompetencyName(String competencyName) {
            this.competencyName = competencyName;
        }

        public String getCompetencyDescription() {
            return competencyDescription;
        }

        public void setCompetencyDescription(String competencyDescription) {
            this.competencyDescription = competencyDescription;
        }

        public String getCompetencyStudentDescription() {
            return competencyStudentDescription;
        }

        public void setCompetencyStudentDescription(String competencyStudentDescription) {
            this.competencyStudentDescription = competencyStudentDescription;
        }

        public Integer getSequence() {
            return sequence;
        }

        public void setSequence(Integer sequence) {
            this.sequence = sequence;
        }
    }

}
