package org.gooru.dap.components.jdbi;

import javax.sql.DataSource;
import org.gooru.dap.components.DataSourceRegistry;
import org.gooru.dap.constants.Constants;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 20/2/18.
 */
public final class DBICreator {

  private DBICreator() {
    throw new AssertionError();
  }

  public static DBI getDbiForDefaultDS() {
    return createDBI(DataSourceRegistry.getInstance().getDefaultDataSource());
  }

  public static DBI getDbiForCoreDS() {
    return createDBI(
        DataSourceRegistry.getInstance().getDataSourceByName(Constants.CORE_DATA_SOURCE));
  }

  public static DBI getDbiForReportsDS() {
    return createDBI(
        DataSourceRegistry.getInstance().getDataSourceByName(Constants.REPORTS_DATA_SOURCE));
  }

  private static DBI createDBI(DataSource dataSource) {
    DBI dbi = new DBI(dataSource);
    dbi.registerArgumentFactory(new PostgresIntegerArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresStringArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresUUIDArrayArgumentFactory());
    return dbi;
  }

}
