package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunnyfeng.rugraduating.adapters.CoursesListAdapter;
import com.sunnyfeng.rugraduating.adapters.IntegerTypeAdapter;
import com.sunnyfeng.rugraduating.adapters.RequirementsListAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // intent keys
    public static final String REQUIREMENT_INTENT_KEY = "requirements intent key";
    public static final String COURSE_INTENT_KEY = "courses intent key";


    private RecyclerView requirementsRecyclerView;
    private RecyclerView.Adapter requirementsAdapter;
    private RecyclerView.LayoutManager requirementLayoutManager;

    private RecyclerView suggestedRecyclerView;
    private RecyclerView.Adapter suggestedAdapter;
    private RecyclerView.LayoutManager suggestedLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Main Page");
        toolbar.inflateMenu(R.menu.options_menu);

        // Drop down menu for majors
        Spinner spinner = findViewById(R.id.major_spinner_main);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.major_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Switch for withPlan
        Switch sw = findViewById(R.id.withPlanSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean withPlan) {
                if (withPlan) {
                    // The toggle is enabled
                    // TODO: add function with plan
                    Toast.makeText(MainActivity.this, "Including plan.", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    // TODO: add function without plan
                    Toast.makeText(MainActivity.this, "Not including plan.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpRecyclerViews();
    }

    private void setUpRecyclerViews() {
        // Set up requirements recycler view
        ArrayList<Requirement> reqsTest = new ArrayList<>();
        Requirement eceTech = new Requirement("ECE Tech Electives", false, 5, 2);
        //eceTech.addCourseTaken(getPrinCommCourse());
        reqsTest.add(eceTech);
        Requirement soeGen = new Requirement("SOE General Electives", false, 10, 3);
        //soeGen.addCourseTaken(getMultiVarCalcCourse());
        reqsTest.add(soeGen);

        requirementLayoutManager = new LinearLayoutManager(this);
        requirementsRecyclerView = findViewById(R.id.requirements_recyclerView);
        requirementsRecyclerView.setHasFixedSize(true);
        requirementsRecyclerView.setLayoutManager(requirementLayoutManager);
        requirementsAdapter = new RequirementsListAdapter(reqsTest);
        requirementsRecyclerView.setAdapter(requirementsAdapter);

        // Set up suggested courses recycler view
        ArrayList<Course> suggestedTest = new ArrayList<>();
        suggestedLayoutManager = new LinearLayoutManager(this);
        suggestedRecyclerView = findViewById(R.id.suggested_recyclerView);
        suggestedRecyclerView.setHasFixedSize(true);
        suggestedRecyclerView.setLayoutManager(suggestedLayoutManager);
        suggestedAdapter = new CoursesListAdapter(suggestedTest);
        suggestedRecyclerView.setAdapter(suggestedAdapter);

        //hit mongodb webhook for course data, will update suggestedRecyclerView asynchronously
        RequestQueue queue = Volley.newRequestQueue(this);
        String netID = "AmanyTest";
        String url ="https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getTakenCourses?netID="+netID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        int i = 0;
                        int responseLength = response.length();
                        String classString;
                        //build courses with values
                        Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();;
                        while(i < responseLength){
                            classString = response.getString(String.valueOf(i++));
                            suggestedTest.add(gson.fromJson(classString, Course.class));
                        }
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                    //update view with new adapter
                    suggestedAdapter = new CoursesListAdapter(suggestedTest);
                    suggestedRecyclerView.setAdapter(suggestedAdapter);
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        queue.add(jsonObjectRequest);
    }

    // Inflate options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    // Add actions when option in menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(this.getLocalClassName(), "Selected Item: " +item.getTitle());
        switch (item.getItemId()) {
            case R.id.add_program_item:
                AddProgramDialog dialogProgram = new AddProgramDialog(this);
                dialogProgram.show();
                return true;
            case R.id.add_to_plan_item:
                AddToPlanDialog dialogPlan = new AddToPlanDialog(this);
                dialogPlan.show();
                return true;
            case R.id.logout_item:
                Intent intent_logout = new Intent(this, LoginActivity.class);
                // don't allow back press to re-enter
                intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                return true;
            case R.id.profile_item:
                Intent intent_profile = new Intent(this, ProfileActivity.class);
                startActivity(intent_profile);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Handle major spinner item selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String itemSelected = parent.getItemAtPosition(pos).toString();
        // TODO: on item selected, filter progress by major
    }

    // Handle major spinner item not selected
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO: maybe handle this? or just lead blank
    }
}
