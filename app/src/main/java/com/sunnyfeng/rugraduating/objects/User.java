package com.sunnyfeng.rugraduating.objects;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class User extends Application {

    private String netID = "not set";
    private String firstName = "not set";
    private String lastName = "not set";
    private GoogleSignInClient sclient = null;

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

    public void setSclient(GoogleSignInClient sclient) { this.sclient = sclient; }

    public GoogleSignInClient getSclient() { return sclient; }
}