package org.gooru.dap.deps.competency.preprocessors;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author mukul@gooru
 */
public class DCAContentModelMapper implements ResultSetMapper<DCAContentModel> {

  @Override
  public DCAContentModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    DCAContentModel model = new DCAContentModel();

    model.setClassId(r.getString(MapperFields.CLASS_ID));
    model.setAllowMasteryAccrual(r.getBoolean(MapperFields.ALLOW_MASTERY_ACCRUAL));
    model.setContentId(r.getString(MapperFields.CONTENT_ID));
    model.setContentType(r.getString(MapperFields.CONTENT_TYPE));

    return model;
  }

  private static final class MapperFields {
    private MapperFields() {
      throw new AssertionError();
    }

    private static final String ID = "id";
    private static final String CLASS_ID = "class_id";
    private static final String ALLOW_MASTERY_ACCRUAL = "allow_mastery_accrual";
    private static final String CONTENT_ID = "content_id";
    private static final String CONTENT_TYPE = "content_type";

  }

}
