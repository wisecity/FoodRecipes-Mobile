package com.wisecity.foodrecipes;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String userName;
    @SerializedName("password")
    private String password;
    @SerializedName("device_id")
    private String device_id;


    public User(String userName, String password, String device_id) {
        this.userName = userName;
        this.password = password;
        this.device_id = device_id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDeviceId() {
        return device_id;
    }
}
