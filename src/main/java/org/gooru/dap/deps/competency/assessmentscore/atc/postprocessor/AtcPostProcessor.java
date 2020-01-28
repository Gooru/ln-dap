package org.gooru.dap.deps.competency.assessmentscore.atc.postprocessor;

import java.util.List;
import java.util.UUID;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.assessmentscore.atc.AtcEvent;
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
public class AtcPostProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(AtcPostProcessor.class);
  private final AssessmentScoreEventMapper assessmentScoreEvent;
  private String classId;
  private String courseId;
  private String subjectCode;
  private Integer gradeId;

  private AtcPostProcessorCoreService atcPostProcessorCoreService =
      new AtcPostProcessorCoreService(DBICreator.getDbiForCoreDS());
  private AtcPostProcessorService atcPostProcessorService =
      new AtcPostProcessorService(DBICreator.getDbiForDefaultDS());
  private CompetencyStatsService gradeCompetencyStatsService =
      new CompetencyStatsService(DBICreator.getDbiForDefaultDS());

  public AtcPostProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  public void compute() {
    try {
      classId = this.assessmentScoreEvent.getContext().getClassId();
      courseId = this.assessmentScoreEvent.getContext().getCourseId();
      // userId = this.assessmentScoreEvent.getUserId();

      // courseId can be NULL in the incoming event, in which case
      // use courseId from the class table
      if (courseId == null) {
        courseId = atcPostProcessorCoreService.fetchCoursefromClass(classId);
      }

      // Fetch ClassMembers
      List<String> classMembers = atcPostProcessorCoreService.fetchClassMembers(classId);

      // Fetch Active ClassMembers from DAP
      List<String> activeClassMembers =
          atcPostProcessorService.fetchActiveClassMembers(classId.toString(), courseId.toString());

      if (classMembers != null && !classMembers.isEmpty() && activeClassMembers != null) {
        classMembers.removeAll(activeClassMembers);
      }

      if (!classMembers.isEmpty()) {
        for (String member : classMembers) {
          LOGGER.info("Seems like student " + member
              + " has not studied anything yet! Updating only Total Competencies.");

          // Calculate gradeId for this student
          // If the gradeId of the Student is not set, then get the
          // gradeId of the Class
          // If that is also not set then store the Global Skyline for
          // the user

          gradeId = atcPostProcessorCoreService.fetchGradefromClassMembers(member, classId);
          if (gradeId == null) {
            gradeId = atcPostProcessorCoreService.fetchGradefromClass(classId, courseId);
          }

          if (gradeId == null || gradeId <= 0) {
            LOGGER.debug("Fetching subject code");
            initializeSubjectCodefromCourse();

            AtcEvent atcEventObject = new AtcEvent(classId, courseId, member, null, subjectCode);

            AtcCompute atcComputeInstance = AtcCompute.createInstance(atcEventObject);
            CompetencyStatsModel skylineCompetencyStats =
                atcComputeInstance.compute(atcEventObject);

            // DEBUG - Convert to Json
            LOGGER.info(new ObjectMapper().writeValueAsString(skylineCompetencyStats));

            // Persist Aggregated Data into a DB Table
            gradeCompetencyStatsService.insertUserClassCompetencyStats(skylineCompetencyStats);

          } else if (gradeId != null && gradeId > 0) {

            LOGGER.debug("Fetching subject code");
            initializeSubjectCodefromGrade();

            // Inject this object for further calculation
            AtcEvent atcEventObject = new AtcEvent(classId, courseId, member, gradeId, subjectCode);

            AtcCompute atcComputeInstance = AtcCompute.createInstance(atcEventObject);
            CompetencyStatsModel gradeCompetencyStats = atcComputeInstance.compute(atcEventObject);

            // DEBUG - Convert to Json
            LOGGER.info(new ObjectMapper().writeValueAsString(gradeCompetencyStats));

            // Persist Aggregated Data into a DB Table
            gradeCompetencyStatsService.insertUserClassCompetencyStats(gradeCompetencyStats);

          } else {
            LOGGER.info("No Learner Profile for " + member + "at class " + classId);;
            return;
          }

        }

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
      LOGGER.warn("Not able to find subject code for specified grade '{}' and class '{}'", gradeId,
          classId);
      throw new IllegalStateException(
          "Not able to find subject code for specified course " + gradeId);
    }
  }

}
