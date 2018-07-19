package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import java.util.regex.Pattern;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.constants.StatusConstants;
import org.gooru.dap.deps.competency.CompetencyConstants;
import org.gooru.dap.deps.competency.events.mapper.AssessmentScoreEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 14-May-2018
 */
public class LearnerProfileCompetencyEvidenceProcessor {

	// TODO: Move this to the config
	private final double MASTERY_SCORE = 80.00;
	private final static Pattern HYPHEN_PATTERN = Pattern.compile("-");

	private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

	private final AssessmentScoreEventMapper assessmentScore;
	private final String gutCode;
	private final boolean isSignature;

	private LearnerProfileCompetencyEvidenceService service = new LearnerProfileCompetencyEvidenceService(
			DBICreator.getDbiForDefaultDS());

	public LearnerProfileCompetencyEvidenceProcessor(AssessmentScoreEventMapper assessmentScore, String gutCode,
			boolean isSignature) {
		this.assessmentScore = assessmentScore;
		this.gutCode = gutCode;
		this.isSignature = isSignature;
	}

	public void process() {

		final double score = this.assessmentScore.getResult().getScore();

		// If the score is greater than the mastry/completion score then only persist
		// the evidence. Below mastry/completion score we are not treating the
		// competency as mastered or completed hence no need to persist the evidence.

		// Update: 12-July-2018: Requirement is to persist the evidence for in progress
		// status in non TS tables

		boolean isMicroCompetency = (HYPHEN_PATTERN.split(gutCode).length >= 5);
		LearnerProfileCompetencyEvidenceCommand command = LearnerProfileCompetencyEvidenceCommandBuilder
				.build(this.assessmentScore);

		// Calculate the status to persist in evidence ts table to uniquely identify the
		// evidence by status
		int status = StatusConstants.IN_PROGRESS;
		if (score >= MASTERY_SCORE) {
			if (isSignature) {
				status = StatusConstants.MASTERED;
			} else {
				status = StatusConstants.COMPLETED;
			}
		}

		// Regardless of score always persist the evidence. This is basically
		// NOT to overwrite the evidence in TS table when competency is transitioned to
		// completed/mastered from in progress. Which also enables to persist evidence
		// for in progress status. However in NON TS table evidence will be overwritten
		// with latest score if its same content, else will be inserted
		LOGGER.debug("persisting evidence in ts tables");
		if (isMicroCompetency) {
			processMicroCompetency(command);
			processMicroCompetencyForTS(command, status);
		} else {
			processCompetency(command);
			processCompetencyForTS(command, status);
		}
	}

	private void processMicroCompetency(LearnerProfileCompetencyEvidenceCommand command) {
		LearnerProfileMicroCompetencyEvidenceBean microCompetencyBean = new LearnerProfileMicroCompetencyEvidenceBean(
				command);
		microCompetencyBean.setMicroCompetencyCode(gutCode);
		this.service.insertOrUpdateLearnerProfileMicroCompetencyEvidence(microCompetencyBean);
	}

	private void processCompetency(LearnerProfileCompetencyEvidenceCommand command) {
		LearnerProfileCompetencyEvidenceBean bean = new LearnerProfileCompetencyEvidenceBean(command);
		bean.setGutCode(gutCode);
		this.service.insertOrUpdateLearnerProfileCompetencyEvidence(bean);
	}

	private void processMicroCompetencyForTS(LearnerProfileCompetencyEvidenceCommand command, int status) {
		LOGGER.debug("persisting micro competency evidence for TS");
		LearnerProfileMicroCompetencyEvidenceBean microCompetencyBean = new LearnerProfileMicroCompetencyEvidenceBean(
				command);
		microCompetencyBean.setMicroCompetencyCode(gutCode);
		microCompetencyBean.setStatus(status);
		this.service.insertOrUpdateLearnerProfileMicroCompetencyEvidenceTS(microCompetencyBean);
	}

	private void processCompetencyForTS(LearnerProfileCompetencyEvidenceCommand command, int status) {
		LOGGER.debug("persisting competency evidence for TS");
		LearnerProfileCompetencyEvidenceBean bean = new LearnerProfileCompetencyEvidenceBean(command);
		bean.setGutCode(gutCode);
		bean.setStatus(status);
		this.service.insertOrUpdateLearnerProfileCompetencyEvidenceTS(bean);
	}
}
