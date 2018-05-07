package org.gooru.dap.processors.events.resource.timespent;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

abstract class OriginalResourceContentDao {

    @SqlQuery("SELECT original_content_id from content where id = :id")
    public abstract String getOriginalContentId(@Bind("id") String  id);
    
}
