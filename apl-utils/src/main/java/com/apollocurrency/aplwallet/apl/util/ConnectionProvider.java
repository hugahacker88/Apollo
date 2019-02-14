/*
 * Copyright © 2018 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
    Connection getConnection() throws SQLException;

    void begin();

    void rollbackTransaction(Connection connection);

    void commitTransaction(Connection connection);

    void endTransaction(Connection connection);

    boolean isInTransaction(Connection connection);
}
