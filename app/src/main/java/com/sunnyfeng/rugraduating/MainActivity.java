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
import com.sunnyfeng.rugraduating.adapters.CoursesListAdapter;
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
        eceTech.addCourseTaken(getPrinCommCourse());
        reqsTest.add(eceTech);
        Requirement soeGen = new Requirement("SOE General Electives", false, 10, 3);
        soeGen.addCourseTaken(getMultiVarCalcCourse());
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
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String param1 = "webhook";
        String param2 = "test";
        String url ="https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/webhook0?arg1="+param1+"&arg2="+param2;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    // TODO: implement serialization? to be able to unpack all course data more simply
                    //get values from json
                    String val1 = "null";
                    String val2 = "null";
                    try{
                        val1 = response.getString("val1");
                        val2 = response.getString("val2");
                    } catch(Exception e){
                        System.out.println("failed to unpack JSON");
                    }
                    //build courses with values
                    suggestedTest.add(getCourse(val1, val2));
                    //update view with new adapter
                    suggestedAdapter = new CoursesListAdapter(suggestedTest);
                    suggestedRecyclerView.setAdapter(suggestedAdapter);
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private Course getCourse(String name, String code) {
        Course prinComm = new Course(name, code, 3,
                "This \"test\" is an example of how we can get values from a webhook set up on MongoDB Stitch.");
        prinComm.addPrereq(getProbCourse());
        return prinComm;
    }

    private Course getPrinCommCourse() {
        Course prinComm = new Course("Wireless Revolution", "14:332:301", 3,
                "This \"flipped\" undergraduate course provides a broad view of how new technologies, economic forces, political constraints, and competitive warfare have created and shaped the \"wireless revolution\" in the last 50 years.  It offers a view inside the world of corporate management-- how strategies were created and why many have failed—and gives students a chance to develop their own strategic skills by solving real-world problems.  The course includes a historical overview of communications and communication systems, basics of wireless technology, technology and politics of cellular, basics of corporate finance, economics of cellular systems and spectrum auctions, case studies in wireless business strategy, the strategic implications of unregulated spectrum, a comparison of 3G, 4G, 5G and WiFi, IoT and the wireless future. Students are required to interact during the lectures in a flipped classroom setting—necessitating pre-lecture preparation, in-class attendance and participation.");
        prinComm.addPrereq(getProbCourse());
        return prinComm;
    }

    private Course getProbCourse() {
        Course prob = new Course("Probability and Random Processes", "14:332:226", 3,
                "Probability and its axioms, conditional probability, independence, counting, random variables and distributions, functions of random variables, expectations, order statistics, central limit theorem, confidence intervals, hypothesis testing, estimation of random variables. Random processes and their characterization, autocorrelation function.");
        prob.addPrereq(new Course("Multivariable Calculus", "01:640:251", 4, "Analytic geometry of three dimensions, partial derivatives, optimization techniques, multiple integrals, vectors in Euclidean space, and vector analysis."));
        return prob;
    }

    private Course getMultiVarCalcCourse() {
        Course course = new Course("Multivariable Calculus", "01:640:251", 4, "Analytic geometry of three dimensions, partial derivatives, optimization techniques, multiple integrals, vectors in Euclidean space, and vector analysis.");
        return course;
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
        Toast.makeText(this,itemSelected + " selected.", Toast.LENGTH_SHORT).show();
    }

    // Handle major spinner item not selected
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO: maybe handle this? or just lead blank
    }
}
