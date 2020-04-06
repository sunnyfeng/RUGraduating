package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CourseInfoActivity extends AppCompatActivity {

    private RecyclerView prereqRecyclerView;
    private RecyclerView.Adapter prereqAdapter;
    private RecyclerView.LayoutManager prereqLayoutManager;

    private RecyclerView equivRecyclerView;
    private RecyclerView.Adapter equivAdapter;
    private RecyclerView.LayoutManager equivLayoutManager;

    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra(MainActivity.COURSE_INTENT_KEY);
        currentCourse = course;

        TextView className = findViewById(R.id.class_name);
        className.setText(course.title);
        TextView classCode = findViewById(R.id.class_code);
        classCode.setText(course.code);
        TextView classCredit = findViewById(R.id.credit_num_display);
        classCredit.setText(String.valueOf(course.numCredits));
        TextView description = findViewById(R.id.class_description_display);
        description.setText(course.description);

        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Course Information");
        toolbar.inflateMenu(R.menu.options_menu);

        // Set up button
        Button backToMainButton = findViewById(R.id.back_to_main_course);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerViews() {
        // Set up prereq recycler view
        prereqLayoutManager = new LinearLayoutManager(this);
        prereqRecyclerView = findViewById(R.id.prereq_recyclerView);
        prereqRecyclerView.setHasFixedSize(true);
        prereqRecyclerView.setLayoutManager(prereqLayoutManager);
        prereqAdapter = new MainActivity_SuggestedCoursesRV_Adapter(currentCourse.getPrereqs());
        prereqRecyclerView.setAdapter(prereqAdapter);

        // Set up equiv recycler view
        equivLayoutManager = new LinearLayoutManager(this);
        equivRecyclerView = findViewById(R.id.equivalencies_recyclerView);
        equivRecyclerView.setHasFixedSize(true);
        equivRecyclerView.setLayoutManager(equivLayoutManager);
        equivAdapter = new MainActivity_SuggestedCoursesRV_Adapter(currentCourse.getEquivs());
        equivRecyclerView.setAdapter(equivAdapter);
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
}
