package org.gooru.dap.processors.repositories.jdbi.common.Dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public abstract class ContentDao {

    @SqlQuery("SELECT original_content_id, content_subformat::text from content where id = :id::uuid")
    @RegisterMapper(ContentMapper.class)
    public abstract ContentBean findContentById(@Bind("id") String  id);
    
}
