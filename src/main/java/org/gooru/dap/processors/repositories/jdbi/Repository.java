package org.gooru.dap.processors.repositories.jdbi;

import org.gooru.dap.components.jdbi.DBICreator;
import org.gooru.dap.processors.ExecutionStatus;
import org.skife.jdbi.v2.DBI;

public abstract class Repository {

  private static final DBI DBI = DBICreator.getDbiForDefaultDS();

  private static final DBI CORE_DBI = DBICreator.getDbiForCoreDS();

  public abstract ExecutionStatus validateRequest();

  public abstract void executeRequest();


  protected DBI getDbiForCoreDS() {
    return CORE_DBI;
  }

  protected DBI getDbiForDefaultDS() {
    return DBI;
  }
}
