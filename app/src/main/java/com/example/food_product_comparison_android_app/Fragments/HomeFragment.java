package com.example.food_product_comparison_android_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.LoadingDialog;
import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.User;
import com.example.food_product_comparison_android_app.Utils;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private User user;
    private TextView welcome_username_tv;
    private CircularImageView home_user_img;
    private RecyclerView homeRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        user = Utils.getLoggedUser(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.findViews(view);
        this.setUpToolbar(view);

        LoadingDialog loading_dialog = new LoadingDialog(requireActivity());
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        this.loadUserProfile();

        executor.execute(()->{
            this.setUpContent();
            uiHandler.post(loading_dialog::dismiss);
        });

        return view;
    }

    private void findViews(View view)
    {
        this.welcome_username_tv = view.findViewById(R.id.welcome_username_tv);
        this.home_user_img = view.findViewById(R.id.home_user_img);
        this.homeRecyclerView = view.findViewById(R.id.home_recyclerView);
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.home_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadUserProfile() {
        if (user.getProfile_img_url() != null && !user.getProfile_img_url().equals("null"))
            Picasso.get().load(user.getProfile_img_url()).into(home_user_img);

        welcome_username_tv.setText(getString(R.string.home_greeting)+ user.getUsername());
    }

    private void setUpContent()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.homeRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Object> products = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            products.add(new Product(i + "", "Martin & Pleasance", "Rest & Quiet Calm Pastilles", 7.99f, "Health Products", true));
        }

        ProductListRecyclerViewAdapter productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(requireActivity().getApplicationContext(),getActivity(), products);
        this.homeRecyclerView.setAdapter(productListRecyclerViewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}