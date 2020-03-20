package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: add splash page logo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        EditText user_email = (EditText) findViewById(R.id.email_addr);
        EditText user_pwd = (EditText) findViewById(R.id.pwd);

        //TODO: check database if user exists
        // if doesn't exist, return



        // else, load intent, send login credentials?
        Intent profile_page = new Intent(this, ProfileActivity.class);
        startActivity(profile_page);


    }

    public void sign_up(View view){
        Intent signup_page = new Intent(this, SignUpActivity.class);
        startActivity(signup_page);
    }
}
