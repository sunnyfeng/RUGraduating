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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.progresviews.ProgressWheel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sunnyfeng.rugraduating.dialogs.AddProgramDialog;
import com.sunnyfeng.rugraduating.dialogs.AddToPlanDialog;
import com.sunnyfeng.rugraduating.objects.User;

import static com.sunnyfeng.rugraduating.ProfileActivity.PROFILE_COMING_FROM_KEY;

public class TopViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String selectedMajor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topview);

        // Toolbar with title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Overall View");
        toolbar.inflateMenu(R.menu.options_menu);

        // Drop down menu for majors
        Spinner spinner = findViewById(R.id.major_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.major_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Progress wheel
        // From: https://github.com/zekapp/Android-ProgressViews
        int totalRequirements = 10;
        int completedRequirements = 3;
        float percentage = ((float)completedRequirements)/totalRequirements * 100;
        int wheelPercentage = (int)((percentage * 358)/100); // For some reason, it goes up to 358% ?
        ProgressWheel progressWheel = findViewById(R.id.progress_wheel);
        progressWheel.setPercentage(wheelPercentage);
        progressWheel.setStepCountText((int)percentage + "%"); //sets text in the middle

        // Set encouraging words based on percentage done
        TextView encouragingWords = findViewById(R.id.encouraging_words);
        encouragingWords.setText(getEncouragingWords(percentage));

        // Go to suggested courses button
        Button specificButton = findViewById(R.id.see_specific_button);
        specificButton.setOnClickListener(v -> {
            Intent intent = new Intent(TopViewActivity.this, MajorActivity.class);
            intent.putExtra(MajorActivity.MAJOR_INTENT_KEY, selectedMajor);
            startActivity(intent);
        });
    }

    private String getEncouragingWords(float percentage) {
        String answer = "You can do this!";
        if (percentage == 0.0) {
            answer = "Let's get started!";
        } else if (0.0 < percentage && percentage < 25.0) {
            answer = "You're off to a great start!";
        } else if (25.0 <= percentage && percentage < 50.0) {
            answer = "You're on your way to graduation!";
        } else if (50.0 <= percentage && percentage < 75.0) {
            answer = "You're making great progress!";
        } else if (75.0 <= percentage && percentage < 100.0) {
            answer = "You're almost there!";
        } else if (percentage == 100.0) {
            answer = "You made it to graduation!";
        }
        return answer;
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
            /*
            case R.id.add_program_item:
                AddProgramDialog dialogProgram = new AddProgramDialog(this);
                dialogProgram.show();
                return true;
            case R.id.add_to_plan_item:
                AddToPlanDialog dialogPlan = new AddToPlanDialog(this);
                dialogPlan.show();
                return true;*/
            case R.id.logout_item:
                Intent intent_logout = new Intent(this, LoginActivity.class);
                User mUser = (User)getApplicationContext();
                mUser.getSclient().signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
                // don't allow back press to re-enter
                intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                return true;
            case R.id.profile_item:
                Intent intent_profile = new Intent(this, ProfileActivity.class);
                intent_profile.putExtra(PROFILE_COMING_FROM_KEY, "TopViewActivity");
                startActivity(intent_profile);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Handle major spinner item selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String itemSelected = parent.getItemAtPosition(pos).toString();
        selectedMajor = itemSelected;
        Log.d("SUNNY", "major: " + selectedMajor);
    }

    // Handle major spinner item not selected
    public void onNothingSelected(AdapterView<?> parent) {
        selectedMajor = "";
    }
}
