package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.LoginFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private TextView forgotten_password_tv;
    private CallbackManager callbackManager;
    private LoginButton fbloginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_fragment, new LoginFragment()).addToBackStack("login_frag").commit();

        this.findInstantiatedViews();
        this.setupDefaultListeners();

        fbloginBtn.setPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        fbloginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        checkLoginStatus();
    }

    private void findInstantiatedViews()
    {
        this.forgotten_password_tv = findViewById(R.id.forgotten_password_tv);
        this.fbloginBtn = findViewById(R.id.login_button);
    }

    private void setupDefaultListeners()
    {
        this.forgotten_password_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "forgotten password textview clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
            if (accessToken1 == null)
            {
                /*Current access token is null, user logged out...*/

            }
            else
            {
                loadUserProfile(accessToken1);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken) {
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

    private void checkLoginStatus()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn)
            loadUserProfile(accessToken);


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


    }
}