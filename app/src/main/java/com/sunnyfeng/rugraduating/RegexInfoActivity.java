package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.adapters.CourseItemListAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Regex;

import java.util.ArrayList;

public class RegexInfoActivity extends AppCompatActivity {

    private RecyclerView regexRecyclerView;
    private RecyclerView.Adapter regexAdapter;
    private RecyclerView.LayoutManager regexLayoutManager;

    private Regex regex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regex_info);

        Intent intent = getIntent();
        Regex regex = (Regex) intent.getSerializableExtra(MajorActivity.REGEX_INTENT_KEY);
        this.regex = regex;

        TextView title = findViewById(R.id.regex_title);
        title.setText(regex.getTitle());

        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Regex Information");
        toolbar.inflateMenu(R.menu.options_menu);

        // Back to main button
        Button backToMainButton = findViewById(R.id.back_to_main_regex);
        backToMainButton.setOnClickListener(v -> {
            Intent intentMain = new Intent(RegexInfoActivity.this, TopViewActivity.class);
            startActivity(intentMain);
        });
    }

    private void setUpRecyclerViews() {
        // Set up regex recycler view
        regexLayoutManager = new LinearLayoutManager(this);
        regexRecyclerView = findViewById(R.id.regex_recyclerView);
        regexRecyclerView.setHasFixedSize(true);
        regexRecyclerView.setLayoutManager(regexLayoutManager);
        regexAdapter = new CourseItemListAdapter(new ArrayList<>(regex.getCourses()));
        regexRecyclerView.setAdapter(regexAdapter);
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
        Log.d(this.getLocalClassName(), "Selected Item: " + item.getTitle());
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
