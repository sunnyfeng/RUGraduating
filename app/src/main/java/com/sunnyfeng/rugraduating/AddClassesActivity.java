package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.adapters.StringListAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;

import java.util.ArrayList;

public class AddClassesActivity extends AppCompatActivity {

    private RecyclerView codesRecyclerView;
    private RecyclerView.Adapter codesAdapter;
    private RecyclerView.LayoutManager codesLayoutManager;

    private ArrayList<String> codes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

        codes = new ArrayList<>();
        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Add Courses");
        toolbar.inflateMenu(R.menu.options_menu);


        // add class classes button
        Button addClassButton = findViewById(R.id.add_class_button);
        addClassButton.setOnClickListener(v -> {
            EditText codeEditText = findViewById(R.id.add_class_edit_text);
            String code = codeEditText.getText().toString();
            EditText gradeEditText = findViewById(R.id.grade_edit_text);
            String grade = gradeEditText.getText().toString();

            //TODO: add some error checking here, maybe not adding if it's already in there
            if (code.length() > 0 && grade.length() > 0) {
                codes.add(code + ": " + grade);
                codesAdapter.notifyDataSetChanged();
                codeEditText.setText("");
                gradeEditText.setText("");
            } else {
                Toast.makeText(AddClassesActivity.this,
                        "One or more fields are empty, try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Submit classes button
        Button submitButton = findViewById(R.id.submit_classes_button);
        submitButton.setOnClickListener(v -> {
            //TODO: insert new classes into database
            super.onBackPressed(); // go back to previous activity
        });

        // Cancel classes button
        Button cancelButton = findViewById(R.id.cancel_add_classes_button);
        cancelButton.setOnClickListener(v -> {
            // go back to the page before
            super.onBackPressed();
        });
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
