package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sunnyfeng.rugraduating.dialogs.AddCourseDialog;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;

public class ProfileActivity extends AppCompatActivity {

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
            AddCourseDialog dialogCourse = new AddCourseDialog(ProfileActivity.this);
            dialogCourse.show();
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

        // TODO: Add many button listeners
        // TODO: add the recycler view stuff
    }
}
