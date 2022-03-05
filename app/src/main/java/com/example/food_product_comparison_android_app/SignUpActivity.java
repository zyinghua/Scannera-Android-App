package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private ImageButton top_back_button;
    private TextInputLayout username_input;
    private TextInputLayout first_name_input;
    private TextInputLayout last_name_input;
    private TextInputLayout email_input;
    private TextInputLayout phone_input;
    private TextInputLayout password_input;
    private TextInputLayout confirm_password_input;
    private MaterialButton sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();
        setAnimationsOnStart();

        this.top_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void findViews()
    {
        this.top_back_button = findViewById(R.id.top_back_btn);
        this.username_input = findViewById(R.id.username_sign_up);
        this.first_name_input = findViewById(R.id.first_name_sign_up);
        this.last_name_input = findViewById(R.id.last_name_sign_up);
        this.email_input = findViewById(R.id.email_sign_up);
        this.phone_input = findViewById(R.id.phone_sign_up);
        this.password_input = findViewById(R.id.password_sign_up);
        this.confirm_password_input = findViewById(R.id.confirm_password_sign_up);
        this.sign_up_btn = findViewById(R.id.sign_up_btn);
    }

    private void setAnimationsOnStart()
    {
        float v = 0;

        top_back_button.setTranslationX(Utils.login_view_animation_translation);
        username_input.setTranslationX(Utils.login_view_animation_translation);
        first_name_input.setTranslationX(Utils.login_view_animation_translation);
        last_name_input.setTranslationX(Utils.login_view_animation_translation);
        email_input.setTranslationX(Utils.login_view_animation_translation);
        phone_input.setTranslationX(Utils.login_view_animation_translation);
        password_input.setTranslationX(Utils.login_view_animation_translation);
        confirm_password_input.setTranslationX(Utils.login_view_animation_translation);
        sign_up_btn.setTranslationY(Utils.login_view_animation_translation);

        top_back_button.setAlpha(v);
        username_input.setAlpha(v);
        first_name_input.setAlpha(v);
        last_name_input.setAlpha(v);
        email_input.setAlpha(v);
        phone_input.setAlpha(v);
        password_input.setAlpha(v);
        confirm_password_input.setAlpha(v);
        sign_up_btn.setAlpha(v);

        top_back_button.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        username_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(200).start();
        first_name_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(300).start();
        last_name_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        email_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        phone_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(600).start();
        password_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(700).start();
        confirm_password_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(800).start();
        sign_up_btn.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
    }
}