package com.garden.endpoints.userManagement.admin;

import com.db.DatabaseAction;
import com.garden.endpoints.API;
import com.garden.endpoints.RoleValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/" + API.AddOwner.ENDPOINT)
public class AddOwnerEndpoint extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "admin")) {
            resp.sendRedirect("/");
            return;
        }

        String login = req.getParameter(API.AddGardener.Params.LOGIN);
        String password = req.getParameter(API.AddGardener.Params.PASSWORD);
        String name = req.getParameter(API.AddGardener.Params.NAME);
        System.out.println("Adding owner");
        DatabaseAction.tryAddUser(req, resp, login, password, name, "owner");
    }
}
