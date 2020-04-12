package com.sunnyfeng.rugraduating;

import android.app.Application;

public class User extends Application {

    private String netID = "not set";
    private String firstName = "not set";
    private String lastName = "not set";
    private String email = "not set";

    public String getNetID() {
        return netID;
    }

    public void setNetID(String str) {
        netID = str;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String str) {
        firstName = str;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String str) {
        lastName = str;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String str) {
        email = str;
    }
}