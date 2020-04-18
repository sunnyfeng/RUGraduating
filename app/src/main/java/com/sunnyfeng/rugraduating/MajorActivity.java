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

import com.sunnyfeng.rugraduating.adapters.RequirementsListAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Requirement;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get major from intent
        Intent intent = getIntent();
        String major_from_intent = (String) intent.getSerializableExtra(MajorActivity.MAJOR_INTENT_KEY);

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
            Intent suggestedIntent = new Intent(MajorActivity.this, SuggestedCoursesActivity.class);
            startActivity(suggestedIntent);
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

        // Progress bar
        int totalRequirements = 10;
        int completedRequirements = 3;
        ProgressBar progressBar = findViewById(R.id.overall_progress_bar);
        progressBar.setProgress(completedRequirements);
        progressBar.setMax(totalRequirements);
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
}
