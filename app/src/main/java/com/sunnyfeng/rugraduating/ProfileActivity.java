package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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
import com.sunnyfeng.rugraduating.adapters.StringArrayAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Course;
import com.sunnyfeng.rugraduating.objects.CourseItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private RecyclerView.Adapter coursesAdapter;
    private RecyclerView.LayoutManager coursesLayoutManager;

    private RecyclerView programsRecyclerView;
    private RecyclerView.Adapter programsAdapter;
    private RecyclerView.LayoutManager programsLayoutManager;

    private RecyclerView planRecyclerView;
    private RecyclerView.Adapter planAdapter;
    private RecyclerView.LayoutManager planLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Toolbar with title for profile (no menu)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Your Profile");

        // Set up buttons
        Button addClassesButton = findViewById(R.id.add_classes_button);
        addClassesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddClassesActivity.class);
            startActivity(intent);
        });
        Button addProgramButton = findViewById(R.id.add_program_button);
        addProgramButton.setOnClickListener(v -> {
            AddProgramDialog dialogProgram = new AddProgramDialog(ProfileActivity.this);
            dialogProgram.show();
        });
        Button addToPlanButton = findViewById(R.id.add_to_plan_button);
        addToPlanButton.setOnClickListener(v -> {
            AddToPlanDialog dialogPlan = new AddToPlanDialog(ProfileActivity.this);
            dialogPlan.show();
        });

        // Back to main button
        Button backToMainButton = findViewById(R.id.back_to_main_profile);
        backToMainButton.setOnClickListener(v -> {
            Intent intentMain = new Intent(ProfileActivity.this, TopViewActivity.class);
            startActivity(intentMain);
        });

        setUpRecyclerViews();
    }

    private void setUpRecyclerViews() {
        //TODO: separate student courses into regular and planned courses
        ArrayList<CourseItem> takenCourses = new ArrayList<>();
        ArrayList<CourseItem> plannedCourses = new ArrayList<>();
        ArrayList<String> programs = new ArrayList<>();

        // Set up courses recycler view
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView = findViewById(R.id.all_classes_recyclerView);
        coursesRecyclerView.setHasFixedSize(true);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        coursesAdapter = new CourseItemListAdapter(takenCourses);
        coursesRecyclerView.setAdapter(coursesAdapter);

        // Set up plan recycler view
        planLayoutManager = new LinearLayoutManager(this);
        planRecyclerView = findViewById(R.id.planned_courses_recyclerView);
        planRecyclerView.setHasFixedSize(true);
        planRecyclerView.setLayoutManager(planLayoutManager);
        planAdapter = new CourseItemListAdapter(plannedCourses);
        planRecyclerView.setAdapter(planAdapter);

        // Set up programs recycler view
        programsLayoutManager = new LinearLayoutManager(this);
        programsRecyclerView = findViewById(R.id.programs_recyclerView);
        programsRecyclerView.setHasFixedSize(true);
        programsRecyclerView.setLayoutManager(programsLayoutManager);
        programsAdapter = new StringArrayAdapter(programs.toArray(new String[0]));
        programsRecyclerView.setAdapter(programsAdapter);

        //a user's taken courses are always just courses, no need to add processing for equiv. or regex here
        //hit mongodb webhook for course data, will update suggestedRecyclerView asynchronously
        RequestQueue queue = Volley.newRequestQueue(this);
        String netID = "avin";
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
                        //add takenCourses
                        JSONObject respCourses = response.getJSONObject("takenCourses");
                        int respCoursesLength = respCourses.length();
                        while(i < respCoursesLength){
                            classString = respCourses.getString(Integer.toString(i++));
                            takenCourses.add(gson.fromJson(classString, Course.class));
                        }
                        //add plannedCourses
                        i=0;
                        JSONObject respPlannedCourses = response.getJSONObject("plannedCourses");
                        int respPlannedCoursesLength = respPlannedCourses.length();
                        while(i < respPlannedCoursesLength){
                            classString = respPlannedCourses.getString(Integer.toString(i++));
                            plannedCourses.add(gson.fromJson(classString, Course.class));
                        }
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                    //update views with new adapters
                    coursesAdapter = new CourseItemListAdapter(takenCourses);
                    coursesRecyclerView.setAdapter(coursesAdapter);
                    planAdapter = new CourseItemListAdapter(plannedCourses);
                    planRecyclerView.setAdapter(planAdapter);
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        queue.add(jsonObjectRequest);

        url ="https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getStudentPrograms?netID="+netID;
        //get programs
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        int i = 0;
                        int responseLength = response.length();
                        String programString;
                        //add programs
                        while(i < responseLength){
                            programString = response.getJSONObject(Integer.toString(i++)).getString("name");
                            programs.add(programString);
                        }
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                    //update views with new adapters
                    programsAdapter = new StringArrayAdapter(programs.toArray(new String[0]));
                    programsRecyclerView.setAdapter(programsAdapter);
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        queue.add(jsonObjectRequest);
    }

}
