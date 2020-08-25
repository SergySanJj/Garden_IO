package com.db;

import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetAction {
    public JSONObject act(ResultSet resultSet) throws SQLException;
}
