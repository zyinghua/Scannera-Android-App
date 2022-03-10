package com.example.food_product_comparison_android_app.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.HomeListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.LoginActivity;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.User;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private int login_option;
    private User user;
    private TextView welcome_username_tv;
    private CircularImageView home_user_img;
    private RecyclerView homeRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeListRecyclerViewAdapter homeListRecyclerViewAdapter;

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

        this.homeRecyclerView = view.findViewById(R.id.home_recyclerView);

        this.layoutManager = new LinearLayoutManager(getContext());
        this.homeRecyclerView.setLayoutManager(this.layoutManager);

        List<String> products = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            products.add(i + "");
        }

        this.homeListRecyclerViewAdapter = new HomeListRecyclerViewAdapter(products);
        this.homeRecyclerView.setAdapter(homeListRecyclerViewAdapter);

        this.findViews(view);
        this.loadUserProfile();

        return view;
    }

    private void findViews(View view)
    {
        this.welcome_username_tv = view.findViewById(R.id.welcome_username_tv);
        this.home_user_img = view.findViewById(R.id.home_user_img);
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

                        Picasso.get().load(img_url).into(home_user_img);
                        welcome_username_tv.setText(getString(R.string.home_greeting) + " " + user.getUsername());

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
                Picasso.get().load(user.getProfile_img_url()).into(home_user_img);

            welcome_username_tv.setText(getString(R.string.home_greeting)+ " " + user.getUsername());
        }
    }
}