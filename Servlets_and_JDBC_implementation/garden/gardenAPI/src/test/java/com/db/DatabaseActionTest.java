package com.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DatabaseActionTest {
    @BeforeClass
    public static void initDB() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        DatabaseConfig.config("gardenTestConfig.json");
        DatabaseAction.dropAndInit();
    }

    @Test
    public void runMethodWorking() {
        final ResultSet[] resultSet = new ResultSet[1];
        DatabaseAction.run(connection -> {
            Statement statement = connection.createStatement();
            resultSet[0] = statement.executeQuery("SELECT * FROM users");
        });
        Assert.assertNotNull(resultSet[0]);
    }

    @Test
    public void userExist() {
        Assert.assertTrue(DatabaseAction.userLoginExists("admin"));
        Assert.assertTrue(DatabaseAction.userLoginExists("gardener"));
        Assert.assertTrue(DatabaseAction.userLoginExists("owner"));

        Assert.assertFalse(DatabaseAction.userLoginExists("notExistingLogin"));
    }

    @Test
    public void addUser() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        try {
            DatabaseAction.tryAddUser(request, response, "testUser", "testPassword", "testName", "gardener");
            verify(session, times(1)).setAttribute("last-action", "User added");
            verify(response, times(1)).sendRedirect("/");
        } catch (IOException e) {
            Assert.assertTrue(false);
        }

        // Adding already existing user
        try {
            DatabaseAction.tryAddUser(request, response, "testUser", "testPassword", "testName", "gardener");
            verify(session, times(1)).setAttribute("last-action", "User already exists");
            verify(response, times(2)).sendRedirect("/");
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void addOrder() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        final Integer[] countBefore = new Integer[1];
        DatabaseAction.run(connection -> {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
            Assert.assertTrue(resultSet.next());
            countBefore[0] = resultSet.getInt(1);
        });

        final Integer[] countAfter = new Integer[1];
        try {
            DatabaseAction.addOrder(request, response, 3, "heal", 10);
            DatabaseAction.run(connection -> {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
                Assert.assertTrue(resultSet.next());
                countAfter[0] = resultSet.getInt(1);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(countAfter[0] > countBefore[0]);
    }

    @Test
    public void userLoginExists() {
        Assert.assertTrue(DatabaseAction.userLoginExists("admin"));
        Assert.assertTrue(DatabaseAction.userLoginExists("gardener"));
        Assert.assertTrue(DatabaseAction.userLoginExists("owner"));

        Assert.assertFalse(DatabaseAction.userLoginExists("unexisting"));
    }
}