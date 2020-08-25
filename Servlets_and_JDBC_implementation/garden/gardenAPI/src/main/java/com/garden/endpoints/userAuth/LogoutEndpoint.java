package com.garden.endpoints.userAuth;

import com.garden.endpoints.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/api/" + API.Logout.ENDPOINT)
public class LogoutEndpoint extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("SID");
        session.invalidate();
        resp.setHeader("Cache-Control","no-cache, no store, must-revalidate"); //HTTP 1.1
        resp.setHeader("Pragma","no-cache"); //HTTP 1.0
        resp.setDateHeader ("Expires", 0);
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
