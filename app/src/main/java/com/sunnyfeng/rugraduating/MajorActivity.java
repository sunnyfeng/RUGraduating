package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunnyfeng.rugraduating.adapters.CourseItemListAdapter;
import com.sunnyfeng.rugraduating.adapters.IntegerTypeAdapter;
import com.sunnyfeng.rugraduating.adapters.RequirementsListAdapter;
import com.sunnyfeng.rugraduating.objects.Course;
import com.sunnyfeng.rugraduating.objects.Equivalency;
import com.sunnyfeng.rugraduating.objects.Regex;
import com.sunnyfeng.rugraduating.objects.Requirement;
import com.sunnyfeng.rugraduating.objects.User;
import com.sunnyfeng.rugraduating.objects.CourseItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MajorActivity extends AppCompatActivity {

    // intent keys
    public static final String REQUIREMENT_INTENT_KEY = "requirement intent key";
    public static final String COURSE_INTENT_KEY = "course intent key";
    public static final String EQUIV_INTENT_KEY = "equiv intent key";
    public static final String REGEX_INTENT_KEY = "regex intent key";
    public static final String MAJOR_INTENT_KEY = "major intent key";


    private RecyclerView requirementsRecyclerView;
    private RecyclerView.Adapter requirementsAdapter;
    private RecyclerView.LayoutManager requirementLayoutManager;

    private String major_from_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get major from intent
        Intent intent = getIntent();
        major_from_intent = (String) intent.getSerializableExtra(MajorActivity.MAJOR_INTENT_KEY);

        // Set major name to be display
        TextView major_display = findViewById(R.id.selected_major_textView);
        major_display.setText(major_from_intent);

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle(major_from_intent);
        toolbar.inflateMenu(R.menu.options_menu);

        // Switch for withPlan
        Switch sw = findViewById(R.id.withPlanSwitch);
        sw.setOnCheckedChangeListener((buttonView, withPlan) -> {
            if (withPlan) {
                // The toggle is enabled
                // TODO: add function with plan
                Toast.makeText(MajorActivity.this, "Including plan.", Toast.LENGTH_SHORT).show();
            } else {
                // The toggle is disabled
                // TODO: add function without plan
                Toast.makeText(MajorActivity.this, "Not including plan.", Toast.LENGTH_SHORT).show();
            }
        });

        // Go to suggested courses button
        Button suggestedButton = findViewById(R.id.go_to_suggested_button);
        suggestedButton.setOnClickListener(v -> {
            Intent i = new Intent(MajorActivity.this, SuggestedCoursesActivity.class);
            startActivity(i);
        });

        setUpRecyclerViews();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(MajorActivity.this, TopViewActivity.class);
        startActivity(i);
    }

    private void setUpRecyclerViews() {
        // Set up requirements recycler view
        ArrayList<Requirement> reqsTest = new ArrayList<>();
        RequestQueue requests = Volley.newRequestQueue(this);

        User mUser = ((User)getApplicationContext());
        //String firstName = mUser.getFirstName();
        //String lastName = mUser.getLastName();
        String netID = mUser.getNetID();

        //ArrayList<String[]> reqCourses = new ArrayList<String[]>();

        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getRequirementProgress?netID=" + netID + "&program=" + major_from_intent;
        JsonObjectRequest getReqsProgress = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try{
                int count = 0;
                while (count < response.length()) {
                    JSONObject reqInfo = response.getJSONObject(Integer.toString(count));
                    JSONArray JSONcourses = reqInfo.getJSONArray("courses");
                    JSONArray JSONuntakenCourses = reqInfo.getJSONArray("untakenCourses");

                    // Convert to list of CER string names and store reqCourses to use for CERObjects call later
                    String[] coursesStringArr = new String[2];
                    StringBuilder sb = new StringBuilder("{");
                    for(int j = 0; j < JSONcourses.length(); j++){
                        sb.append(JSONcourses.getString(j));
                        sb.append(",");
                    }
                    sb.setLength(sb.length() - 1);
                    sb.append("}");
                    coursesStringArr[0] = sb.toString().replace("\\", "");

                    sb.delete(0, sb.length());
                    sb.append("{");
                    for(int j = 0; j < JSONuntakenCourses.length(); j++){
                        sb.append(JSONuntakenCourses.getString(j));
                        sb.append(",");
                    }
                    sb.setLength(sb.length() - 1);
                    sb.append("}");
                    coursesStringArr[1] = sb.toString().replace("\\", "");

                    reqInfo.remove("courses");
                    reqInfo.remove("untakenCourses");

                    String reqJSONString = reqInfo.toString();

                    Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();
                    Requirement req = gson.fromJson(reqJSONString, Requirement.class);

                    Log.d("Help", "Courses Request URL Param: " + coursesStringArr[0]);

                    // Handle empty strings below before requests
                    String coursesUrl = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getCERObjects?wrappedCourseListString="+coursesStringArr[0] + "&netID="+netID;

                    ArrayList<CourseItem> courses = new ArrayList<>();
                    ArrayList<CourseItem> untakenCourses = new ArrayList<>();

                    // Convert list of course strings into CourseItem objects to be put into the requirement's courses list.
                    if (coursesUrl.length() > 1) {
                        JsonObjectRequest coursesCERORequest = new JsonObjectRequest
                                (Request.Method.POST, coursesUrl, null, resp -> {
                                    //get values from json
                                    try{
                                        int k = 0;
                                        String classString;
                                        //build courses with values
                                        JSONArray respCourses = (JSONArray) resp.get("courses");
                                        int respCoursesLength = respCourses.length();
                                        while(k < respCoursesLength){
                                            classString = respCourses.getString(k++);
                                            courses.add(gson.fromJson(classString, Course.class));
                                        }
                                        //add equivalencies
                                        k=0;
                                        JSONArray respEquivs = (JSONArray) resp.get("equivalencies");
                                        int respEquivsLength = respEquivs.length();
                                        while(k < respEquivsLength){
                                            classString = respEquivs.getString(k++);
                                            Equivalency equiv = gson.fromJson(classString, Equivalency.class);
                                            //add equivalency to main list
                                            courses.add(equiv);
                                        }
                                        //add regexes
                                        k=0;
                                        JSONArray respRegexes = (JSONArray)resp.get("regexes");
                                        int respRegexesLength = respRegexes.length();
                                        while(k < respRegexesLength){
                                            classString = respRegexes.getString(k++);
                                            Regex regex = gson.fromJson(classString, Regex.class);
                                            //add regex to main list
                                            courses.add(regex);
                                        }
                                        req.setCoursesTaken(courses);
                                        requirementsAdapter.notifyDataSetChanged();
                                    } catch(Exception e){
                                        System.out.println(e.toString());
                                    }

                                }, error -> {
                                    // TODO: Handle error
                                    System.out.println(error);
                                });
                        requests.add(coursesCERORequest);

                    }


                    // Convert untaken course strings into CourseItem objects to put into the list
                    Log.d("Help", "Untaken Courses Request URL Param: " + coursesStringArr[1]);
                    String untakenCoursesUrl = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getCERObjects?wrappedCourseListString="+coursesStringArr[1] + "&netID="+netID;
                    if (untakenCoursesUrl.length() > 1) {
                        JsonObjectRequest untakenCoursesCERORequest = new JsonObjectRequest
                                (Request.Method.POST, untakenCoursesUrl, null, resp -> {
                                    //get values from json
                                    try{
                                        int k = 0;
                                        String classString;
                                        //build courses with values
                                        JSONArray respCourses = (JSONArray) resp.get("courses");
                                        int respCoursesLength = respCourses.length();
                                        while(k < respCoursesLength){
                                            classString = respCourses.getString(k++);
                                            untakenCourses.add(gson.fromJson(classString, Course.class));
                                        }
                                        //add equivalencies
                                        k=0;
                                        JSONArray respEquivs = (JSONArray) resp.get("equivalencies");
                                        int respEquivsLength = respEquivs.length();
                                        while(k < respEquivsLength){
                                            classString = respEquivs.getString(k++);
                                            Equivalency equiv = gson.fromJson(classString, Equivalency.class);
                                            //add equivalency to main list
                                            untakenCourses.add(equiv);
                                        }
                                        //add regexes
                                        k=0;
                                        JSONArray respRegexes = (JSONArray)resp.get("regexes");
                                        int respRegexesLength = respRegexes.length();
                                        while(k < respRegexesLength){
                                            classString = respRegexes.getString(k++);
                                            Regex regex = gson.fromJson(classString, Regex.class);

                                            //add regex to main list
                                            untakenCourses.add(regex);
                                        }
                                        req.setUntakenCourses(untakenCourses);
                                    } catch(Exception e){
                                        System.out.println(e.toString());
                                    }

                                }, error -> {
                                    // TODO: Handle error
                                    System.out.println(error);
                                });
                        requests.add(untakenCoursesCERORequest);
                    }

                    reqsTest.add(req);
                    count++;
                }
                requirementLayoutManager = new LinearLayoutManager(this);
                requirementsRecyclerView = findViewById(R.id.requirements_recyclerView);
                requirementsRecyclerView.setHasFixedSize(true);
                requirementsRecyclerView.setLayoutManager(requirementLayoutManager);
                requirementsAdapter = new RequirementsListAdapter(reqsTest);
                requirementsRecyclerView.setAdapter(requirementsAdapter);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }, error -> {
            //TODO: Handle error gracefully
            System.out.println(error);
        });
        getReqsProgress.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requests.add(getReqsProgress);


        // Progress bar
        url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/calcNumProgramFulfilledReqs?netID=" + netID + "&program=" + major_from_intent;
        JsonObjectRequest calcNumProgramFulfilledReqs = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try{
                //int totalRequirements = response.getJSONObject("totalReqs").getInt("$numberDouble");
                //int completedRequirements = response.getJSONObject("numFulfilledReqs").getInt("$numberDouble");
                Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();
                String totalReqsString = response.getString("totalReqs");
                String numFulfilledString = response.getString("numFulfilledReqs");

                Log.d("JSON Parse", "totalReqsString: " + totalReqsString);
                Log.d("JSON Parse", "numFulfilledString: " + numFulfilledString);


                int totalRequirements = gson.fromJson(totalReqsString, Integer.class);
                int completedRequirements = gson.fromJson(numFulfilledString, Integer.class);

                Log.d("JSON Parse", "totalReqs: " + totalReqsString);
                Log.d("JSON Parse", "numFulfilledString: " + numFulfilledString);

                ProgressBar progressBar = findViewById(R.id.overall_progress_bar);
                progressBar.setProgress(completedRequirements);
                progressBar.setMax(totalRequirements);

            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }, error -> {
            //TODO: Handle error gracefully
            System.out.println(error);
        });
        requests.add(calcNumProgramFulfilledReqs);

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
            /*
            case R.id.add_program_item:
                AddProgramDialog dialogProgram = new AddProgramDialog(this);
                dialogProgram.show();
                return true;
            case R.id.add_to_plan_item:
                AddToPlanDialog dialogPlan = new AddToPlanDialog(this);
                dialogPlan.show();
                return true;*/
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
}
