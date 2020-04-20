package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.adapters.StringArrayAdapter;
import com.sunnyfeng.rugraduating.objects.User;


public class BuildStudentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_student);

        RequestQueue queue = Volley.newRequestQueue(this);
        User user = ((User)getApplicationContext());
        String netID = "aja193";
        String courses = ""; //should come from previous intent
        String url ="https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/allFunctions?netID="+netID+"?courses="+courses;
        //get programs
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    //get values from json
                    try{
                        System.out.println(response);
                        Intent mainIntent = new Intent(this, ProfileActivity.class);
                        startActivity(mainIntent);
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                }, error -> {
                    // TODO: Handle error
                    System.out.println(error);
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

}
