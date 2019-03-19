package org.gooru.dap.deps.competency.processors;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.assessmentscore.content.ContentCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyEvidenceProcessor;
import org.gooru.dap.deps.competency.assessmentscore.learnerprofile.LearnerProfileCompetencyStatusProcessor;
import org.gooru.dap.deps.competency.common.CompetencyAssessmentService;
import org.gooru.dap.deps.competency.common.CompetencyUtils;
import org.gooru.dap.deps.competency.db.mapper.AssessmentCompetency;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 14-May-2018
 */
public class AssessmentScoreEventProcessor implements EventProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);
  public static final String COMPETENCY_MASTERY = "competencyMastery";
  public static final String SYSTEM_PATHTYPE = "system";

  private final AssessmentScoreEventMapper assessmentScoreEvent;
  private CompetencyAssessmentService service =
      new CompetencyAssessmentService(DBICreator.getDbiForCoreDS());

  public AssessmentScoreEventProcessor(AssessmentScoreEventMapper assessmentScoreEvent) {
    this.assessmentScoreEvent = assessmentScoreEvent;
  }

  @Override
  public void process() {
    try {
      String eventName = this.assessmentScoreEvent.getEventName();
      LOGGER.debug("processing event: {}", eventName);
      switch (eventName) {
        case "usage.assessment.score":
          processAssessmentScore();
          break;

        default:
          LOGGER.warn("invalid event passed in");
          return;
      }
    } catch (Throwable t) {
      LOGGER.error("exception while processing event", t);
      return;
    }
  }

  /*
   * if score >= 80 if Signature assessment Insert or Update learner_profile_competency_status to
   * Mastered if already not mastered Insert or Update learner_profile_competency_evidence with
   * multiple evidence else Insert or Update learner_profile_competency_status to Completed Insert
   * or Update learner_profile_competency_evidence Insert or Update content_competency_status to
   * Completed Insert or Update content_competency_evidence else Insert
   * learner_profile_competency_status to If already NOT completed or mastered Insert
   * content_competency_status to in_progress if already not completed or mastered
   * 
   * Update - 01/08/2018 : Lookup for signature gut codes of assessment only if path id is present
   * in the event. Else skip the signature items lookup. If signature assessment, then no need to
   * use the competencies tagged with the assessment to mark the status. Just use gut codes from
   * signature_items table to be mark the status.
   */
  private void processAssessmentScore() {
    LOGGER.debug("assessment score event processing start");
    String collectionId = this.assessmentScoreEvent.getCollectionId();

    // Fetch competencies tagged with assessment
    LOGGER.debug("fetching competency for assessment: '{}'", collectionId);
    AssessmentCompetency competency = service.getAssessmentCompetency(collectionId);

    Long pathId = this.assessmentScoreEvent.getContext().getPathId();
    LOGGER.debug("pathId value received:{}", pathId);
    if (pathId == null || pathId == 0) {
      String contentSource = this.assessmentScoreEvent.getContext().getContentSource();
      if (contentSource != null && contentSource.equalsIgnoreCase(COMPETENCY_MASTERY)) {
        // (contentSource = competency_mastery) indicates that student has played a Standalone
        // Signature Assessment by inspecting
        // competencies. Since this is not a suggestion to the Student, path_id will be null or 0
        LOGGER.debug("Inferred as Standalone Signature Assessment Play");

        // Fetch signature gut codes of the assessment
        final List<String> signatureGutCodes =
            service.fetchSignatureAssessmentGutCodes(collectionId);
        LOGGER.debug("'{}' gut codes found in signature assessment for '{}'",
            signatureGutCodes.size(), collectionId);

        // For every gut code of the signature item, persist status
        signatureGutCodes.forEach(gc -> {
          LOGGER.debug("persisting LP competency status for signature competency:{}", gc);
          new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gc, true).process();
          new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gc, true).process();
        });
      } else {
        LOGGER.debug("No pathId present, executing logic for non signature items");

        JsonNode taxonomy = competency.getTaxonomy();
        if (taxonomy != null && taxonomy.size() > 0) {
          // Fetch gut code mapping of the competency
          Map<String, String> fwToGutCodeMapping = service
              .getGutCodeMapping(CompetencyUtils.toPostgresArrayString(taxonomy.fieldNames()));

          Iterator<String> fields = taxonomy.fieldNames();
          while (fields.hasNext()) {
            String tc = fields.next();
            String gc = fwToGutCodeMapping.get(tc);

            new ContentCompetencyStatusProcessor(assessmentScoreEvent, tc).process();
            new ContentCompetencyEvidenceProcessor(assessmentScoreEvent, tc,
                (gc != null) ? gc : null).process();
          }
        } else {
          LOGGER.debug(
              "no taxonomy tags are applied to assessment, skipping content competency status updates");
        }

        List<String> gutCodes = competency.getGutCodes();
        if (gutCodes != null && !gutCodes.isEmpty()) {
          gutCodes.forEach(gc -> {
            // Always update the status and evidence of the learner profile, logic for
            // mastery and completed will be in respective processors
            new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gc, false).process();
            new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gc, false)
                .process();
          });
        } else {
          LOGGER.debug(
              "no gut codes found for the assessment, skipping leaner profile status updates");
        }
      }
    } else {
      // Update - 18-Mar-2019: Mastery should be only in context where path type is system, for rest
      // of the path type it should consider for completion only.
      if (!this.assessmentScoreEvent.getContext().getPathType().equalsIgnoreCase(SYSTEM_PATHTYPE)) {
        // Suggestions from route0 will not be treated as Signature Assessments
        LOGGER.debug(
            "pathId is present and pathType is route0, executing logic for non signature items");

        JsonNode taxonomy = competency.getTaxonomy();
        if (taxonomy != null && taxonomy.size() > 0) {
          // Fetch gut code mapping of the competency
          Map<String, String> fwToGutCodeMapping = service
              .getGutCodeMapping(CompetencyUtils.toPostgresArrayString(taxonomy.fieldNames()));

          Iterator<String> fields = taxonomy.fieldNames();
          while (fields.hasNext()) {
            String tc = fields.next();
            String gc = fwToGutCodeMapping.get(tc);

            new ContentCompetencyStatusProcessor(assessmentScoreEvent, tc).process();
            new ContentCompetencyEvidenceProcessor(assessmentScoreEvent, tc,
                (gc != null) ? gc : null).process();
          }
        } else {
          LOGGER.debug(
              "no taxonomy tags are applied to assessment, skipping content competency status updates");
        }

        List<String> gutCodes = competency.getGutCodes();
        if (gutCodes != null && !gutCodes.isEmpty()) {
          gutCodes.forEach(gc -> {
            // Always update the status and evidence of the learner profile, logic for
            // mastery and completed will be in respective processors
            new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gc, false).process();
            new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gc, false)
                .process();
          });
        } else {
          LOGGER.debug(
              "no gut codes found for the assessment, skipping leaner profile status updates");
        }
      } else {
        // Signature Assessments will be suggested ONLY by (pathType = system) and not
        // by (pathType = route0 and teacher)
        LOGGER.debug("pathId value present, executing logic for signature items");

        // Fetch signature gut codes of the assessment
        final List<String> signatureGutCodes =
            service.fetchSignatureAssessmentGutCodes(collectionId);
        LOGGER.debug("'{}' gut codes found in signature assessment for '{}'",
            signatureGutCodes.size(), collectionId);

        // For every gut code of the signature item, persist status
        signatureGutCodes.forEach(gc -> {
          LOGGER.debug("persisting LP competency status for signature competency:{}", gc);
          new LearnerProfileCompetencyStatusProcessor(assessmentScoreEvent, gc, true).process();
          new LearnerProfileCompetencyEvidenceProcessor(assessmentScoreEvent, gc, true).process();
        });
      }
    }
  }
}
