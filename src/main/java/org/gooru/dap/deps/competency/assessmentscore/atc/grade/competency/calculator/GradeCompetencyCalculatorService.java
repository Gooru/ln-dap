package org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator;

import java.util.List;
import java.util.stream.Collectors;

import org.gooru.dap.deps.competency.assessmentscore.atc.competency.Competency;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.CompetencyMap;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.CompetencyCollector;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.DomainCode;
import org.gooru.dap.deps.competency.assessmentscore.atc.competency.SubjectCode;
import org.gooru.dap.deps.competency.assessmentscore.atc.grade.competency.calculator.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GradeCompetencyCalculatorService implements GradeCompetencyCalculator {

    private final DBI defaultDSDbi;
    private final DBI coreDSDbi;
    private GradeCompetencyCalculatorModel model;
    private static final Logger LOGGER = LoggerFactory.getLogger(GradeCompetencyCalculatorService.class);
    private List<String> destinationGutCodes;
    private List<Competency> destinationCompetencies, sourceCompetencies;
    private CompetencyMap destinationCompetencyMap, proficiencyCompetencyMap;
    private TaxonomyDao taxonomyDao;
    private CompetencyCollector gradeCompetencyCollection;

    GradeCompetencyCalculatorService(DBI coreDSDbi, DBI defaultDSDbi) {
        this.defaultDSDbi = defaultDSDbi;
        this.coreDSDbi = coreDSDbi;
    }

    @Override
    public GradeCompetencyModel calculateGradeCompetency(GradeCompetencyCalculatorModel model) {
        this.model = model;
        try {
            return doProcess();
        } catch (RuntimeException re) {
            LOGGER.warn("Not able to get competencies for grade: '{}'", model.getGradeId());
            throw re;
        }
    }

    private GradeCompetencyModel doProcess() {
    	try {
            LOGGER.debug("Fetching destination gut codes");
            fetchDestinationGutCodes();
            LOGGER.debug("Filtering gut codes for competencies");
            filterGutCodesForCompetencyForSpecifiedSubject();
            LOGGER.debug("Creating destination competency map");
            createDestinationCompetencyMap();
            LOGGER.debug("User user proficiency for specified subject and domains");
            fetchUserProficiencyInSpecifiedSubjectAndDomains();
            LOGGER.debug("Creating proficiency competency map");
            createProficiencyCompetencyMap();
            LOGGER.debug("Calculating grade competencies");
            calculateAllGradeCompetencies();
            LOGGER.debug("Building competency route model");
            return new GradeCompetencyModelBuilder(getTaxonomyDao())
                .build(new SubjectCode(model.getSubjectCode()), gradeCompetencyCollection);    		
    	} catch ( IllegalStateException ex) {
    		LOGGER.warn("Caught IllegalStateException ");
    		return null;    		
    	}
    }

    private void calculateAllGradeCompetencies() {
        
    	gradeCompetencyCollection =
                proficiencyCompetencyMap.getCeilingLine().getAllCompetenciesTillCompetencyLine(destinationCompetencyMap.getCeilingLine());
    }

    private void createProficiencyCompetencyMap() {
        proficiencyCompetencyMap = CompetencyMap.build(sourceCompetencies);
    }

    private void fetchUserProficiencyInSpecifiedSubjectAndDomains() {
        List<String> domains =
            destinationCompetencyMap.getDomains().stream().map(DomainCode::getCode).collect(Collectors.toList());
        sourceCompetencies = getTaxonomyDao()
                .fetchProficiencyForUserInSpecifiedSubjectAndDomains(model.getUserId().toString(), model.getSubjectCode(),
                    CollectionUtils.convertToSqlArrayOfString(domains));
    }

    private void createDestinationCompetencyMap() {
        destinationCompetencyMap = CompetencyMap.build(destinationCompetencies);
    }

    private void filterGutCodesForCompetencyForSpecifiedSubject() {
        // Query the db based on gut codes, and create competencies out of it
        destinationCompetencies = getTaxonomyDao()
            .transformGutCodesToCompetency(model.getSubjectCode(), CollectionUtils.convertToSqlArrayOfString(destinationGutCodes));
        if (destinationCompetencies == null || destinationCompetencies.isEmpty()) {
            LOGGER.warn("No destination competencies found after filtering aggregated gut codes for course: '{}'",
                model.getGradeId().toString());
            throw new IllegalStateException("Destination competencies not found for course: " + model.getGradeId());
        }
    }

    private void fetchDestinationGutCodes() {
        CompetencyFetcher competencyFetcher = CompetencyFetcher.build(coreDSDbi);
        LOGGER.info("gradeId " + model.getGradeId() + " subjectCode "+ model.getSubjectCode());
        destinationGutCodes = competencyFetcher.fetchCompetenciesForGrade(model.getGradeId(), model.getSubjectCode());
        if (destinationGutCodes == null || destinationGutCodes.isEmpty()) {
            LOGGER.warn("No aggregated competencies found for course: '{}'", model.getGradeId());
            throw new IllegalStateException("No aggregated competencies found for course: " + model.getGradeId());
        }
    }

    private TaxonomyDao getTaxonomyDao() {
        if (taxonomyDao == null) {
            taxonomyDao = defaultDSDbi.onDemand(TaxonomyDao.class);
        }
        return taxonomyDao;
    }

}
