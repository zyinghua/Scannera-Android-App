package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 900914;
    public static final int NOT_LOGGED_IN = 0;
    public static final int LOCAL_LOGIN = 1;
    public static final int FACEBOOK_LOGIN = 2;
    public static final int GOOGLE_LOGIN = 3;
    public static final String LOGIN_OPTION_KEY = "LOGIN_OPTION";
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private TextInputLayout username_login_input;
    private TextInputLayout password_login_input;
    private TextView forgotten_password_tv;
    private ImageView facebook_login_btn;
    private ImageView google_login_btn;
    private Button login_btn;
    private Button sign_up_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_icon_fragment, new LoginIconFragment()).addToBackStack("login_icon_frag").commit();
        findViews();
        setAnimationsOnStart();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onStart() {
        super.onStart();

        int login_status = checkLoginStatus();

        if (login_status == 0)
        {
            // Not signed in, initialise all the necessary components for
            // the login activity.
            sign_up_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });

            forgotten_password_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, ForgottenPasswordActivity.class);
                    startActivity(intent);
                }
            });

            // Callback registration
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // Successful Facebook login
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    loadFBUserProfile(accessToken);

                    navigateToLandingActivity(FACEBOOK_LOGIN);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Facebook Login Cancelled", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(@NonNull FacebookException exception) {
                    Toast.makeText(LoginActivity.this, "Facebook Login Error", Toast.LENGTH_LONG).show();
                }
            });

            facebook_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
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
        }
        else
        {
            // User have signed in successfully
            this.navigateToLandingActivity(login_status);
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

    private void findViews()
    {
        this.username_login_input = findViewById(R.id.username_login);
        this.password_login_input = findViewById(R.id.password_login);
        this.forgotten_password_tv = findViewById(R.id.forgotten_password_tv);
        this.facebook_login_btn = findViewById(R.id.fb_login_button);
        this.google_login_btn = findViewById(R.id.google_login_button);
        this.login_btn = findViewById(R.id.login_btn);
        this.sign_up_btn = findViewById(R.id.sign_up_btn);
    }

    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                loadGoogleUserProfile(account);
                navigateToLandingActivity(GOOGLE_LOGIN);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(R.string.LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

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
            // String user_icon_url = account.getPhotoUrl().toString();
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

    private int checkLoginStatus()
    {
        if (checkFBLoginStatus())
        {
            return FACEBOOK_LOGIN;
        }
        else if(checkGoogleLoginStatus())
        {
            return GOOGLE_LOGIN;
        }
        else
        {
            return NOT_LOGGED_IN;
        }
    }

    private void navigateToLandingActivity(int login_option)
    {
        finish(); // Avoid the users being able to navigate back to this login activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(LOGIN_OPTION_KEY, login_option);
        startActivity(intent);
    }

    private void setAnimationsOnStart()
    {
        // Basically the idea is set the views to positions
        // that is away from the positions they are meant to be.
        // Then transit them back using animate() with the
        // associated duration, etc...

        float v = 0;

        username_login_input.setTranslationX(Utils.login_view_animation_translation);
        password_login_input.setTranslationX(Utils.login_view_animation_translation);
        login_btn.setTranslationX(Utils.login_view_animation_translation);
        forgotten_password_tv.setTranslationX(-Utils.login_view_animation_translation);
        facebook_login_btn.setTranslationY(Utils.login_view_animation_translation);
        google_login_btn.setTranslationY(Utils.login_view_animation_translation);
        sign_up_btn.setTranslationY(Utils.login_view_animation_translation);

        username_login_input.setAlpha(v);
        password_login_input.setAlpha(v);
        login_btn.setAlpha(v);
        forgotten_password_tv.setAlpha(v);
        facebook_login_btn.setAlpha(v);
        google_login_btn.setAlpha(v);
        sign_up_btn.setAlpha(v);

        username_login_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        password_login_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(600).start();
        forgotten_password_tv.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        facebook_login_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        google_login_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        sign_up_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
    }
}