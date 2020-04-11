package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity{

    // Store registration credentials and pass to database for first-time users
    private String user_name = "";
    private String netID = "";
    private String school = "";
    private String major = "";
    private String user_email = "";
    private String user_pwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Toolbar with title and options menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Sign Up");

        //TODO: populate school and major spinners with database values
        ArrayList<String> schools = new ArrayList<String>();
        schools.add("");
        // TODO: INSERT QUERY TO GET SCHOOLS HERE, REPLACE PLACEHOLDER
        schools.add("TEST SCHOOL");

        ArrayAdapter<String> school_adapter  =  new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, schools);
        Spinner school_spin = (Spinner) findViewById(R.id.spin_school);
        school_spin.setAdapter(school_adapter);

        ArrayList<String> majors = new ArrayList<String>();
        majors.add("");

        ArrayAdapter<String> major_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, majors);
        Spinner major_spin = (Spinner) findViewById(R.id.spin_major);
        major_spin.setAdapter(major_adapter);

        school_spin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                        majors.clear();
                        majors.add("");
                        String selected_school = school_spin.getSelectedItem().toString();
                        if(selected_school.length() > 0){
                            // TODO: INSERT QUERY TO GET MAJORS UNDER SCHOOL HERE, REPLACE PLACEHOLDER
                            majors.add("TEST MAJOR");
                        }
                        major_adapter.notifyDataSetChanged();
                    }
                    public void onNothingSelected(AdapterView<?> parent){}
                }
        );

    }

    public boolean signup_check_populated(){
        boolean complete = true;

        EditText name_textbox = (EditText)findViewById(R.id.name_sign_up);
        user_name = name_textbox.getText().toString();
        if(user_name.length() == 0){
            complete = false;
            name_textbox.setError("Please enter your name");
        }

        EditText netid_textbox = (EditText)findViewById(R.id.netid_sign_up);
        netID = netid_textbox.getText().toString();
        if(netID.length() == 0){
            complete = false;
            netid_textbox.setError("Please enter your name");
        }

        Spinner school_spin = (Spinner)findViewById(R.id.spin_school);
        school = school_spin.getSelectedItem().toString();
        if(school.length() == 0){
            complete = false;

            TextView errorText = (TextView)school_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }

        Spinner major_spin = (Spinner)findViewById(R.id.spin_major);
        major = major_spin.getSelectedItem().toString();
        if(major.length() == 0){
            complete = false;

            TextView errorText = (TextView)major_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }

        EditText email_textbox = (EditText)findViewById(R.id.email_sign_up);
        user_email = email_textbox.getText().toString();
        if(user_email.length() == 0){
            complete = false;
            email_textbox.setError("Please enter your email");
        }

        EditText pwd_textbox = (EditText)findViewById(R.id.pwd_sign_up);
        user_pwd = pwd_textbox.getText().toString();
        if(user_pwd.length() == 0){
            complete = false;
            pwd_textbox.setError("Please enter your password");
        }

        return complete;
    }

    public void create_user(View view){
        // check all fields are populated
        if(signup_check_populated()){
            // TODO: create the user (add to database), new intent to add classes

            Intent mainIntent = new Intent(this, AddClassesActivity.class);
            startActivity(mainIntent);
        }
        // otherwise, do nothing. all unentered fields highlighted
    }
}
