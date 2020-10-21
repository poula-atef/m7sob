package com.example.m7sob;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class captain implements Serializable {
    private String name;
    private String password;
    private HashMap<String,order> orders;

    public captain() {
    }

    public captain(String name, String password, HashMap<String, order> orders) {
        this.name = name;
        this.password = password;
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, order> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, order> orders) {
        this.orders = orders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
