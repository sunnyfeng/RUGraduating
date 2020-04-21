package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.objects.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BuildStudentActivity extends AppCompatActivity {
    TextView view;
    User mUser;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_student);
        mUser = ((User)getApplicationContext());
        intent = getIntent();
        view = findViewById(R.id.building_student);

        String buildType;
        if(intent.hasExtra("buildType")) buildType = intent.getExtras().getString("buildType");
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
                        i.putExtra("response", response.toString());
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