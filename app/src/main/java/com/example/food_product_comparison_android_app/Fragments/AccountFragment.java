package com.example.food_product_comparison_android_app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.AboutUsActivity;
import com.example.food_product_comparison_android_app.AccountInfoActivity;
import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.MainActivity;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.ScanHistoryActivity;
import com.example.food_product_comparison_android_app.StarredProductActivity;
import com.example.food_product_comparison_android_app.User;
import com.example.food_product_comparison_android_app.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;

public class AccountFragment extends Fragment {
    private User user;
    private MaterialButton log_out_btn;
    private CircularImageView user_profile_img;
    private TextView username_tv;
    private TextView contribution_score_tv;
    private MaterialButton account_info_btn;
    private MaterialButton starred_products_btn;
    private MaterialButton scan_history_btn;
    private MaterialButton about_us_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = Utils.getLoggedUser(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        this.findViews(view);
        this.loadUserProfile();
        this.setUpListeners();

        return view;
    }

    private void findViews(View view)
    {
        this.log_out_btn = view.findViewById(R.id.log_out_btn);
        this.user_profile_img = view.findViewById(R.id.user_profile_img);
        this.username_tv = view.findViewById(R.id.username_display);
        this.contribution_score_tv = view.findViewById(R.id.contribution_score);
        this.account_info_btn = view.findViewById(R.id.account_info_btn);
        this.starred_products_btn = view.findViewById(R.id.starred_products_btn);
        this.scan_history_btn = view.findViewById(R.id.scan_history_btn);
        this.about_us_btn = view.findViewById(R.id.about_us_btn);
    }

    private void setUpListeners()
    {
        log_out_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(user.getLoginFlag())
                {
                    case Utils.LOCAL_LOGIN:
                        Utils.removeUserLoginStatus(requireActivity());
                        requireActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;

                    case Utils.FACEBOOK_LOGIN:
                        ((MainActivity) requireActivity()).facebookLogOut();
                        break;

                    case Utils.GOOGLE_LOGIN:
                        ((MainActivity) requireActivity()).googleLogOut();
                        break;
                }
            }
        });

        this.account_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
                startActivity(intent);
            }
        });

        this.starred_products_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StarredProductActivity.class));
            }
        });

        this.scan_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanHistoryActivity.class);
                startActivity(intent);
            }
        });

        this.about_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });
    }

    private void loadUserProfile() {
        if (user.getProfile_img_url() != null)
            Picasso.get().load(user.getProfile_img_url()).into(user_profile_img);
        username_tv.setText(user.getUsername());
        contribution_score_tv.setText(String.format(getString(R.string.contribution_score), user.getContribution_score()));
    }
}