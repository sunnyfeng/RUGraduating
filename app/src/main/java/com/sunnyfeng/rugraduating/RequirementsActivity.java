package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.adapters.CourseItemListAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.CourseItem;
import com.sunnyfeng.rugraduating.objects.Requirement;

import java.util.ArrayList;

public class RequirementsActivity extends AppCompatActivity {

    private RecyclerView completedRecyclerView;
    private RecyclerView.Adapter completedAdapter;
    private RecyclerView.LayoutManager completedLayoutManager;

    private RecyclerView fulfillRecyclerView;
    private RecyclerView.Adapter fulfillAdapter;
    private RecyclerView.LayoutManager fulfillLayoutManager;

    private Requirement currentReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Requirements");
        toolbar.inflateMenu(R.menu.options_menu);

        // Get requirement
        Intent intent = getIntent();
        Requirement requirement = (Requirement) intent.getSerializableExtra(MainActivity.REQUIREMENT_INTENT_KEY);
        currentReq = requirement;
        TextView reqName = findViewById(R.id.requirement_name);
        reqName.setText(requirement.title);
        ProgressBar progBar = findViewById(R.id.progressBar);
        progBar.setMax(requirement.totalRequired);
        progBar.setProgress(requirement.alreadyCompleted);

        //TODO: get from database how many student has completed and how many more they need to
        // complete
        TextView completedNum = findViewById(R.id.completed_courses_credits);
        int completed = currentReq.alreadyCompleted;
        TextView remainingNum = findViewById(R.id.remaining_classes_credits);
        int remaining = currentReq.totalRequired - completed;

        if (currentReq.isCredits) {
            completedNum.setText(completed + " credits");
            remainingNum.setText(remaining + " credits");
        } else {
            completedNum.setText(completed + " courses");
            remainingNum.setText(remaining + " courses");
        }

        setUpRecyclerViews();

        // Set up button
        Button backToMainButton = findViewById(R.id.back_to_main_req);
        backToMainButton.setOnClickListener(v -> {
            Intent intentMain = new Intent(RequirementsActivity.this, MainActivity.class);
            startActivity(intentMain);
        });
    }

    private void setUpRecyclerViews() {
        // Set up completed recycler view
        completedLayoutManager = new LinearLayoutManager(this);
        completedRecyclerView = findViewById(R.id.completed_courses_recyclerView);
        completedRecyclerView.setHasFixedSize(true);
        completedRecyclerView.setLayoutManager(completedLayoutManager);
        completedAdapter = new CourseItemListAdapter(currentReq.getCoursesTaken());
        completedRecyclerView.setAdapter(completedAdapter);

        // Set up fulfill recycler view
        fulfillLayoutManager = new LinearLayoutManager(this);
        fulfillRecyclerView = findViewById(R.id.remaining_recyclerView);
        fulfillRecyclerView.setHasFixedSize(true);
        fulfillRecyclerView.setLayoutManager(fulfillLayoutManager);
        fulfillAdapter = new CourseItemListAdapter(getAllCoursesThatSatisfy(currentReq));
        fulfillRecyclerView.setAdapter(fulfillAdapter);
    }

    private ArrayList<CourseItem> getAllCoursesThatSatisfy(Requirement req) {
        // TODO: get all classes that can fulfill this requirement from DB
        return new ArrayList<>();
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
