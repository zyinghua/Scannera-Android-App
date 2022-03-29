package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Window;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout username_input;
    private TextInputLayout first_name_input;
    private TextInputLayout last_name_input;
    private TextInputLayout email_input;
    private TextInputLayout password_input;
    private TextInputLayout confirm_password_input;
    private MaterialButton sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setUpToolbar();
        this.findViews();
        this.setAnimationsOnStart();
    }

    private void findViews()
    {
        this.username_input = findViewById(R.id.username_sign_up);
        this.first_name_input = findViewById(R.id.firstname_sign_up);
        this.last_name_input = findViewById(R.id.lastname_sign_up);
        this.email_input = findViewById(R.id.email_sign_up);
        this.password_input = findViewById(R.id.password_sign_up);
        this.confirm_password_input = findViewById(R.id.confirm_password_sign_up);
        this.sign_up_btn = findViewById(R.id.sign_up_btn);
    }

    private void setAnimationsOnStart()
    {
        // Basically the idea is set the views to positions
        // that is away from the positions they are meant to be.
        // Then transit them back using animate() with the
        // associated duration, etc...

        float v = 0;

        username_input.setTranslationX(Utils.login_view_animation_translation);
        first_name_input.setTranslationX(Utils.login_view_animation_translation);
        last_name_input.setTranslationX(Utils.login_view_animation_translation);
        email_input.setTranslationX(Utils.login_view_animation_translation);
        password_input.setTranslationX(Utils.login_view_animation_translation);
        confirm_password_input.setTranslationX(Utils.login_view_animation_translation);
        sign_up_btn.setTranslationY(Utils.login_view_animation_translation);

        username_input.setAlpha(v);
        first_name_input.setAlpha(v);
        last_name_input.setAlpha(v);
        email_input.setAlpha(v);
        password_input.setAlpha(v);
        confirm_password_input.setAlpha(v);
        sign_up_btn.setAlpha(v);

        username_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(200).start();
        first_name_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(300).start();
        last_name_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        email_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        password_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(600).start();
        confirm_password_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(700).start();
        sign_up_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}