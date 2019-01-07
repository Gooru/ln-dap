package org.gooru.dap.deps.competency.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author gooru on 08-May-2018
 */
public class GutCodeMapper implements ResultSetMapper<GutCode> {

  @Override
  public GutCode map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    GutCode gc = new GutCode();
    gc.setGutCode(r.getString(GutCodeMapperFields.SOURCE_TAXONOMY_CODE_ID));
    gc.setTaxonomyCode(r.getString(GutCodeMapperFields.TARGET_TAXONOMY_CODE_ID));
    return gc;
  }

  private static final class GutCodeMapperFields {
    private GutCodeMapperFields() {
      throw new AssertionError();
    }

    private static final String SOURCE_TAXONOMY_CODE_ID = "source_taxonomy_code_id";
    private static final String TARGET_TAXONOMY_CODE_ID = "target_taxonomy_code_id";
  }
}
