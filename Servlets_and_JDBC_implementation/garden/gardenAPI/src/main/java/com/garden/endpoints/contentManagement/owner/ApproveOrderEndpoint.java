package com.garden.endpoints.contentManagement.owner;

import com.db.DatabaseAction;
import com.garden.endpoints.API;
import com.garden.endpoints.RoleValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;

@WebServlet("/api/" + API.ApproveOrder.ENDPOINT)
public class ApproveOrderEndpoint extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "owner")) {
            resp.sendRedirect("/");
            return;
        }

        Integer orderId = Integer.valueOf(req.getParameter(API.ApproveOrder.Params.ORDERID));
        System.out.println("Approving order " + orderId);
        DatabaseAction.run(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET approved = true " +
                            "WHERE id = " + orderId);
            statement.executeUpdate();
        });
    }
}
