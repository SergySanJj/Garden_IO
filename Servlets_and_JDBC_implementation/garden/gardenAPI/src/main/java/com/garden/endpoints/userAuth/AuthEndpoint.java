package com.garden.endpoints.userAuth;

import com.db.DatabaseAction;
import com.garden.SIDMaker;
import com.garden.StoredSID;
import com.garden.beans.User;
import com.garden.endpoints.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

@WebServlet("/api/" + API.Authorisation.ENDPOINT)
public class AuthEndpoint extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isValid(req)) {
            String sid = SIDMaker.make(req.getParameter(API.Authorisation.Params.LOGIN),
                    req.getParameter(API.Authorisation.Params.PASSWORD));

            StoredSID.addSID
                    (sid, getUserInfo(req.getParameter(API.Authorisation.Params.LOGIN),
                            req.getParameter(API.Authorisation.Params.PASSWORD)));

            System.out.println("Valid user credentials " + sid);
            req.getSession().setAttribute("SID", sid);

            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            System.out.println("Invalid user credentials");
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }


    private boolean isValid(HttpServletRequest req) {
        return verifyCredentials
                (
                        req.getParameter(API.Authorisation.Params.LOGIN),
                        req.getParameter(API.Authorisation.Params.PASSWORD)
                );
    }

    private boolean verifyCredentials(String login, String password) {
        final boolean[] res = new boolean[1];
        DatabaseAction.run(connection -> {
            try (PreparedStatement statement =
                         connection.prepareStatement("SELECT * FROM users WHERE login=? AND password=?")) {
                statement.setString(1, login);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    res[0] = resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return res[0];
    }

    private User getUserInfo(String login, String password) {
        final User[] res = new User[1];
        DatabaseAction.run(connection -> {
            try (PreparedStatement statement =
                         connection.prepareStatement
                                 (
                                         "SELECT users.id, users.login, users.name, roles.name " +
                                                 "FROM users JOIN roles ON users.role = roles.id " +
                                                 "WHERE users.login=? AND users.password=?")
            ) {
                statement.setString(1, login);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt(1));
                        user.setLogin(resultSet.getString(2));
                        user.setName(resultSet.getString(3));
                        user.setRole(resultSet.getString(4));
                        res[0] = user;
                    } else
                        res[0] = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return res[0];
    }
}
