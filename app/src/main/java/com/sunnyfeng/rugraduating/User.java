package com.sunnyfeng.rugraduating;

import android.app.Application;

public class User extends Application {

    private String netID = "not set";

    public String getNetID() {
        return netID;
    }

    public void setNetID(String str) {
        netID = str;
    }
}