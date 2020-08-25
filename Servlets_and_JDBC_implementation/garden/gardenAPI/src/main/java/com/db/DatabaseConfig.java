package com.db;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class DatabaseConfig {
    public static void config(String fileName) {
        System.out.println("Configuring database");
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = (JSONObject) obj;
        JSONObject databaseInfoObject = (JSONObject) jsonObject.get("database");

        url = (String) databaseInfoObject.get(URL);
        login = (String) databaseInfoObject.get(LOGIN);
        name = (String) databaseInfoObject.get(NAME);
        password = (String) databaseInfoObject.get(PASSWORD);
        configured = true;

        System.out.println("Configured for");
        System.out.println(url + " " + login + " " + name);
    }

    public static String url;
    public static String login;
    public static String name;
    public static String password;

    private static boolean configured = false;

    private static final String URL = "url";
    private static final String LOGIN = "login";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
}
