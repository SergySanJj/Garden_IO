package com.garden.endpoints.contentView.admin;

import com.db.DatabaseAction;
import com.garden.endpoints.API;
import com.garden.endpoints.RoleValidator;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/api/" + API.Content.ENDPOINT + "/" + API.Content.Params.ViewOrders)
public class ViewOrdersEndpoint extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "admin")) {
            resp.sendRedirect("/");
            return;
        }

        DatabaseAction.sendRequestedJSON(req, resp,
                connection -> connection.prepareStatement(
                        "SELECT orders.id, owners.login as owner, gardeners.login as gardener, o.name, quantity, finished, approved " +
                                "FROM orders " +
                                "LEFT JOIN users AS gardeners ON orders.gardener = gardeners.id " +
                                "LEFT JOIN users AS owners ON orders.owner = owners.id " +
                                "JOIN ordertypes o on orders.ordertype = o.id"),
                resultSet -> {
                    JSONObject o = new JSONObject();
                    o.put("id", resultSet.getInt(1));
                    o.put("ownerLogin", resultSet.getString(2));
                    o.put("gardenerLogin", resultSet.getString(3));
                    o.put("orderTypeName", resultSet.getString(4));
                    o.put("quantity", resultSet.getInt(5));
                    o.put("finished", resultSet.getBoolean(6));
                    o.put("approved", resultSet.getBoolean(7));
                    return o;
                },
                "orders");
    }
}
