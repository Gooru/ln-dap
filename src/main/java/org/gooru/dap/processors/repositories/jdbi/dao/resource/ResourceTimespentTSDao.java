package org.gooru.dap.processors.repositories.jdbi.dao.resource;

import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public abstract class ResourceTimespentTSDao {
    
    @SqlUpdate("insert into resource_timespent values('something')")
    public abstract void save();
    
}
