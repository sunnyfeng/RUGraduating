package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

import com.sunnyfeng.rugraduating.adapters.StringListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sunnyfeng.rugraduating.MajorActivity.MAJOR_INTENT_KEY;
import static com.sunnyfeng.rugraduating.ProfileActivity.PROFILE_COMING_FROM_KEY;
import static com.sunnyfeng.rugraduating.SuggestedCoursesActivity.SUGGESTED_COURSES_OBJECT_KEY;

public class AddClassesActivity extends AppCompatActivity {

    private RecyclerView codesRecyclerView;
    private RecyclerView.Adapter codesAdapter;
    private RecyclerView.LayoutManager codesLayoutManager;

    private ArrayList<String> codes;

    private String result;
    private String coming_from;
    private String major_from_intent;
    private String suggested_courses_from_intent;

    //intent keys
    public static final String IS_PLAN_KEY = "isPlan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

        Intent intent = getIntent();
        coming_from = intent.getStringExtra(PROFILE_COMING_FROM_KEY);
        major_from_intent = (String) intent.getSerializableExtra(MAJOR_INTENT_KEY);
        suggested_courses_from_intent = intent.getStringExtra(SUGGESTED_COURSES_OBJECT_KEY);

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

            (new Handler()).postDelayed(this::goToProfileActivity, 5000); // pause 5 sec, then run method

/*
            String courses = "";
            int check = 1;
            for(String eachCode: codes){
                courses += eachCode;
                if(check != codes.size()){
                    courses += ",";
                }
                check++;
            }

            //pass url to buildStudentActivity Intent
            Intent i = new Intent(this, BuildStudentActivity.class);
            i.putExtra("courses", courses);
            if(intent.hasExtra("destination")){
                i.putExtra("destination", intent.getExtras().getString("destination"));
            }
            i.putExtra(MAJOR_INTENT_KEY, major_from_intent);
            i.putExtra(PROFILE_COMING_FROM_KEY, coming_from);
            i.putExtra(SUGGESTED_COURSES_OBJECT_KEY, suggested_courses_from_intent);
            startActivity(i);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, response -> {
                        //get values from json
                        try{
                            result = response.getString("0");
                            System.out.println("RESULT: " + result);
                            //build Future after showing courses, then go to profile
                            Intent main_page = new Intent(this, BuildStudentActivity.class);
                            startActivity(main_page);

                            if(result.compareTo("") != 0){
                                //TODO: it shouldnt go back to prev screen, it should the classes that couldn't be added
                            }
                        } catch(Exception e){
                            Log.d("error", e.toString());
                        }

                    }, error -> {
                        // TODO: Handle error
                        Log.d("error", error.toString());
                    });
            queue.add(jsonObjectRequest);
            */
            //super.onBackPressed(); // go back to previous activity

        });

        // Cancel classes button
        Button cancelButton = findViewById(R.id.cancel_add_classes_button);
        cancelButton.setOnClickListener(v -> {
            // go back to the page before
            this.onBackPressed();
        });
    }

    private void goToProfileActivity() {
        View progress_layout = findViewById(R.id.adding_progress_layout);
        progress_layout.setClickable(true);
        progress_layout.setVisibility(View.INVISIBLE);

        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(AddClassesActivity.this, ProfileActivity.class);
        System.out.println(coming_from);
        i.putExtra(PROFILE_COMING_FROM_KEY, coming_from);
        i.putExtra(MAJOR_INTENT_KEY, major_from_intent);
        i.putExtra(SUGGESTED_COURSES_OBJECT_KEY, suggested_courses_from_intent);
        startActivity(i);
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
