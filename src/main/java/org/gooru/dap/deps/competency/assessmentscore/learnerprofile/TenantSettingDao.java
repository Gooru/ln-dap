package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public abstract class TenantSettingDao {
  
  @SqlQuery("select value from tenant_setting where key='comp_completion_threshold_for_assessment' and id = :tenantId::uuid ")
  protected abstract String fetchTenantCompletionScore(
      @Bind("tenantId") String tenantId);


}
