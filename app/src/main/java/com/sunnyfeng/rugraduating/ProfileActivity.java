package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.adapters.CoursesListAdapter;
import com.sunnyfeng.rugraduating.adapters.StringArrayAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Course;

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

        // Set up button
        Button backToMainButton = findViewById(R.id.back_to_main_profile);
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        setUpRecyclerViews();
    }

    private void setUpRecyclerViews() {
        // Test
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(getPrinCommCourse());
        String [] programs = {"Computer Engineering", "Computer Science"};

        // Set up courses recycler view
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView = findViewById(R.id.all_classes_recyclerView);
        coursesRecyclerView.setHasFixedSize(true);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        coursesAdapter = new CoursesListAdapter(courses);
        coursesRecyclerView.setAdapter(coursesAdapter);

        // Set up programs recycler view
        programsLayoutManager = new LinearLayoutManager(this);
        programsRecyclerView = findViewById(R.id.programs_recyclerView);
        programsRecyclerView.setHasFixedSize(true);
        programsRecyclerView.setLayoutManager(programsLayoutManager);
        programsAdapter = new StringArrayAdapter(programs);
        programsRecyclerView.setAdapter(programsAdapter);

        // Set up plan recycler view
        planLayoutManager = new LinearLayoutManager(this);
        planRecyclerView = findViewById(R.id.planned_courses_recyclerView);
        planRecyclerView.setHasFixedSize(true);
        planRecyclerView.setLayoutManager(planLayoutManager);
        planAdapter = new CoursesListAdapter(courses); //TODO: add planned courses to this
        planRecyclerView.setAdapter(planAdapter);
    }

    private Course getPrinCommCourse() {
        Course prinComm = new Course("14:332:301", "Wireless Revolution", 3, "ECE", "SOE",
                "This \"flipped\" undergraduate course provides a broad view of how new technologies, economic forces, political constraints, and competitive warfare have created and shaped the \"wireless revolution\" in the last 50 years.  It offers a view inside the world of corporate management-- how strategies were created and why many have failed—and gives students a chance to develop their own strategic skills by solving real-world problems.  The course includes a historical overview of communications and communication systems, basics of wireless technology, technology and politics of cellular, basics of corporate finance, economics of cellular systems and spectrum auctions, case studies in wireless business strategy, the strategic implications of unregulated spectrum, a comparison of 3G, 4G, 5G and WiFi, IoT and the wireless future. Students are required to interact during the lectures in a flipped classroom setting—necessitating pre-lecture preparation, in-class attendance and participation.",
                null, null);
        return prinComm;
    }
}
