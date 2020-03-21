package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // populate spin_year
        ArrayList<String> years = new ArrayList<String>();
        years.add("");
        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = this_year - 3; i <= this_year + 4; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> years_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, years);

        Spinner spin_year = (Spinner)findViewById(R.id.spin_year);
        spin_year.setAdapter(years_adapter);
        spin_year.setSelection(0);

        // populate seasons
        ArrayList<String> seasons = new ArrayList<String>();
        seasons.add("");
        seasons.add("Fall");
        seasons.add("Winter");
        seasons.add("Spring");
        seasons.add("Summer");
        ArrayAdapter<String> seasons_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, seasons);

        Spinner spin_season = (Spinner) findViewById(R.id.spin_season);
        spin_season.setAdapter(seasons_adapter);
        spin_season.setSelection(0);

        //TODO: populate school and major spinners with database values
    }

    public boolean signup_check_populated(){
        boolean complete = true;

        EditText name_textbox = (EditText)findViewById(R.id.name_sign_up);
        String user_name = name_textbox.getText().toString();
        if(user_name.length() == 0){
            complete = false;
            name_textbox.setError("Please enter your name");
        }

        Spinner seasons_spin = (Spinner)findViewById(R.id.spin_season);
        String season = seasons_spin.getSelectedItem().toString();
        if(season.length() == 0){
            complete = false;

            TextView errorText = (TextView)seasons_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }

        Spinner year_spin = (Spinner)findViewById(R.id.spin_year);
        String year = year_spin.getSelectedItem().toString();
        if(year.length() == 0){
            complete = false;

            TextView errorText = (TextView)year_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }
        /*
        Spinner school_spin = (Spinner)findViewById(R.id.spin_school);
        String school = school_spin.getSelectedItem().toString();
        if(school.length() == 0){
            complete = false;

            TextView errorText = (TextView)school_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }

        Spinner major_spin = (Spinner)findViewById(R.id.spin_major);
        String major = major_spin.getSelectedItem().toString();
        if(major.length() == 0){
            complete = false;

            TextView errorText = (TextView)major_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }
        */
        EditText email_textbox = (EditText)findViewById(R.id.email_sign_up);
        String user_email = email_textbox.getText().toString();
        if(user_email.length() == 0){
            complete = false;
            email_textbox.setError("Please enter your email");
        }

        EditText pwd_textbox = (EditText)findViewById(R.id.pwd_sign_up);
        String user_pwd = pwd_textbox.getText().toString();
        if(user_pwd.length() == 0){
            complete = false;
            pwd_textbox.setError("Please enter your password");
        }

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
