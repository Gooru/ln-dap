
package org.gooru.dap.jobs.group.reports;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 09-Dec-2019
 */
public class ClassModelMapper implements ResultSetMapper<ClassModel> {

  private final static Logger LOGGER = LoggerFactory.getLogger(ClassModelMapper.class);

  @Override
  public ClassModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    ClassModel model = new ClassModel();
    model.setId(r.getString("id"));
    model.setTitle(r.getString("title"));
    model.setGradeCurrent(r.getLong("grade_current"));

    String strPreference = r.getString("preference");
    ObjectMapper mapper = new ObjectMapper();
    try {
      ClassPreferenceModel preference = mapper.readValue(strPreference, ClassPreferenceModel.class);
      model.setSubject(preference.getSubject());
      model.setFramework(preference.getFramework());
    } catch (IOException e) {
      LOGGER.debug("unable to read preference from the class");
    }
    return model;
  }

}
