package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Gson gson;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private TextInputLayout login_acc_input_layout;
    private TextInputLayout password_login_input_layout;
    private TextInputEditText login_acc_input;
    private TextInputEditText password_login_input;
    private TextView forgotten_password_tv;
    private ImageView facebook_login_btn;
    private ImageView google_login_btn;
    private MaterialButton login_btn;
    private Button sign_up_btn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setAnimationsOnStart();

        this.gson = new Gson();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
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

        if (login_status == Utils.NOT_LOGGED_IN)
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
                    Intent intent = new Intent(LoginActivity.this, ForgottenPasswordActivity.class);
                    startActivity(intent);
                }
            });

            // !<Facebook>! Callback registration
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // Successful Facebook login
                    user = new User(Utils.FACEBOOK_LOGIN, AccessToken.getCurrentAccessToken());
                    navigateToLandingActivity();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(@NonNull FacebookException exception) {
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
                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

                    if(status == ConnectionResult.SUCCESS)
                        googleSignIn();
                    else
                        Toast.makeText(LoginActivity.this, getString(R.string.google_ps_missing_msg), Toast.LENGTH_LONG).show();
                }
            });

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_acc_input_layout.setError(null);
                    password_login_input_layout.setError(null);

                    checkLocalLoginInput();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Utils.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void findViews()
    {
        this.login_acc_input_layout = findViewById(R.id.login_acc_input_layout);
        this.password_login_input_layout = findViewById(R.id.password_login);
        this.login_acc_input = findViewById(R.id.login_acc_input_et);
        this.password_login_input = findViewById(R.id.password_login_et);
        this.forgotten_password_tv = findViewById(R.id.forgotten_password_tv);
        this.facebook_login_btn = findViewById(R.id.fb_login_button);
        this.google_login_btn = findViewById(R.id.google_login_button);
        this.login_btn = findViewById(R.id.login_btn);
        this.sign_up_btn = findViewById(R.id.sign_up_btn);
    }

    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Utils.RC_SIGN_IN);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                loadGoogleUserInfo(account);
                navigateToLandingActivity();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
             Log.w("DEBUG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void loadGoogleUserInfo(GoogleSignInAccount account)
    {
        if (account != null)
        {
            String first_name = account.getGivenName();
            String last_name = account.getFamilyName();
            String email = account.getEmail();
            String img_url = null;

            if (account.getPhotoUrl() != null)
                img_url = account.getPhotoUrl().toString();

            this.createNewUserFromThirdParty(first_name, first_name, last_name, email, img_url);
        }
    }

    private void loadFacebookUserInfo(AccessToken accessToken)
    {
        /*Instantiate a request*/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                try {
                    assert jsonObject != null;
                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    String email = jsonObject.getString("email");
                    String img_url = "https://graph.facebook.com/"+jsonObject.getString("id")+"/picture?type=normal";

                    createNewUserFromThirdParty(first_name, first_name, last_name, email, img_url);

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

    private boolean checkFBLoginStatus()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn)
            user = new User(Utils.FACEBOOK_LOGIN, accessToken);

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
        loadGoogleUserInfo(account);

        return account != null;
    }

    private boolean checkLocalLoginStatus()
    {
        SharedPreferences app_sp = getSharedPreferences(Utils.APP_LOCAL_SP, 0);

        String logged_user = app_sp.getString(Utils.LOCAL_LOGGED_USER, null);

        if (logged_user == null)
        {
            return false;
        }
        else {
            Type type = new TypeToken<User>() {}.getType();
            user = gson.fromJson(logged_user, type);

            return true;
        }
    }

    private int checkLoginStatus()
    {
        if(checkLocalLoginStatus())
        {
            return Utils.LOCAL_LOGIN;
        }
        else if (checkFBLoginStatus())
        {
            return Utils.FACEBOOK_LOGIN;
        }
        else if(checkGoogleLoginStatus())
        {
            return Utils.GOOGLE_LOGIN;
        }
        else
        {
            return Utils.NOT_LOGGED_IN;
        }
    }

    private void navigateToLandingActivity()
    {
        finish(); // Avoid the users being able to navigate back to this login activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Utils.USER_INFO_KEY, gson.toJson(user));
        startActivity(intent);
    }

    private void navigateToLandingActivity(User user)
    {
        finish(); // Avoid the users being able to navigate back to this login activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Utils.USER_INFO_KEY, gson.toJson(user));
        startActivity(intent);
    }

    private void setAnimationsOnStart()
    {
        // Basically the idea is set the views to positions
        // that is away from the positions they are meant to be.
        // Then transit them back using animate() with the
        // associated duration, etc...

        float v = 0;

        login_acc_input_layout.setTranslationX(Utils.login_view_animation_translation);
        password_login_input_layout.setTranslationX(Utils.login_view_animation_translation);
        login_btn.setTranslationX(Utils.login_view_animation_translation);
        forgotten_password_tv.setTranslationX(-Utils.login_view_animation_translation);
        facebook_login_btn.setTranslationY(Utils.login_view_animation_translation);
        google_login_btn.setTranslationY(Utils.login_view_animation_translation);
        sign_up_btn.setTranslationY(Utils.login_view_animation_translation);

        login_acc_input_layout.setAlpha(v);
        password_login_input_layout.setAlpha(v);
        login_btn.setAlpha(v);
        forgotten_password_tv.setAlpha(v);
        facebook_login_btn.setAlpha(v);
        google_login_btn.setAlpha(v);
        sign_up_btn.setAlpha(v);

        login_acc_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        password_login_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(600).start();
        forgotten_password_tv.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        facebook_login_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        google_login_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        sign_up_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        login_acc_input_layout.setError(null);
        password_login_input_layout.setError(null);
    }

    private boolean checkLocalLoginInput()
    {
        String login_acc = Objects.requireNonNull(this.login_acc_input.getText()).toString();
        String password = Objects.requireNonNull(this.password_login_input.getText()).toString();
        boolean isEmail;

        if(login_acc.isEmpty())
        {
            this.login_acc_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
            return false;
        }
        else if(password.isEmpty())
        {
            this.password_login_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
            return false;
        }

        if(Utils.validateUserInput(this, login_acc, Utils.EMAIL_INPUT).equals(getString(R.string.valid_user_input)))
            isEmail = true;
        else if(Utils.validateUserInput(this, login_acc, Utils.USERNAME_INPUT).equals(getString(R.string.valid_user_input)))
            isEmail = false;
        else
        {
            this.login_acc_input_layout.setError(getString(R.string.invalid_username_or_email));
            return false; // Not valid username or email input
        }

        // Send API request to the server here for username/email and password match
        checkUserByEmailViaAPIRequest(login_acc, password);

        return true;
    }

    private void checkUserByEmailViaAPIRequest(String email, String password)
    {
        Call<User> call = Utils.getServerAPI(this).getUserByEmail(email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful())
                {
                    User userResponse = response.body();

                    if(userResponse == null)
                    {
                        login_acc_input_layout.setError(getString(R.string.email_user_not_registered_error));
                    }
                    else
                    {
                        if(password.equals(userResponse.getPassword()))
                        {
                            user = userResponse;
                            navigateToLandingActivity();
                        }
                        else
                        {
                            password_login_input_layout.setError(getString(R.string.user_acc_password_not_match_error));
                        }
                    }
                }
                else
                {
                    checkUserByEmailViaAPIRequest(email, password);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(() -> {
                    Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void createNewUserFromThirdParty(String username, String firstname, String lastname, String email, String profile_img_url)
    {
        //Send a POST request to the server to create the user instance
        Call<Void> call = Utils.getServerAPI(this).createUser(username, firstname, lastname, email, null, profile_img_url);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                {
                    user = new User(Utils.LOCAL_LOGIN, username, firstname, lastname, email, null, profile_img_url);
                    Utils.displayWelcomeToast(LoginActivity.this, firstname, lastname);
                }
                else
                {
                    createNewUserFromThirdParty(username, firstname, lastname, email, profile_img_url);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(() -> {
                    Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}