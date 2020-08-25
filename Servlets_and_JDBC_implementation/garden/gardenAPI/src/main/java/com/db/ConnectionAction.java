package com.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionAction {
    void run(Connection connection) throws SQLException;
}
