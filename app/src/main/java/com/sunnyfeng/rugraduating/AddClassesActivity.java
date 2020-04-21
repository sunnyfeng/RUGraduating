package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.adapters.StringListAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.User;

import java.util.ArrayList;
import java.util.Iterator;

public class AddClassesActivity extends AppCompatActivity {

    private RecyclerView codesRecyclerView;
    private RecyclerView.Adapter codesAdapter;
    private RecyclerView.LayoutManager codesLayoutManager;

    private ArrayList<String> codes;

    private String result;

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


        // add classes button
        Button addClassButton = findViewById(R.id.add_class_button);
        addClassButton.setOnClickListener(v -> {
            EditText codeEditText = findViewById(R.id.add_class_edit_text);
            String code = codeEditText.getText().toString();
            EditText gradeEditText = findViewById(R.id.grade_edit_text);
            String grade = gradeEditText.getText().toString();

            //TODO: add some error checking here (such as not adding if it's already in there //Amany --> you dont need to do this because i already do it in the mongodb side, if you send me the same code twice, ill only insert the first one
            if (code.length() > 0 && grade.length() > 0) {
                codes.add(code + "-" + grade);
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
            startActivity(i);
            /*
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
