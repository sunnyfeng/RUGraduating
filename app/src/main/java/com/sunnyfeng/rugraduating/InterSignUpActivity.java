package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sunnyfeng.rugraduating.objects.User;


public class InterSignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_signup);

        //check here if user is in database to decide whether to sign-up or login
        //temporary just go to main page
        RequestQueue requests = Volley.newRequestQueue(this);
        User mUser = (User)getApplicationContext();
        String netID = mUser.getNetID();
        String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/doesStudentExist?netID=" + netID;

        JsonObjectRequest doesStudentExist = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try{
                boolean userInDb = response.getBoolean("0");
                if(userInDb){
                    Intent main_page = new Intent(this, TopViewActivity.class);
                    startActivity(main_page);
                } else {
                    Intent signup_page = new Intent(this, SignUpActivity.class);
                    startActivity(signup_page);
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }, error -> {
            //TODO: Handle error gracefully
            System.out.println(error);
        });
        requests.add(doesStudentExist);

    }

}
