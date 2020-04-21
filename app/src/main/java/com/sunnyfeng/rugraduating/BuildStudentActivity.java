
/*
package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.objects.User;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.sunnyfeng.rugraduating.MajorActivity.MAJOR_INTENT_KEY;
import static com.sunnyfeng.rugraduating.ProfileActivity.PROFILE_COMING_FROM_KEY;
import static com.sunnyfeng.rugraduating.SuggestedCoursesActivity.SUGGESTED_COURSES_OBJECT_KEY;

public class BuildStudentActivity extends AppCompatActivity {
    TextView view;
    User mUser;
    Intent intent;
    private String major_from_intent;
    private String suggested_courses_from_intent;

    //intent keys
    public static final String BUILD_STUDENT_TYPE_KEY = "buildType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_student);
        mUser = ((User)getApplicationContext());
        intent = getIntent();
        view = findViewById(R.id.building_student);
        major_from_intent = (String) intent.getSerializableExtra(MajorActivity.MAJOR_INTENT_KEY);
        suggested_courses_from_intent = intent.getStringExtra(SUGGESTED_COURSES_OBJECT_KEY);

        String buildType;
        if(intent.hasExtra(BUILD_STUDENT_TYPE_KEY)) buildType = intent.getExtras().getString(BUILD_STUDENT_TYPE_KEY);
        else buildType = "addCourses";

        List<String> phrases = Arrays.asList("fasten your seatbelt", "sit tight", "fire up the engines", "prepare for liftoff", "saddle up",
                "let's get ready to RUUMMBBLE","don't change the channel","don't blink or you'll miss it", "before you ask","watch and learn");
        Random rand = new Random();
        int randomIndex = rand.nextInt(phrases.size());

        if(buildType.equals("buildSuggested")) buildSuggested(phrases.get(randomIndex));
        else addCourses(phrases.get(randomIndex));


    }

    private void addCourses(String phrase){
        view.setText("Hey " + mUser.getFirstName() + ", " + phrase + ":\nWe're updating your profile!");

        RequestQueue queue = Volley.newRequestQueue(this);
        String netID = mUser.getNetID();
        final String destination;
        if(intent.hasExtra("destination")) destination = intent.getExtras().getString("destination");
        else destination = " ";
        String courses = intent.getExtras().getString("courses");
        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/insertTakenCourses?courses={"+courses+"}&netID="+netID;
        JsonObjectRequest buildFulfilled = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        System.out.println(response);
                        if(destination.equals("TopViewActivity")){
                            Intent mainIntent = new Intent(this, TopViewActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Intent mainIntent = new Intent(this, ProfileActivity.class);
                            mainIntent.putExtra(MAJOR_INTENT_KEY, major_from_intent);
                            mainIntent.putExtra(PROFILE_COMING_FROM_KEY, intent.getStringExtra(PROFILE_COMING_FROM_KEY));
                            mainIntent.putExtra(SUGGESTED_COURSES_OBJECT_KEY, suggested_courses_from_intent);
                            startActivity(mainIntent);
                        }

                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        buildFulfilled.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(buildFulfilled);
    }

    private void buildSuggested(String phrase){
        view.setText("Hey " + mUser.getFirstName() + ", " + phrase + ":\nWe're calculating your options!");

        RequestQueue queue = Volley.newRequestQueue(this);
        String netID = mUser.getNetID();
        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/getSuggestedCourses?netID="+netID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        Intent i = new Intent(this, SuggestedCoursesActivity.class);
                        i.putExtra(SUGGESTED_COURSES_OBJECT_KEY, response.toString());
                        i.putExtra(MAJOR_INTENT_KEY, major_from_intent);
                        startActivity(i);
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

}

*/
