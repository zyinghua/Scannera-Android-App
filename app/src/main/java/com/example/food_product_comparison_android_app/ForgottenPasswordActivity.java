package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_product_comparison_android_app.Fragments.ForgottenPasswordFragment;
import com.example.food_product_comparison_android_app.Fragments.LoginIconFragment;
import com.example.food_product_comparison_android_app.Fragments.PasswordResetEmailSentFragment;

public class ForgottenPasswordActivity extends AppCompatActivity {
    private ForgottenPasswordFragment forgotten_password_fragment;
    private PasswordResetEmailSentFragment email_sent_fragment;
    private static final int SHOWING_INSTRUCTION_FRAG = 1;
    private static final int SHOWING_RESET_FRAG = 2;
    private int current_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        this.forgotten_password_fragment = new ForgottenPasswordFragment();
        this.email_sent_fragment = new PasswordResetEmailSentFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.forgotten_password_fragment).addToBackStack("forgotten_password_frag").commit();
        this.current_frag = SHOWING_INSTRUCTION_FRAG;
    }

    public void changeToEmailSentFrag()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.email_sent_fragment).addToBackStack("email_sent_frag").commit();
        this.current_frag = SHOWING_RESET_FRAG;
    }

    public void resend()
    {
        getSupportFragmentManager().beginTransaction().remove(this.email_sent_fragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.email_sent_fragment).addToBackStack("email_sent_frag").commit();
    }

    @Override
    public void onBackPressed() {
        if (current_frag == SHOWING_INSTRUCTION_FRAG)
        {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if (current_frag == SHOWING_RESET_FRAG)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.forgotten_password_fragment).addToBackStack("forgotten_password_frag").commit();
            this.current_frag = SHOWING_INSTRUCTION_FRAG;
        }
    }
}