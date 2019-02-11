package org.gooru.dap.deps.competency.assessmentscore.atc;

import java.util.UUID;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.AtcCompute;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.CompetencyStatsModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.CompetencyStatsService;
import org.gooru.dap.deps.competency.assessmentscore.atc.helper.SubjectFetcher;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mukul@gooru
 */
public class AtcProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(AtcProcessor.class);
  private final AssessmentScoreEventMapper assessmentScoreEvent;
  private String classId;
  private String courseId;
  private String userId;
  private String subjectCode;
  private Integer gradeId;

  private AtcService atcService = new AtcService(DBICreator.getDbiForCoreDS());
  private CompetencyStatsService gradeCompetencyStatsService =
      new CompetencyStatsService(DBICreator.getDbiForDefaultDS());

  public AtcProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  public void compute() {
    try {
      classId = this.assessmentScoreEvent.getContext().getClassId();
      courseId = this.assessmentScoreEvent.getContext().getCourseId();
      userId = this.assessmentScoreEvent.getUserId();
      
      //courseId can be NULL in the incoming event, in which case
      //use courseId from the class table
      if(courseId == null) {
        courseId = atcService.fetchCoursefromClass(classId);        
      }

      // Calculate gradeId for this student
      // If the gradeId of the Student is not set, then get the gradeId of the Class
      // If that is also not set then store the Global Skyline for the user

      gradeId = atcService.fetchGradefromClassMembers(userId, classId);
      if (gradeId == null) {
          gradeId = atcService.fetchGradefromClass(classId, courseId);
      }

      if (gradeId == null || gradeId <= 0) {        
        LOGGER.debug("Fetching subject code");
        initializeSubjectCodefromCourse();

        AtcEvent atcEventObject = new AtcEvent(classId, courseId, userId, null, subjectCode);

        AtcCompute atcComputeInstance = AtcCompute.createInstance(atcEventObject);
        CompetencyStatsModel skylineCompetencyStats = atcComputeInstance.compute(atcEventObject);

        // DEBUG - Convert to Json
        LOGGER.info(new ObjectMapper().writeValueAsString(skylineCompetencyStats));

        // Persist Aggregated Data into a DB Table
        gradeCompetencyStatsService.insertUserClassCompetencyStats(skylineCompetencyStats);

      } else if (gradeId != null && gradeId > 0) {
        
        LOGGER.debug("Fetching subject code");
        initializeSubjectCodefromGrade();
        
        // Inject this object for further calculation
        AtcEvent atcEventObject = new AtcEvent(classId, courseId, userId, gradeId, subjectCode);

        AtcCompute atcComputeInstance = AtcCompute.createInstance(atcEventObject);
        CompetencyStatsModel gradeCompetencyStats = atcComputeInstance.compute(atcEventObject);

        // DEBUG - Convert to Json
        LOGGER.info(new ObjectMapper().writeValueAsString(gradeCompetencyStats));

        // Persist Aggregated Data into a DB Table
        gradeCompetencyStatsService.insertUserClassCompetencyStats(gradeCompetencyStats);

      } 
      else {
        LOGGER.info("No Learner Profile for " + userId + "at class " + classId);;
        return;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return;
    }
  }

  private void initializeSubjectCodefromCourse() {
    subjectCode = SubjectFetcher.build().fetchSubjectFromCourse(UUID.fromString(courseId));
    if (subjectCode == null || subjectCode.isEmpty()) {
      LOGGER.warn("Not able to find subject code for specified course '{}' and class '{}'",
          courseId, classId);
      throw new IllegalStateException(
          "Not able to find subject code for specified course " + courseId);
    }
  }

  private void initializeSubjectCodefromGrade() {
    subjectCode = SubjectFetcher.build().fetchSubjectFromGrade(gradeId);
    if (subjectCode == null || subjectCode.isEmpty()) {
      LOGGER.warn("Not able to find subject code for specified grade '{}' and class '{}'",
          gradeId, classId);
      throw new IllegalStateException(
          "Not able to find subject code for specified course " + gradeId);
    }
  }

}
