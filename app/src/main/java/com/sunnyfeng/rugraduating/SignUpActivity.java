package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = this_year - 3; i <= this_year + 4; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> years_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, years);

        Spinner spin_year = (Spinner)findViewById(R.id.spin_year);
        spin_year.setAdapter(years_adapter);
        spin_year.setSelection(3);

        // populate seasons
        ArrayList<String> seasons = new ArrayList<String>();
        seasons.add("Fall");
        seasons.add("Winter");
        seasons.add("Spring");
        seasons.add("Summer");
        ArrayAdapter<String> seasons_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, seasons);

        Spinner spin_season = (Spinner) findViewById(R.id.spin_season);
        spin_season.setAdapter(seasons_adapter);
        spin_season.setSelection(2);

        //TODO: populate school and major spinners with database values
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
