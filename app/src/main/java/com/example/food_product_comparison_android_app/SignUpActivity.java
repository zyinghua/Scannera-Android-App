package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Objects;
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

                if (checkUserInput())
                {
                }
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

    private boolean checkUserInput()
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
            return false;
        } else if (!firstname_result.equals(getString(R.string.valid_user_input))){
            this.firstname_input_layout.setError(firstname_result);
            return false;
        } else if (!lastname_result.equals(getString(R.string.valid_user_input))) {
            this.lastname_input_layout.setError(lastname_result);
            return false;
        } else if(!email_result.equals(getString(R.string.valid_user_input))) {
            this.email_input_layout.setError(email_result);
            return false;
        } else if (!password_result.equals(getString(R.string.valid_user_input))) {
            this.password_input_layout.setError(password_result);
            return false;
        } else if (!confirm_password.equals(password)) {
            this.confirm_password_input_layout.setError(getString(R.string.confirm_password_not_match_error));
            return false;
        } else {
            createUserOnChecked(username, firstname, lastname, email, password);
            return true;
        }
    }

    private boolean checkIfUsernameExists()
    {
        return false;
    }

    private boolean checkIfEmailExists()
    {
        return false;
    }

    private void createUserOnChecked(String username, String firstname, String lastname, String email, String password)
    {
        //Send a POST request to the server to create the user instance
        Call<Void> call = Utils.getServerAPI(this).createUser(username, firstname, lastname, email, password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                {
                    User user = new User(Utils.LOCAL_LOGIN, username, firstname, lastname, email, password);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(Utils.USER_INFO_KEY, new Gson().toJson(user));
                    startActivity(intent);
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }
}