package com.example.m7sob;

import java.io.Serializable;

public class order  implements Serializable {
    private String code;
    private String date;
    private String time;
    private int shop;
    private int customer;

    public order() {
    }

    public order(String code, String date, String time, int shop, int customer) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.shop = shop;
        this.customer = customer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
