package org.gooru.dap.processors.repositories.jdbi.dao.resource;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public abstract class OriginalResourceContentDao {

    @SqlQuery("SELECT original_content_id from content where id = :id")
    public abstract String getOriginalContentId(@Bind("id") String  id);
    
}
