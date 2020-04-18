package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import com.sunnyfeng.rugraduating.adapters.CourseItemListAdapter;
import com.sunnyfeng.rugraduating.adapters.IntegerTypeAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Course;
import com.sunnyfeng.rugraduating.objects.CourseItem;
import com.sunnyfeng.rugraduating.objects.Equivalency;
import com.sunnyfeng.rugraduating.objects.Regex;

import org.json.JSONArray;

import java.util.ArrayList;

public class SuggestedCoursesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView suggestedRecyclerView;
    private RecyclerView.Adapter suggestedAdapter;
    private RecyclerView.LayoutManager suggestedLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_classes);

        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Suggested Courses");
        toolbar.inflateMenu(R.menu.options_menu);

        //TODO: get these options from DB
        String[] levelArray = {"No Selection", "0", "1", "2"};
        String[] programArray = {"No Selection", "Computer Engineering", "Computer Science"};
        String[] reqArray = {"No Selection", "Technical Electives", "Computer Electives"};

        // Drop down menu for level
        Spinner levelSpinner = findViewById(R.id.level_spinner);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, levelArray);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);
        levelSpinner.setOnItemSelectedListener(this);

        // Drop down menu for program
        Spinner programSpinner = findViewById(R.id.program_spinner);
        ArrayAdapter<String> programAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, programArray);
        programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programSpinner.setAdapter(programAdapter);
        programSpinner.setOnItemSelectedListener(this);

        // Drop down menu for reqs
        Spinner reqSpinner = findViewById(R.id.requirement_spinner);
        ArrayAdapter<String> reqAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, reqArray);
        reqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reqSpinner.setAdapter(reqAdapter);
        reqSpinner.setOnItemSelectedListener(this);

        // Refresh button
        Button refreshButton = findViewById(R.id.refresh_suggested_button);
        refreshButton.setOnClickListener(v -> {
            //TODO: On refresh, do query for suggested courses with the spinner parameters
        });

        // Back button
        Button backToMainButton = findViewById(R.id.back_to_main_suggested);
        backToMainButton.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }

    private void setUpRecyclerViews() {
        // Set up suggested recycler view
        suggestedLayoutManager = new LinearLayoutManager(this);
        suggestedRecyclerView = findViewById(R.id.suggested_recyclerView);
        suggestedRecyclerView.setHasFixedSize(true);
        suggestedRecyclerView.setLayoutManager(suggestedLayoutManager);

        ArrayList<CourseItem> suggestedTest = new ArrayList<>();
        suggestedAdapter = new CourseItemListAdapter(suggestedTest);
        suggestedRecyclerView.setAdapter(suggestedAdapter);

        //this query works for courses, equivalencies, and regexes
        //should have an array of courses
        String[] testCourses = {"14:332:331", "Calculus II Math/Phys Equivalency", "01:160:12.*"};
        //convert to a string
        StringBuilder sb = new StringBuilder("{");
        for(int i = 0; i < testCourses.length; i++){
            sb.append(testCourses[i]);
            sb.append(",");
        }
        sb.append("}");
        String testCoursesString = sb.toString();
        //hit mongodb webhook for course data, will update suggestedRecyclerView asynchronously
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getCERObjects?wrappedCourseListString="+testCoursesString;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        int i = 0;
                        String classString;
                        //build courses with values
                        Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();

                        //add courses
                        JSONArray respCourses = (JSONArray) response.get("courses");
                        int respCoursesLength = respCourses.length();
                        while(i < respCoursesLength){
                            classString = respCourses.getString(i++);
                            suggestedTest.add(gson.fromJson(classString, Course.class));
                        }
                        //add equivalencies
                        i=0;
                        JSONArray respEquivs = (JSONArray) response.get("equivalencies");
                        int respEquivsLength = respEquivs.length();
                        while(i < respEquivsLength){
                            classString = respEquivs.getString(i++);
                            suggestedTest.add(gson.fromJson(classString, Equivalency.class));
                        }
                        //add regexes
                        i=0;
                        JSONArray respRegexes = (JSONArray)response.get("regexes");
                        int respRegexesLength = respRegexes.length();
                        while(i < respRegexesLength){
                            classString = respRegexes.getString(i++);
                            suggestedTest.add(gson.fromJson(classString, Regex.class));
                        }
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                    //update view with new adapter
                    suggestedAdapter = new CourseItemListAdapter(suggestedTest);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            //TODO: filter options based on the selections above
            case R.id.level_spinner:
                break;
            case R.id.program_spinner:
                break;
            case R.id.requirement_spinner:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
