package com.garden.endpoints.contentView.gardener;

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

@WebServlet("/api/" + API.Content.ENDPOINT + "/" + API.Content.Params.ViewFinishedJobs)
public class ViewFinishedJobsEndpoint extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "gardener")) {
            resp.sendRedirect("/");
            return;
        }

        Integer userId = StoredSID.getUser((String) req.getSession().getAttribute("SID")).getId();

        DatabaseAction.sendRequestedJSON(req, resp,
                connection -> connection.prepareStatement(
                        "SELECT orders.id, o.name, orders.quantity, owner.name, orders.approved " +
                                "FROM orders JOIN ordertypes o on orders.ordertype = o.id " +
                                "LEFT JOIN users owner on orders.owner = owner.id " +
                                "WHERE orders.finished = true AND orders.gardener = " + userId),
                resultSet -> {
                    JSONObject o = new JSONObject();
                    o.put("id", resultSet.getInt(1));
                    o.put("jobType", resultSet.getString(2));
                    o.put("quantity", resultSet.getInt(3));
                    o.put("ownerName", resultSet.getString(4));
                    o.put("approved", resultSet.getBoolean(5));
                    return o;
                },
                "finishedJobs");
    }
}
