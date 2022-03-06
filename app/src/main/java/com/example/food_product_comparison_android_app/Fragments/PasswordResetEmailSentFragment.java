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
import com.google.android.material.button.MaterialButton;

public class PasswordResetEmailSentFragment extends Fragment {
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

    }
}