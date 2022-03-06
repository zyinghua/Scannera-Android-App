package com.example.food_product_comparison_android_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.ForgottenPasswordActivity;
import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.Utils;
import com.google.android.material.button.MaterialButton;

public class PasswordResetEmailSentFragment extends Fragment {
    private TextView email_sent_tv;
    private ImageButton top_close_btn;
    private MaterialButton login_btn;
    private TextView resend_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_reset_email_sent, container, false);

        this.findViews(view);
        this.setDefaultListeners();
        this.setAnimationsOnStart();

        return view;
    }

    private void findViews(View view)
    {
        this.top_close_btn = view.findViewById(R.id.top_close_btn);
        this.email_sent_tv = view.findViewById(R.id.email_sent_tv);
        this.login_btn = view.findViewById(R.id.login_btn);
        this.resend_btn = view.findViewById(R.id.resend_tv);
    }

    private void setDefaultListeners()
    {
        this.top_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgottenPasswordActivity) getActivity()).finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        this.login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgottenPasswordActivity) getActivity()).finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        this.resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgottenPasswordActivity) getActivity()).resend();
            }
        });
    }

    private void setAnimationsOnStart()
    {
        float v = 0;

        top_close_btn.setTranslationX(Utils.login_view_animation_translation);
        email_sent_tv.setTranslationY(-Utils.login_view_animation_translation);
        login_btn.setTranslationX(Utils.login_view_animation_translation);
        resend_btn.setTranslationX(Utils.login_view_animation_translation);

        top_close_btn.setAlpha(v);
        email_sent_tv.setAlpha(v);
        login_btn.setAlpha(v);
        resend_btn.setAlpha(v);

        top_close_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        email_sent_tv.animate().translationY(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
        resend_btn.animate().translationX(0).alpha(1).setDuration(Utils.login_view_animation_duration).setStartDelay(400).start();
    }
}