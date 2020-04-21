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
import com.sunnyfeng.rugraduating.objects.Course;
import com.sunnyfeng.rugraduating.objects.CourseItem;
import com.sunnyfeng.rugraduating.objects.Equivalency;
import com.sunnyfeng.rugraduating.objects.Regex;
import com.sunnyfeng.rugraduating.objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SuggestedCoursesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView suggestedRecyclerView;
    private RecyclerView.Adapter suggestedAdapter;
    private RecyclerView.LayoutManager suggestedLayoutManager;

    private JSONObject suggestedCoursesObject;
    private JSONObject levelObject;
    private JSONObject programObject;
    private HashMap<String, String[]> requirements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_classes);

        String[] levelArray = {"Loading . . ."};
        // Drop down menu for level
        Spinner levelSpinner = findViewById(R.id.level_spinner);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, levelArray);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);
        levelSpinner.setOnItemSelectedListener(this);

        User mUser = (User)getApplicationContext();

        // start progress overlay
        View progress_layout = findViewById(R.id.suggested_progress_layout);
        progress_layout.setClickable(false);
        progress_layout.setVisibility(View.VISIBLE);

        TextView view = findViewById(R.id.suggested_progress_message);
        List<String> phrases = Arrays.asList("fasten your seatbelt", "sit tight", "fire up the engines", "prepare for liftoff", "saddle up",
                "let's get ready to RUUMMBBLE","don't change the channel","don't blink or you'll miss it", "before you ask","watch and learn");
        Random rand = new Random();
        int randomIndex = rand.nextInt(phrases.size());
        view.setText("Hey " + mUser.getFirstName() + ", " + phrases.get(randomIndex) + ":\nWe're calculating your options!");

        RequestQueue queue = Volley.newRequestQueue(this);
        String netID = mUser.getNetID();
        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getSuggestedCourses?netID="+netID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        suggestedCoursesObject = response;
                        //populate level spinner
                        ArrayList<String> keys = new ArrayList<>();
                        Iterator<String> i = suggestedCoursesObject.keys();
                        do{
                            String key = i.next();
                            if(key.equals("0")) key = "Immediately";
                            else if (key.equals("1")) key = "After 1 Prerequisite";
                            else key = "After " + key + " Prerequisites";
                            keys.add(key);
                        }while(i.hasNext());

                        ArrayAdapter<String> levelAdapterNew = new ArrayAdapter<>
                                (this, android.R.layout.simple_spinner_item, (String[])keys.toArray(new String[0]));
                        levelAdapterNew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        levelSpinner.setAdapter(levelAdapterNew);

                        // end progress overlay
                        progress_layout.setClickable(true);
                        progress_layout.setVisibility(View.INVISIBLE);
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                }, error -> {
                    // end progress overlay
                    progress_layout.setClickable(true);
                    progress_layout.setVisibility(View.INVISIBLE);
                    try {
                        if(error.networkResponse != null) {
                            String res = new String(error.networkResponse.data, "utf-8");
                            Toast.makeText(SuggestedCoursesActivity.this, res, Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(SuggestedCoursesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(SuggestedCoursesActivity.this, "Unsupported encoding.", Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);

        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Suggested Courses");
        toolbar.inflateMenu(R.menu.options_menu);

        String[] programArray = {};
        String[] reqArray = {};

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

        // Back button
        Button backToMainButton = findViewById(R.id.back_to_main_suggested);
        backToMainButton.setOnClickListener(v -> {
            Intent i = new Intent(SuggestedCoursesActivity.this, TopViewActivity.class);
            startActivity(i);
        });
    }

    /*
    @Override
    public void onBackPressed(){
        Intent i = new Intent(SuggestedCoursesActivity.this, MajorActivity.class);
        i.putExtra(MAJOR_INTENT_KEY, major_from_intent);
        startActivity(i);
    }*/


    private void setUpRecyclerViews() {
        // Set up suggested recycler view
        suggestedLayoutManager = new LinearLayoutManager(this);
        suggestedRecyclerView = findViewById(R.id.suggested_recyclerView);
        suggestedRecyclerView.setHasFixedSize(true);
        suggestedRecyclerView.setLayoutManager(suggestedLayoutManager);

        ArrayList<CourseItem> suggestedTest = new ArrayList<>();
        suggestedAdapter = new CourseItemListAdapter(suggestedTest);
        suggestedRecyclerView.setAdapter(suggestedAdapter);
        suggestedAdapter = new CourseItemListAdapter(suggestedTest);
        suggestedRecyclerView.setAdapter(suggestedAdapter);
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
                return true;
             */
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

    static String splitCamelCase(String s) {
        s = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectionValue  = adapterView.getSelectedItem().toString();
        if(selectionValue.equals("Loading . . .")) return;
        //clear courses
        ArrayList<CourseItem> suggestedTest = new ArrayList<>();
        suggestedAdapter = new CourseItemListAdapter(suggestedTest);
        suggestedRecyclerView.setAdapter(suggestedAdapter);
        suggestedAdapter = new CourseItemListAdapter(suggestedTest);
        suggestedRecyclerView.setAdapter(suggestedAdapter);

        Spinner programSpinner = findViewById(R.id.program_spinner);
        Spinner reqSpinner = findViewById(R.id.requirement_spinner);
        ArrayAdapter<String> programAdapter;
        ArrayAdapter<String> reqAdapter;

        switch(adapterView.getId()) {
            //TODO: filter options based on the selections above
            case R.id.level_spinner:
                //clear programs and reqs
                // Drop down menu for program
                programAdapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_spinner_item, new String[0]);
                programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                programSpinner.setAdapter(programAdapter);
                programSpinner.setOnItemSelectedListener(this);

                // Drop down menu for reqs
                reqAdapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_spinner_item, new String[0]);
                reqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reqSpinner.setAdapter(reqAdapter);
                reqSpinner.setOnItemSelectedListener(this);

                //populate program spinner object
                try{
                    String[] selectionValueArray = selectionValue.split(" ");
                    if(selectionValueArray[0].equals("Immediately")) selectionValue = "0";
                    else selectionValue = selectionValueArray[1];
                    levelObject = suggestedCoursesObject.getJSONObject(selectionValue);
                ArrayList<String> keys = new ArrayList<>();
                Iterator<String> keyIter = levelObject.keys();
                do{
                    keys.add(keyIter.next());
                }while(keyIter.hasNext());
                //populate top-level menu with levels
                programAdapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_spinner_item, keys.toArray(new String[0]));
                programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                programSpinner.setAdapter(programAdapter);
                programSpinner.setOnItemSelectedListener(this);
                } catch(Exception e){
                    System.out.println("Error fetching levelObject for level: " + selectionValue);
                }
                break;
            case R.id.program_spinner:
                //clear reqs
                // Drop down menu for reqs
                reqAdapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_spinner_item, new String[0]);
                reqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reqSpinner.setAdapter(reqAdapter);
                reqSpinner.setOnItemSelectedListener(this);

                //populate req spinner object
                try{
                    programObject = levelObject.getJSONObject(selectionValue);
                    requirements = new HashMap<>();
                    Iterator<String> keyIter = programObject.keys();
                    do{
                        String keyString = keyIter.next();
                        String courses = programObject.getJSONArray(keyString).toString();
                        if(courses.length() == 2) continue;
                        courses = courses.substring( 1, courses.length() - 1);
                        String[] key = keyString.split(":");
                        if(key[0].charAt(0) == 'g') key[0] = "School-Level " + splitCamelCase(key[0].substring(2));
                        else key[0] = splitCamelCase(key[0]);
                        String toAdd;
                        if(key[1].equals("-1")) toAdd = key[0];
                        else if(key[1].equals("1")) toAdd = key[0] + " (" + key[1] + " course left)";
                        else toAdd = key[0] + " (" + key[1] + " courses left)";
                        requirements.put(toAdd, courses.split(","));
                    }while(keyIter.hasNext());
                    //populate top-level menu with levels
                    reqAdapter = new ArrayAdapter<>
                            (this, android.R.layout.simple_spinner_item, requirements.keySet().toArray(new String[0]));
                    reqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    reqSpinner.setAdapter(reqAdapter);
                    reqSpinner.setOnItemSelectedListener(this);
                } catch(Exception e){
                    System.out.println("Error fetching programObject for program: " + selectionValue);
                }
                break;
            case R.id.requirement_spinner:
                //populate courses
                String[] courses = requirements.get(selectionValue);
                try {
                    //convert to a string
                    StringBuilder sb = new StringBuilder("{");
                    for(int j = 0; j < courses.length; j++){
                        sb.append(courses[j].substring( 1, courses[j].length() - 1));
                        sb.append(",");
                    }
                    sb.setLength(sb.length() - 1);
                    sb.append("}");
                    String testCoursesString = sb.toString().replace("\\", "");
                    User mUser = (User)getApplicationContext();
                    String netID = mUser.getNetID();
                    //hit mongodb webhook for course data, will update suggestedRecyclerView asynchronously
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url ="https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getCERObjects?wrappedCourseListString="+testCoursesString + "&netID="+netID;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, response -> {
                                //get values from json
                                try{
                                    int k = 0;
                                    String classString;
                                    //build courses with values
                                    Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();
                                    //add courses
                                    JSONArray respCourses = (JSONArray) response.get("courses");
                                    int respCoursesLength = respCourses.length();
                                    while(k < respCoursesLength){
                                        classString = respCourses.getString(k++);
                                        suggestedTest.add(gson.fromJson(classString, Course.class));
                                    }
                                    //add equivalencies
                                    k=0;
                                    JSONArray respEquivs = (JSONArray) response.get("equivalencies");
                                    int respEquivsLength = respEquivs.length();
                                    while(k < respEquivsLength){
                                        classString = respEquivs.getString(k++);
                                        Equivalency equiv = gson.fromJson(classString, Equivalency.class);
                                        //remove not-taken courses (in this level) from equivalency
                                        ArrayList<Course> courseList = equiv.getCourses();
                                        courseList.retainAll(suggestedTest);
                                        equiv.setCourses(courseList);
                                        //remove courses covered by equivalency from main list
                                        for(Course x : courseList){
                                            suggestedTest.remove(x);
                                        }
                                        //add equivalency to main list
                                        suggestedTest.add(equiv);
                                    }
                                    //add regexes
                                    k=0;
                                    JSONArray respRegexes = (JSONArray)response.get("regexes");
                                    int respRegexesLength = respRegexes.length();
                                    while(k < respRegexesLength){
                                        classString = respRegexes.getString(k++);
                                        Regex regex = gson.fromJson(classString, Regex.class);
                                        //remove not-taken courses (in this level) from regex
                                        ArrayList<Course> courseList = regex.getCourses();
                                        courseList.retainAll(suggestedTest);
                                        regex.setCourses(courseList);
                                        //remove courses covered by regex from main list
                                        for(Course x : courseList){
                                            suggestedTest.remove(x);
                                        }
                                        //add regex to main list
                                        suggestedTest.add(regex);
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
                } catch (Exception e){
                    System.out.println("Error fetching courses for req: " + selectionValue);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
