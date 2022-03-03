package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.LoginIconFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 900914;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_icon_fragment, new LoginIconFragment()).addToBackStack("login_icon_frag").commit();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        if (!checkLoginStatus())
        {
            // Not signed in, initialise all the necessary components for
            // the login activity.
            TextView forgotten_password_tv = findViewById(R.id.forgotten_password_tv);
            LoginButton fb_login_btn = findViewById(R.id.fb_login_button);
            SignInButton google_login_btn = findViewById(R.id.google_login_button);
            Button sign_up_btn = findViewById(R.id.sign_up_btn);
            sign_up_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });

            fb_login_btn.setPermissions(Arrays.asList("email", "public_profile"));
            // If you are using in a fragment, call loginButton.setFragment(this);

            // Callback registration
            fb_login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(@NonNull FacebookException exception) {
                    // App code
                }
            });

            google_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.google_login_button) {
                        googleSignIn();
                    }
                }
            });

            forgotten_password_tv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(LoginActivity.this, "forgotten password textview clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            // User have signed in successfully
            this.navigateToLandingActivity();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null)
            {
                loadGoogleUserProfile(account);
                navigateToLandingActivity();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(R.string.LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
            loadGoogleUserProfile(null);
        }
    }

//    AccessTokenTracker fbTokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
//            if (accessToken1 == null)
//            {
//                /*Current access token is null, user logged out...*/
//
//            }
//            else
//            {
//                loadFBUserProfile(accessToken1);
//            }
//        }
//    };

    private void loadFBUserProfile(AccessToken newAccessToken) {
        /*Instantiate a request*/
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                try {
                    assert jsonObject != null;
                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    String email = jsonObject.getString("email");
                    String id = jsonObject.getString("id");
                    String user_icon_url = "https://graph.facebook.com/"+id+"/picture?type=normal";
                    Toast.makeText(LoginActivity.this, "Facebook Login: First Name: "+first_name+", Last Name: "+last_name+", Email: "+email+", Id: "+id+";", Toast.LENGTH_LONG).show();

                    /*Now perform actions with the data obtained...*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync(); // Now execute the request with the parameters
    }

    private void loadGoogleUserProfile(GoogleSignInAccount account)
    {
        if (account != null)
        {
            String first_name = account.getGivenName();
            String last_name = account.getFamilyName();
            String email = account.getEmail();
            String id = account.getId();
            String user_icon_url = account.getPhotoUrl().toString();
            Toast.makeText(LoginActivity.this, "Google Login: First Name: "+first_name+", Last Name: "+last_name+", Email: "+email+", Id: "+id+";", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkFBLoginStatus()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn)
            loadFBUserProfile(accessToken);  // Load user data automatically

        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(@NonNull AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
            }
            @Override
            public void onFailure() {
                // No access token could be retrieved for the user
            }
            @Override
            public void onError(@NonNull Exception exception) {
                // An error occurred
            }
        });

        return isLoggedIn;
    }

    private boolean checkGoogleLoginStatus()
    {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        loadGoogleUserProfile(account);

        return account != null;
    }

    private boolean checkLoginStatus()
    {
        return checkFBLoginStatus() || checkGoogleLoginStatus();
    }

    private void navigateToLandingActivity()
    {
        finish(); // Avoid the users being able to navigate back to this login activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}