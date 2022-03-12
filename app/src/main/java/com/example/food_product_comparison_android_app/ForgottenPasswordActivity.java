package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgottenPasswordActivity extends AppCompatActivity {
    public static final String RESET_EMAIl_ADDRESS_KEY = "RESET_EMAIl_ADDRESS";
    private ImageButton top_back_btn;
    private TextView activity_title;
    private LottieAnimationView forgot_password_animation;
    private MaterialButton send_email_btn;
    private TextView instruction;
    private TextInputLayout email_input;
    private TextInputEditText email_input_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        this.findViews();
        this.setDefaultListeners();
        this.setAnimationsOnStart();
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
        this.forgot_password_animation = findViewById(R.id.forgot_password_animation);
        this.send_email_btn = findViewById(R.id.send_email_btn);
        this.activity_title = findViewById(R.id.activity_title);
        this.instruction = findViewById(R.id.forgotten_password_instruction);
        this.email_input = findViewById(R.id.email_input);
        this.email_input_et = findViewById(R.id.email_input_et);
    }

    private void setDefaultListeners()
    {
        this.top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ForgottenPasswordActivity.this, PasswordEmailSentActivity.class);
                intent.putExtra(RESET_EMAIl_ADDRESS_KEY, email_input_et.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void setAnimationsOnStart()
    {
        float v = 0;

        top_back_btn.setTranslationX(Utils.login_view_animation_translation);
        activity_title.setTranslationY(-Utils.login_view_animation_translation);
        forgot_password_animation.setTranslationY(-Utils.login_view_animation_translation);
        instruction.setTranslationX(Utils.login_view_animation_translation);
        email_input.setTranslationX(Utils.login_view_animation_translation);
        send_email_btn.setTranslationX(Utils.login_view_animation_translation);

        top_back_btn.setAlpha(v);
        activity_title.setAlpha(v);
        forgot_password_animation.setAlpha(v);
        instruction.setAlpha(v);
        email_input.setAlpha(v);
        send_email_btn.setAlpha(v);

        top_back_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        activity_title.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        forgot_password_animation.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        instruction.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        email_input.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
        send_email_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(500).start();
    }

}