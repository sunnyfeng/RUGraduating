package com.sunnyfeng.rugraduating;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.adapters.StringArrayAdapter;
import com.sunnyfeng.rugraduating.objects.User;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;


public class BuildStudentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_student);

        RequestQueue queue = Volley.newRequestQueue(this);
        User mUser = ((User)getApplicationContext());
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
