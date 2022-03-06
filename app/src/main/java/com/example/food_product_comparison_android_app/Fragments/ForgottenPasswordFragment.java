package com.example.food_product_comparison_android_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_product_comparison_android_app.ForgottenPasswordActivity;
import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.R;
import com.google.android.material.button.MaterialButton;

public class ForgottenPasswordFragment extends Fragment {
    private ImageButton top_back_btn;
    private LottieAnimationView forgot_password_animation;
    private MaterialButton send_email_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgotten_password, container, false);

        this.findViews(view);
        this.setDefaultListeners();
        this.setAnimationsOnStart();

        return view;
    }


    private void findViews(View view)
    {
        this.top_back_btn = view.findViewById(R.id.top_back_btn);
        this.forgot_password_animation = view.findViewById(R.id.forgot_password_animation);
        this.send_email_btn = view.findViewById(R.id.send_email_btn);
    }

    private void setDefaultListeners()
    {
        this.top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgottenPasswordActivity) getActivity()).finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        this.send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgottenPasswordActivity) getActivity()).changeToEmailSentFrag();
            }
        });
    }

    private void setAnimationsOnStart()
    {

    }
}