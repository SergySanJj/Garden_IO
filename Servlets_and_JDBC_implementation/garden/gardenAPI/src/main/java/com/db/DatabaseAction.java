package com.db;

import com.garden.endpoints.API;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class DatabaseAction {
    public static void addOrder(HttpServletRequest req, HttpServletResponse resp,
                                Integer ownerID, String orderType, Integer quantity) throws IOException {
        System.out.println("Order adding");

        DatabaseAction.run(connection -> {
            try (PreparedStatement statement =
                         connection.prepareStatement("INSERT INTO orders " +
                                 "values (nextval('orders_id_seq')," +
                                 "? , " +
                                 "null, " +
                                 "(SELECT ordertypes.id FROM ordertypes WHERE ordertypes.name = ?), " +
                                 "?, " +
                                 "false, " +
                                 "false);")) {
                statement.setInt(1, ownerID);
                statement.setString(2, orderType);
                statement.setInt(3, quantity);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void tryAddUser(HttpServletRequest req, HttpServletResponse resp,
                                  String login, String password, String name, String role) throws IOException {
        System.out.println("User adding");

        if (userLoginExists(login)) {
            req.getSession().setAttribute("last-action", "User already exists");
        } else {
            DatabaseAction.run(connection -> {
                try (PreparedStatement statement =
                             connection.prepareStatement("INSERT INTO users " +
                                     "values (nextval('id_seq'), ?, ?, ?, (SELECT roles.id FROM roles where roles.name = ?));")) {
                    statement.setString(1, login);
                    statement.setString(2, password);
                    statement.setString(3, name);
                    statement.setString(4, role);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            req.getSession().setAttribute("last-action", "User added");
        }
        resp.sendRedirect("/");
    }

    public static void sendRequestedJSON(HttpServletRequest req,
                                         HttpServletResponse resp,
                                         PrepareStatementAction prepareStatementAction,
                                         ResultSetAction resultSetAction,
                                         String containerName) throws IOException {
        resp.setContentType("application/json");
        JSONArray array = new JSONArray();
        DatabaseAction.run(connection -> {
            try (PreparedStatement statement = prepareStatementAction.act(connection)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        JSONObject o = resultSetAction.act(resultSet);
                        array.add(o);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        JSONObject container = new JSONObject();
        container.put(containerName, array);

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(container.toJSONString());
        }
    }


    public static synchronized void run(ConnectionAction action) {
        try (Connection connection = DriverManager.getConnection
                (
                        DatabaseConfig.url,
                        DatabaseConfig.name,
                        DatabaseConfig.password
                )) {
            action.run(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userLoginExists(String login) {
        boolean[] res = new boolean[1];
        DatabaseAction.run(connection -> {
            try (PreparedStatement statement =
                         connection.prepareStatement("SELECT * FROM users WHERE login=?")) {
                statement.setString(1, login);

                try (ResultSet resultSet = statement.executeQuery()) {
                    res[0] = resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return res[0];
    }
    public static void dropAndInit() {
        run(connection -> {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS roles,users,orderTypes,orders;");
            statement.execute("DROP SEQUENCE IF EXISTS id_seq,ordertypes_id_seq,orders_id_seq;");
            statement.execute("CREATE SEQUENCE IF NOT EXISTS id_seq\n" +
                    "    INCREMENT 1\n" +
                    "    MINVALUE 1\n" +
                    "    MAXVALUE 9223372036854775807\n" +
                    "    START 1\n" +
                    "    CACHE 1;");
            statement.execute("create table if not exists roles\n" +
                    "(\n" +
                    "    id   serial      NOT NULL primary key,\n" +
                    "    name varchar(10) NOT NULL\n" +
                    ");");
            statement.execute("insert into roles\n" +
                    "values (1, 'admin');\n" +
                    "insert into roles\n" +
                    "values (2, 'gardener');\n" +
                    "insert into roles\n" +
                    "values (3, 'owner');");
            statement.execute("create table IF NOT EXISTS users\n" +
                    "(\n" +
                    "    id       serial             NOT NULL primary key,\n" +
                    "    login    varchar(32) UNIQUE NOT NULL,\n" +
                    "    password varchar(32)        NOT NULL,\n" +
                    "    name     varchar            NOT NULL,\n" +
                    "    role     INTEGER            NOT NULL,\n" +
                    "    foreign key (role) references roles (id)\n" +
                    ");");
            statement.execute("ALTER TABLE users\n" +
                    "    ALTER COLUMN id SET default nextval('id_seq');");
            statement.execute("insert into users\n" +
                    "values (nextval('id_seq'), 'admin', 'admin', 'Mike', 1);\n" +
                    "insert into users\n" +
                    "values (nextval('id_seq'), 'gardener', 'gardener', 'David', 2);\n" +
                    "insert into users\n" +
                    "values (nextval('id_seq'), 'owner', 'owner', 'Alice', 3);");
            statement.execute("create table if not exists orderTypes\n" +
                    "(\n" +
                    "    id   serial NOT NULL primary key,\n" +
                    "    name varchar(20)\n" +
                    ");");
            statement.execute("CREATE SEQUENCE IF NOT EXISTS ordertypes_id_seq\n" +
                    "    INCREMENT 1\n" +
                    "    MINVALUE 1\n" +
                    "    MAXVALUE 9223372036854775807\n" +
                    "    START 1\n" +
                    "    CACHE 1;");
            statement.execute("ALTER SEQUENCE ordertypes_id_seq\n" +
                    "    OWNED BY orderTypes.id;\n" +
                    "ALTER TABLE orderTypes\n" +
                    "    ALTER COLUMN id SET default nextval('ordertypes_id_seq');");
            statement.execute("insert into orderTypes\n" +
                    "values (nextval('ordertypes_id_seq'), 'heal');\n" +
                    "insert into orderTypes\n" +
                    "values (nextval('ordertypes_id_seq'), 'art');\n" +
                    "insert into orderTypes\n" +
                    "values (nextval('ordertypes_id_seq'), 'destroy');");
            statement.execute("create table if not exists orders\n" +
                    "(\n" +
                    "    id        serial NOT NULL primary key,\n" +
                    "    owner     integer,\n" +
                    "    gardener  integer,\n" +
                    "    orderType integer,\n" +
                    "    quantity  integer,\n" +
                    "    finished  boolean,\n" +
                    "    approved  boolean,\n" +
                    "    foreign key (owner) references users (id),\n" +
                    "    foreign key (gardener) references users (id),\n" +
                    "    foreign key (orderType) references orderTypes (id)\n" +
                    ");");
            statement.execute("CREATE SEQUENCE IF NOT EXISTS orders_id_seq\n" +
                    "    INCREMENT 1\n" +
                    "    MINVALUE 1\n" +
                    "    MAXVALUE 9223372036854775807\n" +
                    "    START 1\n" +
                    "    CACHE 1;");
            statement.execute("ALTER SEQUENCE orders_id_seq\n" +
                    "    OWNED BY orders.id;\n" +
                    "ALTER TABLE orders\n" +
                    "    ALTER COLUMN id SET default nextval('orders_id_seq');");
            statement.execute("insert into orders\n" +
                    "values (nextval('orders_id_seq'),\n" +
                    "        (SELECT users.id from users where users.name = 'owner'),\n" +
                    "        (SELECT users.id from users where users.name = 'gardener'),\n" +
                    "        1, 10, false, false);\n" +
                    "insert into orders\n" +
                    "values (nextval('orders_id_seq'),\n" +
                    "        (SELECT users.id from users where users.name = 'owner'),\n" +
                    "        (SELECT users.id from users where users.name = 'gardener'),\n" +
                    "        1, 10, false, true);\n" +
                    "insert into orders\n" +
                    "values (nextval('orders_id_seq'),\n" +
                    "        (SELECT users.id from users where users.name = 'owner'),\n" +
                    "        (SELECT users.id from users where users.name = 'gardener'),\n" +
                    "        1, 10, true, false);\n" +
                    "insert into orders\n" +
                    "values (nextval('orders_id_seq'),\n" +
                    "        (SELECT users.id from users where users.name = 'owner'),\n" +
                    "        (SELECT users.id from users where users.name = 'gardener'),\n" +
                    "        1, 10, true, true);");
            statement.close();
        });
    }
}
