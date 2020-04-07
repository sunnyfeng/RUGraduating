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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;

public class AddClassesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

        // Toolbar with title and options menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Add your classes");
        toolbar.inflateMenu(R.menu.options_menu);

        // Drop down menu for majors
        Spinner spinner = findViewById(R.id.major_spinner_add);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.major_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Set up button
        Button addClassesButton = findViewById(R.id.add_classes_button);
        addClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: maybe go back to profile if it came from profile
                Intent intent = new Intent(AddClassesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

    // Handle major spinner item selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String itemSelected = parent.getItemAtPosition(pos).toString();
        // TODO: on item selected, filter courses list
        Toast.makeText(this,itemSelected + " selected.", Toast.LENGTH_SHORT).show();
    }

    // Handle major spinner item not selected
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO: maybe handle this? or just lead blank
    }
}
