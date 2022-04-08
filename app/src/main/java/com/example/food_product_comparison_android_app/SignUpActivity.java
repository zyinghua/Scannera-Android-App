package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout username_input_layout;
    private TextInputLayout firstname_input_layout;
    private TextInputLayout lastname_input_layout;
    private TextInputLayout email_input_layout;
    private TextInputLayout password_input_layout;
    private TextInputLayout confirm_password_input_layout;
    private TextInputEditText username_input;
    private TextInputEditText firstname_input;
    private TextInputEditText lastname_input;
    private TextInputEditText email_input;
    private TextInputEditText password_input;
    private TextInputEditText confirm_password_input;
    private MaterialButton sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setUpToolbar();
        this.findViews();
        this.setAnimationsOnStart();

        this.sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFieldErrors();

                checkUserInput();
            }
        });
    }

    private void findViews()
    {
        this.username_input_layout = findViewById(R.id.username_sign_up);
        this.firstname_input_layout = findViewById(R.id.firstname_sign_up);
        this.lastname_input_layout = findViewById(R.id.lastname_sign_up);
        this.email_input_layout = findViewById(R.id.email_sign_up);
        this.password_input_layout = findViewById(R.id.password_sign_up);
        this.confirm_password_input_layout = findViewById(R.id.confirm_password_sign_up);
        this.username_input = findViewById(R.id.username_sign_up_et);
        this.firstname_input = findViewById(R.id.firstname_sign_up_et);
        this.lastname_input = findViewById(R.id.lastname_sign_up_et);
        this.email_input = findViewById(R.id.email_sign_up_et);
        this.password_input = findViewById(R.id.password_sign_up_et);
        this.confirm_password_input = findViewById(R.id.confirm_password_sign_up_et);
        this.sign_up_btn = findViewById(R.id.sign_up_btn);
    }

    private void setAnimationsOnStart()
    {
        // Basically the idea is set the views to positions
        // that is away from the positions they are meant to be.
        // Then transit them back using animate() with the
        // associated duration, etc...

        float v = 0;

        username_input_layout.setTranslationX(Utils.login_view_animation_translation);
        firstname_input_layout.setTranslationX(Utils.login_view_animation_translation);
        lastname_input_layout.setTranslationX(Utils.login_view_animation_translation);
        email_input_layout.setTranslationX(Utils.login_view_animation_translation);
        password_input_layout.setTranslationX(Utils.login_view_animation_translation);
        confirm_password_input_layout.setTranslationX(Utils.login_view_animation_translation);
        sign_up_btn.setTranslationY(Utils.login_view_animation_translation);

        username_input_layout.setAlpha(v);
        firstname_input_layout.setAlpha(v);
        lastname_input_layout.setAlpha(v);
        email_input_layout.setAlpha(v);
        password_input_layout.setAlpha(v);
        confirm_password_input_layout.setAlpha(v);
        sign_up_btn.setAlpha(v);

        username_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(200).start();
        firstname_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(300).start();
        lastname_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        email_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        password_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(600).start();
        confirm_password_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(700).start();
        sign_up_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void clearFieldErrors()
    {
        this.username_input_layout.setError(null);
        this.firstname_input_layout.setError(null);
        this.lastname_input_layout.setError(null);
        this.email_input_layout.setError(null);
        this.password_input_layout.setError(null);
        this.confirm_password_input_layout.setError(null);
    }

    private void checkUserInput()
    {
        String username = Objects.requireNonNull(this.username_input.getText()).toString();
        String firstname = Objects.requireNonNull(this.firstname_input.getText()).toString();
        String lastname = Objects.requireNonNull(this.lastname_input.getText()).toString();
        String email = Objects.requireNonNull(this.email_input.getText()).toString();
        String password = Objects.requireNonNull(this.password_input.getText()).toString();

        String username_result = Utils.validateUserInput(this, username, Utils.USERNAME_INPUT);
        String firstname_result = Utils.validateUserInput(this, firstname, Utils.FLNAME_INPUT);
        String lastname_result = Utils.validateUserInput(this, lastname, Utils.FLNAME_INPUT);
        String email_result = Utils.validateUserInput(this, email, Utils.EMAIL_INPUT);
        String password_result = Utils.validateUserInput(this, password, Utils.PASSWORD_INPUT);
        String confirm_password = Objects.requireNonNull(this.confirm_password_input.getText()).toString();

        if (!username_result.equals(getString(R.string.valid_user_input)))
        {
            this.username_input_layout.setError(username_result);
        } else if (!firstname_result.equals(getString(R.string.valid_user_input))){
            this.firstname_input_layout.setError(firstname_result);
        } else if (!lastname_result.equals(getString(R.string.valid_user_input))) {
            this.lastname_input_layout.setError(lastname_result);
        } else if(!email_result.equals(getString(R.string.valid_user_input))) {
            this.email_input_layout.setError(email_result);
        } else if (!password_result.equals(getString(R.string.valid_user_input))) {
            this.password_input_layout.setError(password_result);
        } else if (!confirm_password.equals(password)) {
            this.confirm_password_input_layout.setError(getString(R.string.confirm_password_not_match_error));
        }
        else {
            attemptToCreateUser(System.currentTimeMillis(), username, firstname, lastname, email, password);
        }
    }

    private void attemptToCreateUser(Long init_time, String username, String firstname, String lastname, String email, String password)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        //Send a POST request to the server to create the user instance
        Call<User> call = Utils.getServerAPI(this).postUser(username, firstname, lastname, email, password, null);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    User userResponse = response.body();
                    userResponse.setLogin_flag(Utils.LOCAL_LOGIN);

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(response.code() == 405)
                {
                    // Username already exists
                    username_input_layout.setError(getString(R.string.username_taken_error));

                } else if (response.code() == 406)
                {
                    // Email already exists
                    email_input_layout.setError(getString(R.string.email_address_taken_error));

                } else {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        attemptToCreateUser(init_time, username, firstname, lastname, email, password);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(SignUpActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

}