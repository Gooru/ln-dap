package org.gooru.dap.deps.competency.status;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 04-May-2018
 */
public class CompetencyStatusCommand {

	private String userId;
	private String competencyCode;
	private String competencyDisplayCode;
	private String frameworkCode;
	private String status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompetencyCode() {
		return competencyCode;
	}

	public void setCompetencyCode(String competencyCode) {
		this.competencyCode = competencyCode;
	}

	public String getCompetencyDisplayCode() {
		return competencyDisplayCode;
	}

	public void setCompetencyDisplayCode(String competencyDisplayCode) {
		this.competencyDisplayCode = competencyDisplayCode;
	}

	public String getFrameworkCode() {
		return frameworkCode;
	}

	public void setFrameworkCode(String frameworkCode) {
		this.frameworkCode = frameworkCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	static CompetencyStatusCommand build(JsonNode request) {
		CompetencyStatusCommand command = CompetencyStatusCommand.buildFromJson(request);
		command.validate();
		return command;
	}

	private static CompetencyStatusCommand buildFromJson(JsonNode request) {
		CompetencyStatusCommand command = new CompetencyStatusCommand();
		return command;
	}

	private void validate() {

	}

	public CompetencyStatusBean asBean() {
		CompetencyStatusBean bean = new CompetencyStatusBean();
		return bean;
	}

	public static class CompetencyStatusBean {
		private String userId;
		private String competencyCode;
		private String competencyDisplayCode;
		private String frameworkCode;
		private String status;

		public String getUserId() {
			return userId;
		}

		public String getCompetencyCode() {
			return competencyCode;
		}

		public String getCompetencyDisplayCode() {
			return competencyDisplayCode;
		}

		public String getFrameworkCode() {
			return frameworkCode;
		}

		public String getStatus() {
			return status;
		}

	}
}
