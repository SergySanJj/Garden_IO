package com.garden;

import com.db.DatabaseConfig;
import com.garden.endpoints.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = "/api",  loadOnStartup = 1)
public class GardenServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        DatabaseConfig.config("gardenConfig.json");

        // Verify that driver is loaded
        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){
        try (PrintWriter writer = resp.getWriter()) {
            StringBuilder q = new StringBuilder();
            Class<?>[] classes = API.class.getClasses();
            for (Class<?> cl:classes){
                q.append(cl.getName()).append(":\n");
                Field[] fields = cl.getFields();
                for (Field field:fields){
                    q.append("\t").append(field.getName()).append("\n");
                }
            }
            writer.write(q.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getServletPath());
    }
}
