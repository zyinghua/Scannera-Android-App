package com.example.food_product_comparison_android_app.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.User;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class HomeFragment extends Fragment {
    private int login_option;
    private User user;
    private MaterialButton scan_btn;
    private TextView welcome_username_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            login_option = getArguments().getInt(LoginActivity.LOGIN_OPTION_KEY);

            Gson gson = new Gson();
            Type type = new TypeToken<User>() {}.getType();
            user = gson.fromJson(getArguments().getString(LoginActivity.USER_INFO_KEY), type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.findViews(view);
        this.loadUserProfile();

        return view;
    }

    private void findViews(View view)
    {
        this.scan_btn = view.findViewById(R.id.scan_btn);
        this.welcome_username_tv = view.findViewById(R.id.welcome_username_tv);
    }

    private void loadUserProfile() {
        if (login_option == LoginActivity.FACEBOOK_LOGIN)
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

                        welcome_username_tv.setText(welcome_username_tv.getText().toString() + user.getUsername() + "!");

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
            welcome_username_tv.setText(welcome_username_tv.getText().toString() + user.getUsername() + "!");
        }
    }
}