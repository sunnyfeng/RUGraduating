package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.objects.User;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity{

    // Store registration credentials and pass to database for first-time users
    private String user_name = "";
    private String netID = "";
    private String school = "";
    private String major = "";

    private String emptyString = "None selected";

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

        // Populating schools with names of all schools via POST request to Stitch getSchools function.
        ArrayList<String> schools = new ArrayList<String>();
        schools.add(emptyString); // Empty entry at top of drop-down.
        RequestQueue requests = Volley.newRequestQueue(this);
        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getSchools";
        JsonObjectRequest getAllSchools = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try{
                int count = 0;
                while (count < response.length()) {
                    schools.add(response.getString(Integer.toString(count)));
                    count++;
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }, error -> {
            //TODO: Handle error gracefully
            System.out.println(error);
        });
        requests.add(getAllSchools);


        ArrayAdapter<String> school_adapter  =  new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, schools);
        Spinner school_spin = (Spinner) findViewById(R.id.spin_school);
        school_spin.setAdapter(school_adapter);

        // Leave majors spinner blank until schools spinner has a selected value.
        ArrayList<String> majors = new ArrayList<String>();
        majors.add(emptyString);

        ArrayAdapter<String> major_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, majors);
        Spinner major_spin = (Spinner) findViewById(R.id.spin_major);
        major_spin.setAdapter(major_adapter);

        school_spin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                        majors.clear();
                        majors.add(emptyString);
                        String selectedSchool = school_spin.getSelectedItem().toString();
                        if(selectedSchool.length() > 0 && !selectedSchool.equals(emptyString)){
                            // Passing selectedSchool into Stitch getPrograms function to populate majors with strings of program names
                            // in selectedSchool.
                            String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getPrograms?schoolName="+selectedSchool;
                            JsonObjectRequest getAllPrograms = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
                                try{
                                    int count = 0;
                                    while (count < response.length()) {
                                        majors.add(response.getString(Integer.toString(count)));
                                        count++;
                                    }

                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }

                            }, error -> {
                                //TODO: Handle error gracefully
                                System.out.println(error);
                            });
                            requests.add(getAllPrograms);

                        }
                        major_adapter.notifyDataSetChanged();
                    }
                    public void onNothingSelected(AdapterView<?> parent){}
                }
        );

        User mUser = ((User)getApplicationContext());
        String firstName = mUser.getFirstName();
        String lastName = mUser.getLastName();
        String netID = mUser.getNetID();
        if(!(firstName.equals("not set") || lastName.equals("not set"))) {
            EditText name_textbox = (EditText)findViewById(R.id.name_sign_up);
            name_textbox.setText(mUser.getFirstName() + " " + mUser.getLastName());
        }
        if(!mUser.getNetID().equals("not set")){
            EditText netid_textbox = (EditText)findViewById(R.id.netid_sign_up);
            netid_textbox.setText(netID);
            netid_textbox.setEnabled(false);
            netid_textbox.setInputType(InputType.TYPE_NULL);
        }
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
            netid_textbox.setError("Please enter your netID");
        }

        Spinner school_spin = (Spinner)findViewById(R.id.spin_school);
        school = school_spin.getSelectedItem().toString();
        if(school.length() == 0 || school.equals(emptyString)){
            complete = false;

            TextView errorText = (TextView)school_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }

        Spinner major_spin = (Spinner)findViewById(R.id.spin_major);
        major = major_spin.getSelectedItem().toString();
        if(major.length() == 0 || major.equals(emptyString)){
            complete = false;

            TextView errorText = (TextView)major_spin.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error

        }

        return complete;
    }

    public void create_user(View view){
        // check all fields are populated
        if(signup_check_populated()){
            User user = ((User)(getApplicationContext()));
            String[] userNameArr = user_name.split(" ");
            user.setFirstName(userNameArr[0]);
            if(userNameArr[1] != null){
                user.setLastName(userNameArr[1]);
            }
            //user.setNetID(netID); //should be set in stone at this point
            // Now, package schools and majors into two comma separated strings delineated by { and }. For now, not doing that b/c
            // both of the inputs only accept one school and major.....(should be changed).
            // Then, send POST request to backend to create new student in the database. If it works, then we receive true,
            // otherwise false.
            RequestQueue requests = Volley.newRequestQueue(this);
            String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/registerStudent?netID=" + netID + "&name="
                    + user_name + "&schools=" + school + "&programs=" + major;
            JsonObjectRequest registerStudent = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
                try{
                    boolean isSuccessful = response.getBoolean("0");
                    if (isSuccessful) {
                        Intent mainIntent = new Intent(this, AddClassesActivity.class);
                        startActivity(mainIntent);
                    } else {
                        Intent mainIntent = new Intent(this, LoginActivity.class);
                        startActivity(mainIntent);
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }, error -> {
                //TODO: Handle error gracefully
                System.out.println(error);
            });
            requests.add(registerStudent);



        }
        // otherwise, do nothing. all unentered fields highlighted
    }
}
