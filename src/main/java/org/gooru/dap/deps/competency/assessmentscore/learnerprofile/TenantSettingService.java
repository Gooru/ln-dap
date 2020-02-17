package org.gooru.dap.deps.competency.assessmentscore.learnerprofile;


import org.gooru.dap.constants.Constants;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantSettingService {
  private final TenantSettingDao dao;
  private final static Logger LOGGER = LoggerFactory.getLogger(TenantSettingService.class);
      
  public TenantSettingService(DBI dbiForCoreDS) {
    this.dao = dbiForCoreDS.onDemand(TenantSettingDao.class);
    
  }
  public Double fetchTenantCompletionScore(
      String tenantId) {
    String completionScore = this.dao.fetchTenantCompletionScore(tenantId);
    try  {
      return Double.parseDouble(completionScore);
    } catch(Exception e) {
      LOGGER.error("Invalid completion score for settings '{}'",completionScore);
    }
    // returning default score incase of error or null
    return Constants.DEFAULT_COMPLETED_SCORE;
  }
}
