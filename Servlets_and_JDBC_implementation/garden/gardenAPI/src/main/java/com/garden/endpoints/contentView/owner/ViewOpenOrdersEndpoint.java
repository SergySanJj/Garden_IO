package com.garden.endpoints.contentView.owner;

import com.db.DatabaseAction;
import com.garden.StoredSID;
import com.garden.endpoints.API;
import com.garden.endpoints.RoleValidator;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/" + API.Content.ENDPOINT + "/" + API.Content.Params.ViewOpenOrders)
public class ViewOpenOrdersEndpoint extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "owner")) {
            resp.sendRedirect("/");
            return;
        }

        Integer userId = StoredSID.getUser((String) req.getSession().getAttribute("SID")).getId();

        DatabaseAction.sendRequestedJSON(req, resp,
                connection -> connection.prepareStatement(
                        "SELECT orders.id, orderType.name, orders.quantity " +
                                "FROM orders " +
                                "LEFT JOIN ordertypes orderType on orders.ordertype = orderType.id " +
                                "WHERE orders.finished = false AND orders.owner = " + userId),
                resultSet -> {
                    JSONObject o = new JSONObject();
                    o.put("id", resultSet.getInt(1));
                    o.put("orderType", resultSet.getString(2));
                    o.put("quantity", resultSet.getInt(3));
                    return o;
                },
                "openOrders");
    }
}
