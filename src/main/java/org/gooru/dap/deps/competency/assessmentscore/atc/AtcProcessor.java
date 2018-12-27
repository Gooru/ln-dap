package org.gooru.dap.deps.competency.assessmentscore.atc;

import java.util.UUID;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.AtcCompute;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.GradeCompetencyStatsModel;
import org.gooru.dap.deps.competency.assessmentscore.atc.compute.GradeCompetencyStatsService;
import org.gooru.dap.deps.competency.assessmentscore.atc.helper.SubjectInferer;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mukul@gooru
 */
public class AtcProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

  private final AssessmentScoreEventMapper assessmentScoreEvent;
  private String classId;
  private String courseId;
  private String userId;
  private String subjectCode;

  private AtcService atcService = new AtcService(DBICreator.getDbiForCoreDS());
  private GradeCompetencyStatsService gradeCompetencyStatsService =
      new GradeCompetencyStatsService(DBICreator.getDbiForDefaultDS());


  public AtcProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  public void compute() {
    try {
      classId = this.assessmentScoreEvent.getContext().getClassId();
      courseId = this.assessmentScoreEvent.getContext().getCourseId();
      userId = this.assessmentScoreEvent.getUserId();

      // Calculate gradeId for this student
      // If the gradeId of the Student is not set, then get the gradeId of the Class
      // If that is also not set then EXIT

      // It is assumed that the Setter system has taken care of the fact that
      // Student grade for this class is always lower than that upper bound of the
      // class.

      Integer gradeId = atcService.fetchGradefromClassMembers(userId, classId);
      if (gradeId == null) {
        gradeId = atcService.fetchGradefromClass(classId, courseId);
      }

      if (gradeId != null) {
        LOGGER.debug("Fetching subject code");
        initializeSubjectCode();
        // If we don't get Subject Code - EXIT
        if (subjectCode != null) {
          // Inject this object for further calculation
          AtcEvent atcEventObject = new AtcEvent(classId, courseId, userId, gradeId, subjectCode);

          AtcCompute atcComputeInstance = AtcCompute.createInstance();
          GradeCompetencyStatsModel gradeCompetencyStats =
              atcComputeInstance.compute(atcEventObject);

          // DEBUG - Convert to Json
          LOGGER.debug(new ObjectMapper().writeValueAsString(gradeCompetencyStats));

          // Persist Aggregated Data into a DB Table
          gradeCompetencyStatsService.insertUserClassCompetencyStats(gradeCompetencyStats);

        } else {
          LOGGER.info("Subject is not set for user" + userId + "at class " + classId
              + ".No further processing will be done!");
          return;
        }
      } else {
        LOGGER.info("Grade is not set for user" + userId + "at class " + classId
            + ".No further processing will be done!");
        return;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return;
    }
  }

  private void initializeSubjectCode() {
    subjectCode = SubjectInferer.build().inferSubjectForCourse(UUID.fromString(courseId));
    if (subjectCode == null) {
      LOGGER.warn("Not able to find subject code for specified course '{}'", courseId);
      throw new IllegalStateException(
          "Not able to find subject code for specified course " + courseId);
    }
  }

}