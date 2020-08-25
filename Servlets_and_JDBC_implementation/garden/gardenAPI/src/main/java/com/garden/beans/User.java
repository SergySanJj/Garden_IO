package com.garden.beans;

public class User {
    public User() {

    }

    public User(Integer id, String login, String name, String role) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.role = role;
    }

    private Integer id;
    private String login;
    private String name;
    private String role;

    public static class Roles {
        public static final String ADMIN = "admin";
        public static final String GARDENER = "gardener";
        public static final String OWNER = "owner";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return id + " " + login + " " + role;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            User other = (User) obj;
            return id.equals(other.id) &&
                    login.equals(other.login) &&
                    name.equals(other.name) &&
                    role.equals(other.role);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
