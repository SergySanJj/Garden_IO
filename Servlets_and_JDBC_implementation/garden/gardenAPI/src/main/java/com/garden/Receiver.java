package com.garden;

import com.db.DatabaseAction;
import com.db.ResultSetAction;
import com.garden.endpoints.API;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Receiver<E> {
    private Class<E> typeHost;
    private final static int maxDelay = 20000;

    public List<E> arrayContent(String containerName, String endpoint, HttpServletRequest request, String sessionId) {
        List<E> res = new ArrayList<>();
        String s = Receiver.receiveContent(request, endpoint, sessionId);
        JSONObject container = null;
        try {
            container = (JSONObject) new JSONParser().parse(s);
        } catch (ParseException e) {
            System.out.println("Error in: " + s);
            e.printStackTrace();
            return res;
        }
        JSONArray items = (JSONArray) container.get(containerName);
        Gson g = new Gson();

        if (items == null || items.isEmpty())
            return res;

        for (Object user : items) {
            E item = g.fromJson(((JSONObject) user).toJSONString(), typeHost);
            res.add(item);
        }
        return res;
    }

    public static String receiveContent(HttpServletRequest request, String endpoint, String sessionId) {
        return getFrom(request.getRequestURL() + "api/" + API.Content.ENDPOINT + "/" + endpoint, sessionId);
    }

    public static String getFrom(String urlString, String sessionId) {
        try {
            final URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            con.setRequestProperty("Cookie", "JSESSIONID=" + URLEncoder.encode(sessionId, StandardCharsets.UTF_8));
            final String content = readFrom(con);
            if (content != null) return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readFrom(HttpURLConnection con) {
        con.setConnectTimeout(maxDelay);
        con.setReadTimeout(maxDelay);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getOrderTypesRepresentation() {
        List<String> types = new ArrayList<>();
        DatabaseAction.run(connection -> {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT ordertypes.name FROM ordertypes");
                ResultSet results = statement.executeQuery();
                while (results.next()) {
                    types.add(results.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        StringBuilder res = new StringBuilder("<input list='orderTypes' name='" + API.AddOrder.Params.TYPE + "'>" +
                "<datalist id='orderTypes'>");
        for (String item : types) {
            res.append("<option value='").append(item).append("'>");
        }
        res.append("</datalist>");
        return res.toString();
    }

    public Receiver(Class<E> typeHost) {
        this.typeHost = typeHost;
    }
}
