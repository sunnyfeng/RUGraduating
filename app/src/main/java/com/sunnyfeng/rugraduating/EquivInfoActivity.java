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

import com.sunnyfeng.rugraduating.adapters.StringArrayAdapter;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.Equivalency;

public class EquivInfoActivity extends AppCompatActivity {

    private RecyclerView equivRecyclerView;
    private RecyclerView.Adapter equivAdapter;
    private RecyclerView.LayoutManager equivLayoutManager;

    private Equivalency equivalency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equiv_info);

        Intent intent = getIntent();
        Equivalency equiv = (Equivalency) intent.getSerializableExtra(MainActivity.EQUIV_INTENT_KEY);
        equivalency = equiv;

        TextView title = findViewById(R.id.equiv_title);
        title.setText(equivalency.getTitle());

        setUpRecyclerViews();

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Equivalency Information");
        toolbar.inflateMenu(R.menu.options_menu);

        // Set up button
        Button backToMainButton = findViewById(R.id.back_to_main_equiv);
        backToMainButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(EquivInfoActivity.this, MainActivity.class);
            startActivity(intent1);
        });
    }

    private void setUpRecyclerViews() {

        // Set up equiv recycler view
        equivLayoutManager = new LinearLayoutManager(this);
        equivRecyclerView = findViewById(R.id.equiv_recyclerView);
        equivRecyclerView.setHasFixedSize(true);
        equivRecyclerView.setLayoutManager(equivLayoutManager);
        //TODO: save equivs as Course objects
        equivAdapter = new StringArrayAdapter(equivalency.getCourses());
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
    }
}
