package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //TODO: populate spinners
    }

    public boolean signup_check_populated(){
        boolean complete = true;
        // TODO:check each field, highlight if un-entered, set complete to false

        return complete;
    }

    public void create_user(View view){
        // check all fields are populated
        if(signup_check_populated()){
            // TODO:create the user (add to database), new intent to add classes


            Intent add_class_page = new Intent(this, AddClassesActivity.class);
            startActivity(add_class_page);
        }
        // otherwise, do nothing. all unentered fields highlighted
    }
}
