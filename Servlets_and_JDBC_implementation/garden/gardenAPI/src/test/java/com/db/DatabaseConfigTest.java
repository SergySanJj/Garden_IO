package com.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseConfigTest {
    @BeforeClass
    public static void loadDatabaseDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void configReadFromFile() {
        DatabaseConfig.config("gardenTestConfig.json");

        Assert.assertNotNull(DatabaseConfig.url);
        Assert.assertNotNull(DatabaseConfig.name);
        Assert.assertNotNull(DatabaseConfig.login);
        Assert.assertNotNull(DatabaseConfig.password);
    }

    @Test
    public void initDatabaseConnectionFromDatabaseConfig() {
        DatabaseConfig.config("gardenTestConfig.json");

        try (Connection connection = DriverManager.getConnection
                (
                        DatabaseConfig.url,
                        DatabaseConfig.name,
                        DatabaseConfig.password
                )) {
            Assert.assertNotNull(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}