package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.adapters.StringArrayAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Course;

public class CourseInfoActivity extends AppCompatActivity {

    private RecyclerView prereqRecyclerView;
    private RecyclerView.Adapter prereqAdapter;
    private RecyclerView.LayoutManager prereqLayoutManager;

    private RecyclerView coreqRecyclerView;
    private RecyclerView.Adapter coreqAdapter;
    private RecyclerView.LayoutManager coreqLayoutManager;

    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra(MajorActivity.COURSE_INTENT_KEY);
        currentCourse = course;

        TextView className = findViewById(R.id.class_name);
        className.setText(course.getName());
        TextView classCode = findViewById(R.id.class_code);
        classCode.setText(course.get_id());
        TextView classCredit = findViewById(R.id.credit_num_display);
        classCredit.setText(String.valueOf(course.getCredits()));
        TextView description = findViewById(R.id.class_description_display);
        description.setText(course.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod()); //make scrollable

        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Course Information");
        toolbar.inflateMenu(R.menu.options_menu);

        // Back to main button
        Button backToMainButton = findViewById(R.id.back_to_main_course);
        backToMainButton.setOnClickListener(v -> {
            Intent intentMain = new Intent(CourseInfoActivity.this, TopViewActivity.class);
            startActivity(intentMain);
        });
    }

    private void setUpRecyclerViews() {
        // Set up prereq recycler view
        prereqLayoutManager = new LinearLayoutManager(this);
        prereqRecyclerView = findViewById(R.id.prereq_recyclerView);
        prereqRecyclerView.setHasFixedSize(true);
        prereqRecyclerView.setLayoutManager(prereqLayoutManager);
        //TODO: save prereqs as Course objects and use CourseItemListAdapter instead
        prereqAdapter = new StringArrayAdapter(currentCourse.getPrereqs().toArray(new String[0]));
        prereqRecyclerView.setAdapter(prereqAdapter);

        // Set up coreq recycler view
        coreqLayoutManager = new LinearLayoutManager(this);
        coreqRecyclerView = findViewById(R.id.coreq_recyclerView);
        coreqRecyclerView.setHasFixedSize(true);
        coreqRecyclerView.setLayoutManager(coreqLayoutManager);
        //TODO: save coreqas as Course objects and use CourseItemListAdapter instead
        coreqAdapter = new StringArrayAdapter(currentCourse.getCoreqs().toArray(new String[0]));
        coreqRecyclerView.setAdapter(coreqAdapter);
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
