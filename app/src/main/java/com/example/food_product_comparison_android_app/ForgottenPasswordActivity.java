package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        this.forgotten_password_fragment = new ForgottenPasswordFragment();
        this.email_sent_fragment = new PasswordResetEmailSentFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.forgotten_password_activity_container, this.forgotten_password_fragment).addToBackStack("forgotten_password_frag").commit();
    }

    public void changeToEmailSentFrag()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.forgotten_password_activity_container, this.email_sent_fragment).addToBackStack("email_sent_frag").commit();
    }
}