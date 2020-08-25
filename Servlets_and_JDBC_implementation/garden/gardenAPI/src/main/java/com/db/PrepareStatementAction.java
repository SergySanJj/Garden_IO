package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementAction {
    PreparedStatement act(Connection connection) throws SQLException;
}
