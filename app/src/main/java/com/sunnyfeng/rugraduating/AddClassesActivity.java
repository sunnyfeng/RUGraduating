package com.sunnyfeng.rugraduating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.sunnyfeng.rugraduating.adapters.StringListAdapter;
import com.sunnyfeng.rugraduating.objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AddClassesActivity extends AppCompatActivity {

    private RecyclerView codesRecyclerView;
    private RecyclerView.Adapter codesAdapter;
    private RecyclerView.LayoutManager codesLayoutManager;

    private ArrayList<String> codes;

    private String destination;

    //intent keys
    public static final String IS_PLAN_KEY = "isPlan";
    public static final String PROFILE_ACTIVITY_DESTINATION_KEY = "destination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

        Intent intent = getIntent();
        if(intent.hasExtra(PROFILE_ACTIVITY_DESTINATION_KEY)) destination = intent.getExtras().getString(PROFILE_ACTIVITY_DESTINATION_KEY);
        else destination = " ";

        final boolean isPlan;
        if(intent.hasExtra(IS_PLAN_KEY)) isPlan = intent.getExtras().getBoolean(IS_PLAN_KEY);
        else isPlan = false;

        if(isPlan){
            TextView view = findViewById(R.id.classes_taken_text_display);
            view.setText("Add Planned Courses");
        }

        codes = new ArrayList<>();
        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        if(isPlan) toolbar.setSubtitle("Add Planned Courses");
        else toolbar.setSubtitle("Add Courses");

        toolbar.inflateMenu(R.menu.options_menu);

        //Populate spinner with possible grades
        String gradesList[] = {"A","B+","B","C","D","F"};
        ArrayList<String> grades = new ArrayList<>(Arrays.asList(gradesList));
        ArrayAdapter<String> grade_adapter  =  new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, grades);
        Spinner grade_spin = (Spinner) findViewById(R.id.grade_spinner);

        if(isPlan){
            grade_spin.setVisibility(View.GONE);
        } else{
            grade_spin.setAdapter(grade_adapter);
            grade_adapter.notifyDataSetChanged();
        }

        // add classes button
        Button addClassButton = findViewById(R.id.add_class_button);
        addClassButton.setOnClickListener(v -> {
            hideSoftKeyboard(AddClassesActivity.this);
            EditText codeEditText = findViewById(R.id.add_class_edit_text);
            String code = codeEditText.getText().toString();
            Spinner gradeEditText = findViewById(R.id.grade_spinner);
            String grade = "N";
            if(!isPlan){
                grade = gradeEditText.getSelectedItem().toString();
            }

            //TODO: add some error checking here (such as not adding if it's already in there //Amany --> you dont need to do this because i already do it in the mongodb side, if you send me the same code twice, ill only insert the first one
            if (code.length() > 0 && grade.length() > 0) {
                codes.add(code + "-" + grade);
                codesAdapter.notifyDataSetChanged();
                codeEditText.setText("");
                gradeEditText.setSelection(0);
            } else {
                Toast.makeText(AddClassesActivity.this,
                        "One or more fields are empty, try again.", Toast.LENGTH_SHORT).show();
            }

        });

        // Submit classes button
        Button submitButton = findViewById(R.id.submit_classes_button);
        submitButton.setOnClickListener(v -> {
            // start progress overlay
            View progress_layout = findViewById(R.id.adding_progress_layout);
            progress_layout.setClickable(false);
            progress_layout.setVisibility(View.VISIBLE);

            User mUser = (User)getApplicationContext();
            TextView view = findViewById(R.id.adding_message);
            List<String> phrases = Arrays.asList("fasten your seatbelt", "sit tight", "fire up the engines", "prepare for liftoff", "saddle up",
                    "let's get ready to RUUMMBBLE","don't change the channel","don't blink or you'll miss it", "before you ask","watch and learn");
            Random rand = new Random();
            int randomIndex = rand.nextInt(phrases.size());
            view.setText("Hey " + mUser.getFirstName() + ", " + phrases.get(randomIndex) + ":\nWe're updating your profile!");

            //(new Handler()).postDelayed(this::goToProfileActivity, 5000); // pause 5 sec, then run method

            String courses = "";
            int check = 1;
            for(String eachCode: codes){
                courses += eachCode;
                if(check != codes.size()){
                    courses += ",";
                }
                check++;
            }

            String netID = mUser.getNetID();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/insertTakenCourses?courses={"+courses+"}&netID="+netID;
            JsonObjectRequest buildFulfilled = new JsonObjectRequest
                    (Request.Method.POST, url, null, response -> {
                        //get values from json
                        try{
                            if(destination.equals("TopViewActivity")){
                                goToTopViewActivity();
                            } else {
                                goToProfileActivity();
                            }

                        } catch(Exception e){
                            System.out.println(e.toString());
                        }
                    }, error -> {
                        // TODO: Handle error
                        System.out.println(error);
                    });

            buildFulfilled.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(buildFulfilled);
            //super.onBackPressed(); // go back to previous activity

        });

        // Cancel classes button
        Button cancelButton = findViewById(R.id.cancel_add_classes_button);
        cancelButton.setOnClickListener(v -> {
            // go back to the page before
            if(destination.equals("TopViewActivity")){
                Intent i = new Intent(AddClassesActivity.this, TopViewActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else super.onBackPressed();
        });
    }

    private void goToTopViewActivity() {
        View progress_layout = findViewById(R.id.adding_progress_layout);
        progress_layout.setClickable(true);
        progress_layout.setVisibility(View.GONE);

        Intent mainIntent = new Intent(this, TopViewActivity.class);
        startActivity(mainIntent);
    }

    private void goToProfileActivity() {
        View progress_layout = findViewById(R.id.adding_progress_layout);
        progress_layout.setClickable(true);
        progress_layout.setVisibility(View.INVISIBLE);

        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void setUpRecyclerViews() {
        // Set up codes recycler view
        codesLayoutManager = new LinearLayoutManager(this);
        codesRecyclerView = findViewById(R.id.classes_recyclerView);
        codesRecyclerView.setHasFixedSize(true);
        codesRecyclerView.setLayoutManager(codesLayoutManager);
        codesAdapter = new StringListAdapter(codes);
        codesRecyclerView.setAdapter(codesAdapter);
    }

    /*
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
    }*/
}
