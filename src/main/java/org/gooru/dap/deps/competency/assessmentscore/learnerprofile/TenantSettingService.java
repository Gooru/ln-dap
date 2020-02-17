package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;


import org.skife.jdbi.v2.DBI;

public class TenantSettingService {
  private final TenantSettingDao dao;
  
  public TenantSettingService(DBI dbiForCoreDS) {
    this.dao = dbiForCoreDS.onDemand(TenantSettingDao.class);
    
  }
  public String fetchTenantSettings(
      String tenantId) {
    return this.dao.fetchTenantSettings(tenantId);
  }
}
