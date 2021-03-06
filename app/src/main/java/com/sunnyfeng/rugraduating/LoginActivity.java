package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sunnyfeng.rugraduating.objects.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    GoogleSignInClient sClient = null;
    int RC_SIGN_IN = 72;
    String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Toolbar with title and options menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RU Graduating");
        }
        toolbar.setSubtitle("Welcome!");

        // fade in logo
        ImageView logo = findViewById(R.id.splash);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        logo.startAnimation(myFadeInAnimation);

        //google stuff
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .setHostedDomain("scarletmail.rutgers.edu")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        sClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = sClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account){
        if(account != null){
            User mUser = ((User)getApplicationContext());
            mUser.setSclient(sClient);
            if(account.getEmail() == null) {
                System.out.println("error fetching user email");
            } else{
                mUser.setNetID(account.getEmail().split("@", 2)[0]);
            }
            if(account.getDisplayName() == null){
                System.out.println("error fetching user name");
            } else {
                String[] toCapitalize = account.getDisplayName().toLowerCase().split(" ");
                if(toCapitalize.length != 0){
                mUser.setFirstName(String.valueOf(toCapitalize[0].charAt(0)).toUpperCase() + toCapitalize[0].substring(1));
                if(toCapitalize.length > 1) mUser.setLastName(String.valueOf(toCapitalize[1].charAt(0)).toUpperCase() + toCapitalize[1].substring(1));
                }
            }
            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setVisibility(View.GONE);

            TextView showSign = findViewById(R.id.show_signing);
            showSign.setVisibility(View.VISIBLE);

            //check here if user is in database to decide whether to sign-up or login
            //temporary just go to main page
            RequestQueue requests = Volley.newRequestQueue(this);
            String netID = mUser.getNetID();
            String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/degreenav-uuidd/service/webhookTest/incoming_webhook/doesStudentExist?netID=" + netID;

            JsonObjectRequest doesStudentExist = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
                try{
                    boolean userInDb = response.getBoolean("0");
                    if(userInDb){
                        Intent main_page = new Intent(this, TopViewActivity.class);
                        main_page.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    // Don't allow back press after logging out
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
