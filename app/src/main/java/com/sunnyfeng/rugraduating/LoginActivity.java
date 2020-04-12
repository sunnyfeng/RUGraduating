package com.sunnyfeng.rugraduating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    GoogleSignInClient sClient = null;
    int RC_SIGN_IN = 72;
    String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: add splash page logo
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
            mUser.setNetID(account.getEmail().split("@",2)[0]);
            mUser.setEmail(account.getEmail());
            String[] toCapitalize = account.getDisplayName().toLowerCase().split(" ");
            mUser.setFirstName(String.valueOf(toCapitalize[0].charAt(0)).toUpperCase() + toCapitalize[0].substring(1));
            mUser.setLastName(String.valueOf(toCapitalize[1].charAt(0)).toUpperCase() + toCapitalize[1].substring(1));
            //check here if user is in database to decide whether to sign-up or login
            //temporary just go to main page
            Intent main_page = new Intent(this, MainActivity.class);
            startActivity(main_page);
        }
    }

    public boolean login_check_populated(){
        boolean complete = true;

        EditText email_textbox = (EditText) findViewById(R.id.email_addr);
        String user_email = email_textbox.getText().toString();
        if(user_email.length() == 0){
            complete = false;
            email_textbox.setError("Please enter your email");
        }

        EditText pwd_textbox = (EditText) findViewById(R.id.pwd);
        String user_pwd = pwd_textbox.getText().toString();
        if(user_pwd.length() == 0){
            complete = false;
            pwd_textbox.setError("Please enter your password");
        }
        return complete;
    }

    public void login(View view){
        if(login_check_populated()) {

            //TODO: check database if user exists
            // if doesn't exist, return


            // else, load intent, send login credentials?
            Intent main_page = new Intent(this, MainActivity.class);
            startActivity(main_page);

        }
    }

    public void sign_up(View view){
        // send straight to sign up page
        Intent signup_page = new Intent(this, SignUpActivity.class);
        startActivity(signup_page);
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
