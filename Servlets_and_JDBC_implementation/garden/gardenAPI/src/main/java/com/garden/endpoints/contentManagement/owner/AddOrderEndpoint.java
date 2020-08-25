package com.garden.endpoints.contentManagement.owner;

import com.db.DatabaseAction;
import com.garden.StoredSID;
import com.garden.endpoints.API;
import com.garden.endpoints.RoleValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/" + API.AddOrder.ENDPOINT)
public class AddOrderEndpoint extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "owner")) {
            resp.sendRedirect("/");
            return;
        }

        Integer ownerId = StoredSID.getUser((String) req.getSession().getAttribute("SID")).getId();
        String orderType = req.getParameter(API.AddOrder.Params.TYPE);
        Integer quantity = Integer.parseInt(req.getParameter(API.AddOrder.Params.QUANTITY));

        DatabaseAction.addOrder(req, resp, ownerId, orderType, quantity);
        req.getSession().setAttribute("last-action","Order for " + quantity + " of " + orderType + " posted");
        resp.sendRedirect("/");
    }
}
