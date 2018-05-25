package org.gooru.dap.deps.competency.db.mapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gooru.dap.deps.competency.CompetencyConstants;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gooru on 08-May-2018
 */
public class AssessmentCompetencyMapper implements ResultSetMapper<AssessmentCompetency> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CompetencyConstants.LOGGER_NAME);

	@Override
	public AssessmentCompetency map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		ObjectMapper mapper = new ObjectMapper();
		
		AssessmentCompetency ac = new AssessmentCompetency();
		try {
			String taxonomy = r.getString(AssessmentCompetencyMapperFields.TAXONOMY);
			if (taxonomy != null && !taxonomy.isEmpty()) {
				JsonParser parser = mapper.getFactory().createParser(taxonomy);
				ac.setTaxonomy(mapper.readTree(parser));
			}
		} catch (IOException e) {
			LOGGER.warn("unable to parse assessment taxonomy", e);
		}

		return ac;
	}

	private static final class AssessmentCompetencyMapperFields {
		private AssessmentCompetencyMapperFields() {
			throw new AssertionError();
		}

		private static final String TAXONOMY = "taxonomy";
	}
}
