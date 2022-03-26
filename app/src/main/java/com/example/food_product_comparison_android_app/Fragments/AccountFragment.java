package com.example.food_product_comparison_android_app.Fragments;

import android.content.Intent;
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
    private TextView contribution_score;
    private MaterialButton account_info_btn;
    private MaterialButton starred_products_btn;
    private MaterialButton scan_history_btn;
    private MaterialButton about_us_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<User>() {}.getType();
            user = gson.fromJson(getArguments().getString(Utils.USER_INFO_KEY), type);
        }
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
        this.contribution_score = view.findViewById(R.id.contribution_score);
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
                switch(user.getLogin_option())
                {
                    case Utils.FACEBOOK_LOGIN:
                        ((MainActivity) requireActivity()).facebookLogOut();
                        break;

                    case Utils.GOOGLE_LOGIN:
                        ((MainActivity) requireActivity()).googleLogOut();
                        break;

                    default:
                        ((MainActivity) requireActivity()).finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                }
            }
        });

        this.account_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
                intent.putExtra(Utils.USER_INFO_KEY, new Gson().toJson(user));
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
        if (user.getLogin_option() == Utils.FACEBOOK_LOGIN)
        {
            /*Instantiate a request*/
            GraphRequest request = GraphRequest.newMeRequest(user.getFb_access_token(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                    try {
                        assert jsonObject != null;
                        String first_name = jsonObject.getString("first_name");
                        String last_name = jsonObject.getString("last_name");
                        String email = jsonObject.getString("email");
                        String id = jsonObject.getString("id");
                        String img_url = "https://graph.facebook.com/"+jsonObject.getString("id")+"/picture?type=normal";

                        Picasso.get().load(img_url).into(user_profile_img);
                        username_tv.setText(first_name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "first_name, last_name, email, id");
            request.setParameters(parameters);
            request.executeAsync(); // Now execute the request with the parameters
        }
        else
        {
            if (user.getProfile_img_url() != null)
                Picasso.get().load(user.getProfile_img_url()).into(user_profile_img);
            username_tv.setText(user.getUsername());
        }
    }
}