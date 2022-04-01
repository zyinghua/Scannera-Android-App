package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgottenPasswordActivity extends AppCompatActivity {
    public static final String RESET_EMAIl_ADDRESS_KEY = "RESET_EMAIl_ADDRESS";
    private TextView activity_title;
    private LottieAnimationView forgot_password_animation;
    private MaterialButton send_email_btn;
    private TextView instruction;
    private TextInputLayout email_address_input_layout;
    private TextInputEditText email_address_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        this.setUpToolbar();
        this.findViews();
        this.setDefaultListeners();
        this.setAnimationsOnStart();
    }

    private void findViews()
    {
        this.forgot_password_animation = findViewById(R.id.forgot_password_animation);
        this.send_email_btn = findViewById(R.id.send_email_btn);
        this.activity_title = findViewById(R.id.activity_title);
        this.instruction = findViewById(R.id.forgotten_password_instruction);
        this.email_address_input_layout = findViewById(R.id.email_address_input_layout);
        this.email_address_input = findViewById(R.id.email_address_input_et);
    }

    private void setDefaultListeners()
    {
        this.send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_address_input_layout.setError(null);

                String email_address = Objects.requireNonNull(email_address_input.getText()).toString();
                String email_address_validation_result = Utils.validateUserInput(ForgottenPasswordActivity.this, email_address, Utils.EMAIL_INPUT);
                if(email_address_validation_result.equals(getString(R.string.valid_user_input)))
                {
                    // Send request to the server here to check the existence of the email and send if exists
                    Utils.sendPasswordResetEmailToTargetAddress(ForgottenPasswordActivity.this, email_address);

                    finish();
                    Intent intent = new Intent(ForgottenPasswordActivity.this, PasswordEmailSentActivity.class);
                    intent.putExtra(RESET_EMAIl_ADDRESS_KEY, email_address);
                    startActivity(intent);
                }
                else
                    email_address_input_layout.setError(email_address_validation_result);

            }
        });
    }

    private void setAnimationsOnStart()
    {
        float v = 0;

        activity_title.setTranslationY(-Utils.login_view_animation_translation);
        forgot_password_animation.setTranslationY(-Utils.login_view_animation_translation);
        instruction.setTranslationX(Utils.login_view_animation_translation);
        email_address_input_layout.setTranslationX(Utils.login_view_animation_translation);
        send_email_btn.setTranslationX(Utils.login_view_animation_translation);

        activity_title.setAlpha(v);
        forgot_password_animation.setAlpha(v);
        instruction.setAlpha(v);
        email_address_input_layout.setAlpha(v);
        send_email_btn.setAlpha(v);

        activity_title.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        forgot_password_animation.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        instruction.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        email_address_input_layout.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        send_email_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}