package com.fit3162.scannera_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fit3162.scannera_app.Dialogs.LoadingDialog;
import com.fit3162.scannera_app.GeneralJavaClasses.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setAnimationsOnStart();

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

        if (!this.checkLoginStatus())
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
                    // User user = new User(Utils.FACEBOOK_LOGIN, AccessToken.getCurrentAccessToken());
                    // navigateToLandingActivity(user);
                    loginWithFacebookUserInfo(AccessToken.getCurrentAccessToken());
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
                loginWithGoogleUserInfo(account);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
             Log.w("DEBUG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void loginWithGoogleUserInfo(GoogleSignInAccount account)
    {
        if (account != null)
        {
            String first_name = account.getGivenName();
            String last_name = account.getFamilyName();
            String email = account.getEmail();
            String img_url = null;

            if (account.getPhotoUrl() != null)
                img_url = account.getPhotoUrl().toString();

            handleThirdPartyUser(System.currentTimeMillis(), Utils.GOOGLE_LOGIN, email, first_name, last_name, email, img_url);
        }
    }

    private void loginWithFacebookUserInfo(AccessToken accessToken)
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

                    handleThirdPartyUser(System.currentTimeMillis(), Utils.FACEBOOK_LOGIN, email, first_name, last_name, email, img_url);

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

    private boolean checkLoginStatus()
    {
        SharedPreferences app_sp = getSharedPreferences(Utils.APP_LOCAL_SP, 0);

        String logged_user = app_sp.getString(Utils.LOGGED_USER, null);

        if (logged_user == null)
        {
            // The third party records should sync with the shared preferences, if sp is modified manually in device.
            if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired())
            {
                LoginManager.getInstance().logOut();
            }
            else if(GoogleSignIn.getLastSignedInAccount(this) != null)
            {
                mGoogleSignInClient.signOut();
            }

            return false;
        }
        else {
            navigateToLandingActivity();
            return true;
        }
    }

    private void navigateToLandingActivity()
    {
        finish(); // Avoid the users being able to navigate back to this login activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        login_acc_input_layout.setError(null);
        password_login_input_layout.setError(null);
    }

    private void checkLocalLoginInput()
    {
        String login_acc = Objects.requireNonNull(this.login_acc_input.getText()).toString();
        String password = Objects.requireNonNull(this.password_login_input.getText()).toString();

        if(login_acc.isEmpty())
        {
            this.login_acc_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
        }
        else if(password.isEmpty())
        {
            this.password_login_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
        }
        else if (!Utils.validateUserInfoInput(this, login_acc, Utils.EMAIL_INPUT).equals(getString(R.string.valid_user_input))
        && !Utils.validateUserInfoInput(this, login_acc, Utils.USERNAME_INPUT).equals(getString(R.string.valid_user_input)))
        {
            this.login_acc_input_layout.setError(getString(R.string.invalid_username_or_email));
        }
        else {
            // Send API request to the server here for username/email and password match
            checkLocalUserInput(System.currentTimeMillis(), login_acc, password);
        }
    }

    private void checkLocalUserInput(Long init_time, String login_acc_title, String password)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<User> call = Utils.getServerAPI(this).getUserByEmailOrUsername(login_acc_title);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                loading_dialog.dismiss();

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
                            // Log the user in
                            userResponse.setLoginFlag(Utils.LOCAL_LOGIN);
                            Utils.updateUserLoginStatus(LoginActivity.this, userResponse);
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
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        checkLocalUserInput(init_time, login_acc_title, password);
                        Log.e("DEBUG", response.code() + "");
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleThirdPartyUser(Long init_time, int login_flag, String username, String firstname, String lastname, String email, String profile_img_url)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<User> call = Utils.getServerAPI(this).getUserByEmailOrUsername(email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    User userResponse = response.body();

                    if(userResponse == null)
                    {
                        // Create a new user
                        createNewUserFromThirdParty(System.currentTimeMillis(), login_flag, username, firstname, lastname, email, profile_img_url);
                    }
                    else
                    {
                        userResponse.setLoginFlag(login_flag);
                        Utils.updateUserLoginStatus(LoginActivity.this, userResponse);
                        navigateToLandingActivity();
                    }
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        handleThirdPartyUser(init_time, login_flag, username, firstname, lastname, email, profile_img_url);
                        Log.e("DEBUG", response.code() + "");
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNewUserFromThirdParty(Long init_time, int login_flag, String username, String firstname, String lastname, String email, String profile_img_url)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        //Send a POST request to the server to create the user instance
        Call<User> call = Utils.getServerAPI(this).postUser(username, firstname, lastname, email, null, profile_img_url);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    User user = response.body();
                    user.setLoginFlag(login_flag);
                    Utils.updateUserLoginStatus(LoginActivity.this, user);
                    navigateToLandingActivity();

                    Utils.displayWelcomeToast(LoginActivity.this, firstname, lastname);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        createNewUserFromThirdParty(init_time, login_flag, username, firstname, lastname, email, profile_img_url);
                        Log.e("DEBUG", response.code() + "");
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
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
}