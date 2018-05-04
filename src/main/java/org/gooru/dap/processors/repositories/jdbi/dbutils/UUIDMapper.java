package org.gooru.dap.processors.repositories.jdbi.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


public class UUIDMapper implements ResultSetMapper<UUID> {

    @Override
    public UUID map(final int index, final ResultSet resultSet, final StatementContext statementContext)
        throws SQLException {
        return UUID.fromString(resultSet.getString(1));
    }

}
