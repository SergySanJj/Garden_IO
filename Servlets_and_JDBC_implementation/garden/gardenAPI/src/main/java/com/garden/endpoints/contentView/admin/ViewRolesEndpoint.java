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

@WebServlet("/api/" + API.Content.ENDPOINT + "/" + API.Content.Params.ViewRoles)
public class ViewRolesEndpoint extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RoleValidator.validate(req, "admin")) {
            resp.sendRedirect("/");
            return;
        }

        DatabaseAction.sendRequestedJSON(req, resp,
                connection -> connection.prepareStatement(
                        "SELECT roles.id, roles.name " +
                                "FROM roles"),
                resultSet -> {
                    JSONObject o = new JSONObject();
                    o.put("id", resultSet.getInt(1));
                    o.put("name", resultSet.getString(2));
                    return o;
                },
                "roles");
    }
}
