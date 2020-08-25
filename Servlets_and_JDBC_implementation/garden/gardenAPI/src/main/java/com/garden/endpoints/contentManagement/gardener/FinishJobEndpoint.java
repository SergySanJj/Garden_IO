package com.garden.endpoints.contentManagement.gardener;


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
import java.sql.PreparedStatement;

@WebServlet("/api/" + API.FinishJob.ENDPOINT)
public class FinishJobEndpoint extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "gardener")) {
            resp.sendRedirect("/");
            return;
        }

        Integer orderId = Integer.valueOf(req.getParameter(API.FinishJob.Params.ORDERID));
        Integer gardenerId = StoredSID.getUser((String) req.getSession().getAttribute("SID")).getId();

        System.out.println("Finishing order " + orderId);
        DatabaseAction.run(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET finished = true, gardener = " + gardenerId +
                            " WHERE id = " + orderId);
            statement.executeUpdate();
        });
        req.getSession().setAttribute("last-action","Order " + orderId + " completed, waiting for approval");
        resp.sendRedirect("/");
    }
}
