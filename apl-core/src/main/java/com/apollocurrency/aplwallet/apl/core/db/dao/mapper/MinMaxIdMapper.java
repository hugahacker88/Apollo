/*
 * Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.db.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apollocurrency.aplwallet.apl.core.db.derived.MinMaxDbId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;


public class MinMaxIdMapper implements RowMapper<MinMaxDbId> {

    @Override
    public MinMaxDbId map(ResultSet rs, StatementContext ctx) throws SQLException {

        return MinMaxDbId.builder()
                .minDbId(rs.getLong("MIN_ID") - 1) // pagination is exclusive for lower bound
                .maxDbId(rs.getLong("MAX_ID") + 1) // pagination is exclusive for upper bound
                .count(rs.getLong("COUNT"))
                .build();
    }
}
