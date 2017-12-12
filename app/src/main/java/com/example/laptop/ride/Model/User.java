package com.example.laptop.ride.Model;

/**
 * Created by Laptop on 11/27/2017.
 */

public class User {
    private String emaail,password,name,phone;

    public User() {
    }

    public User(String emaail, String password, String name, String phone) {
        this.emaail = emaail;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public String getEmaail() {
        return emaail;
    }

    public void setEmaail(String emaail) {
        this.emaail = emaail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

