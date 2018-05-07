package org.gooru.dap.processors.repositories.jdbi.common.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ContentMapper implements ResultSetMapper<ContentBean> {

    private final static String CONTENT_SUBFORMAT = "content_subformat";
    private final static String ORIGINAL_CONTENT_ID = "original_content_id";

    @Override
    public ContentBean map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        ContentBean contentBean = new ContentBean();
        contentBean.setOriginalContentId(r.getString(ORIGINAL_CONTENT_ID));
        contentBean.setContentType(r.getString(CONTENT_SUBFORMAT));
        return contentBean;
    }

}
