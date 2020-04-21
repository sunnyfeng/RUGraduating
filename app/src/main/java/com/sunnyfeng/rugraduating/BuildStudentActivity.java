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

public class BuildStudentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_student);
        User mUser = ((User)getApplicationContext());

        List<String> phrases = Arrays.asList("fasten your seatbelt", "sit tight", "fire up the engines", "prepare for liftoff", "saddle up",
                "let's get ready to RUUMMBBLE","don't change the channel","don't blink or you'll miss it", "before you ask","watch and learn");
        Random rand = new Random();
        int randomIndex = rand.nextInt(phrases.size());

        TextView view = findViewById(R.id.building_student);
        view.setText("Hey " + mUser.getFirstName() + ", " + phrases.get(randomIndex) + ":\nWe're updating your plan!");

        RequestQueue queue = Volley.newRequestQueue(this);
        String netID = mUser.getNetID();
        Intent intent = getIntent();
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

}
