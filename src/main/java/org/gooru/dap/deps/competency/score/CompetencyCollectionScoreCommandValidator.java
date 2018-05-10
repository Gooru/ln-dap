package org.gooru.dap.deps.competency.score;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gooru on 07-May-2018
 */
public class CompetencyCollectionScoreCommandValidator {
	
	private final static Logger LOGGER = LoggerFactory.getLogger("");
	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

	private final static List<String> VALID_FORMATS = Arrays.asList("assessment", "collection");

	private final CompetencyCollectionScoreCommand command;

	public CompetencyCollectionScoreCommandValidator(CompetencyCollectionScoreCommand command) {
		this.command = command;
	}
	
	public void validate() {
		validateFormat();
	}

	private void validateFormat() {
		String format = this.command.getCollectionType();
		if (format == null || format.isEmpty() || !VALID_FORMATS.contains(format)) {
			LOGGER.warn("Invalid collection format provided");
			this.validationFailed("collectionscore.format.invalid");
		}
	}
	
	private void validationFailed(String messageCode) {
		
	}
}
