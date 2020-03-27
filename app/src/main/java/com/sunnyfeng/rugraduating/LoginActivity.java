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

    public boolean login_check_populated(){
        boolean complete = true;

        EditText email_textbox = (EditText) findViewById(R.id.email_addr);
        String user_email = email_textbox.getText().toString();
        if(user_email.length() == 0){
            complete = false;
            email_textbox.setError("Please enter your email");
        }

        EditText pwd_textbox = (EditText) findViewById(R.id.pwd);
        String user_pwd = pwd_textbox.getText().toString();
        if(user_pwd.length() == 0){
            complete = false;
            pwd_textbox.setError("Please enter your password");
        }
        return complete;
    }

    public void login(View view){
        if(login_check_populated()) {

            //TODO: check database if user exists
            // if doesn't exist, return


            // else, load intent, send login credentials?
            Intent main_page = new Intent(this, MainActivity.class);
            startActivity(main_page);

        }
    }

    public void sign_up(View view){
        // send straight to sign up page
        Intent signup_page = new Intent(this, SignUpActivity.class);
        startActivity(signup_page);
    }
}
