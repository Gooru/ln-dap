package org.gooru.dap.deps.competency.usermatrix;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gooru on 04-May-2018
 */
public class UserCompetencyMatrixCommand {

	private String txSubjectCode;
	private String userId;
	private String txCompCode;
	private String status;

	public String getTxSubjectCode() {
		return txSubjectCode;
	}

	public String getUserId() {
		return userId;
	}

	public String getTxCompCode() {
		return txCompCode;
	}

	public String getStatus() {
		return status;
	}

	static UserCompetencyMatrixCommand builder(JsonNode request) {
		UserCompetencyMatrixCommand command = UserCompetencyMatrixCommand.buildFromJson(request);
		command.validate();
		return command;
	}

	private static UserCompetencyMatrixCommand buildFromJson(JsonNode request) {
		UserCompetencyMatrixCommand command = new UserCompetencyMatrixCommand();
		command.txSubjectCode = "";
		command.txCompCode = "";
		command.userId = "";
		command.status = "";
		return command;
	}

	private void validate() {

	}

	public UserCompetencyMatrixBean asBean() {
		UserCompetencyMatrixBean bean = new UserCompetencyMatrixBean();
		bean.txSubjectCode = txSubjectCode;
		bean.userId = userId;
		bean.txCompCode = txCompCode;
		bean.status = status;
		return bean;
	}

	private static class UserCompetencyMatrixBean {
		private String txSubjectCode;
		private String userId;
		private String txCompCode;
		private String status;

		public String getTxSubjectCode() {
			return txSubjectCode;
		}

		public void setTxSubjectCode(String txSubjectCode) {
			this.txSubjectCode = txSubjectCode;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getTxCompCode() {
			return txCompCode;
		}

		public void setTxCompCode(String txCompCode) {
			this.txCompCode = txCompCode;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
