package com.example.food_product_comparison_android_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.food_product_comparison_android_app.ForgottenPasswordActivity;
import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.MainActivity;
import com.example.food_product_comparison_android_app.R;


public class ProfileFragment extends Fragment {
    private int login_option;
    private Button log_out_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            login_option = getArguments().getInt(LoginActivity.LOGIN_OPTION_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.findViews(view);

        log_out_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(login_option)
                {
                    case LoginActivity.FACEBOOK_LOGIN:
                        ((MainActivity) getActivity()).facebookLogOut();
                        break;

                    case LoginActivity.GOOGLE_LOGIN:
                        ((MainActivity) getActivity()).googleLogOut();
                        break;

                    default:
                        ((MainActivity) getActivity()).finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                }
            }
        });

        return view;
    }

    private void findViews(View view)
    {
        this.log_out_btn = view.findViewById(R.id.log_out_btn);
    }
}