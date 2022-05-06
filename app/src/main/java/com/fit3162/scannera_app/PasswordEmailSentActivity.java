package com.fit3162.scannera_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class PasswordEmailSentActivity extends AppCompatActivity {
    private TextView email_sent_tv;
    private MaterialButton login_btn;
    private TextView resend_btn;
    private String target_email;
    private String target_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_email_sent);

        this.setUpToolbar();
        this.findViews();
        this.setDefaultListeners();
        this.setAnimationsOnStart();

        this.target_email = getIntent().getStringExtra(ForgottenPasswordActivity.RESET_EMAIl_ADDRESS_KEY);
        this.target_user_id = getIntent().getStringExtra(ForgottenPasswordActivity.RESET_USER_ID);
    }

    private void findViews()
    {
        this.email_sent_tv = findViewById(R.id.email_sent_tv);
        this.login_btn = findViewById(R.id.login_btn);
        this.resend_btn = findViewById(R.id.resend_tv);
    }

    private void setDefaultListeners()
    {
        this.login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will lead to the original login activity we started with
            }
        });

        this.resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateUserPasswordAndActivity(System.currentTimeMillis(),PasswordEmailSentActivity.this, target_email, target_user_id);
            }
        });
    }

    private void setAnimationsOnStart()
    {
        float v = 0;

        email_sent_tv.setTranslationY(-Utils.login_view_animation_translation);
        login_btn.setTranslationX(Utils.login_view_animation_translation);
        resend_btn.setTranslationX(Utils.login_view_animation_translation);

        email_sent_tv.setAlpha(v);
        login_btn.setAlpha(v);
        resend_btn.setAlpha(v);

        email_sent_tv.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        resend_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}