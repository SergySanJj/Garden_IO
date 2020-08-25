package com.garden.beans;

public class UnconfirmedOrder {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getGardenerLogin() {
        return gardenerLogin;
    }

    public void setGardenerLogin(String gardenerLogin) {
        this.gardenerLogin = gardenerLogin;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private Integer id;
    private String orderType;
    private String gardenerLogin;
    private int quantity;
}
