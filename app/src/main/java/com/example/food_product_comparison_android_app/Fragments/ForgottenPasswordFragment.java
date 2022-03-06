package com.example.food_product_comparison_android_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_product_comparison_android_app.ForgottenPasswordActivity;
import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ForgottenPasswordFragment extends Fragment {
    private ImageButton top_back_btn;
    private TextView activity_title;
    private LottieAnimationView forgot_password_animation;
    private MaterialButton send_email_btn;
    private TextView instruction;
    private TextInputLayout email_input;

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
        this.activity_title = view.findViewById(R.id.activity_title);
        this.instruction = view.findViewById(R.id.forgotten_password_instruction);
        this.email_input = view.findViewById(R.id.email_input);
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