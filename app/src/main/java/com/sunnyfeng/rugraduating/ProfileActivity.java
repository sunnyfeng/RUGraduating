package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Button modifyClassesButton = findViewById(R.id.modify_classes_button);
        modifyClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AddClassesActivity.class);
                startActivity(intent);
            }
        });
        Button addProgramButton = findViewById(R.id.add_program_button);
        addProgramButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddProgramDialog dialogProgram = new AddProgramDialog(ProfileActivity.this);
                dialogProgram.show();
            }
        });
        Button addToPlanButton = findViewById(R.id.add_to_plan_button);
        addToPlanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddToPlanDialog dialogPlan = new AddToPlanDialog(ProfileActivity.this);
                dialogPlan.show();
            }
        });

        // Set up button
        Button backToMainButton = findViewById(R.id.back_to_main_profile);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // TODO: Add many button listeners
        // TODO: add the recycler view stuff
    }
}
