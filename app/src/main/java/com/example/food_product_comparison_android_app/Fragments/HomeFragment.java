package com.example.food_product_comparison_android_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.LoadingDialog;
import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.ScanHistoryActivity;
import com.example.food_product_comparison_android_app.StarredProductActivity;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private User user;
    private TextView welcome_username_tv;
    private CircularImageView home_user_img;
    private List<Product> recommended_products;
    private LinearLayoutManager layoutManager;
    private RecyclerView homeRecyclerView;
    // private boolean isLoading;

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
        this.loadUserProfile();
        this.setUpContent();

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

        welcome_username_tv.setText(String.format(getString(R.string.home_greeting), user.getUsername()));
    }

    private void setUpContent()
    {
        // isLoading = false;
        layoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(layoutManager);

        handleOnGetRecommendedProducts(System.currentTimeMillis());

//        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if(!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == products.size() - 1)
//                {
//                    loadASetOfProducts();
//                    isLoading = true;
//                }
//            }
//        });
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

    private void handleOnGetRecommendedProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(requireActivity());
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor(); // get a thread to execute the request
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ArrayList<Object> recommended_products;

            try {
                URL webServiceUrl = new URL(getString(R.string.server_base_url) + "api/favourite/get?user_id="+user.getId());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) webServiceUrl.openConnection();

                if (httpsURLConnection.getResponseCode() >= 200 && httpsURLConnection.getResponseCode() < 300) // If successful
                {
                    loading_dialog.dismiss();

                    // -----------------------------------------------
                    // Parse Data
                    recommended_products = Utils.parseProductsFromResponse(getActivity(), httpsURLConnection);
                    // -----------------------------------------------

                    uiHandler.post(() -> {
                        ProductListRecyclerViewAdapter rpAdapter = new ProductListRecyclerViewAdapter(
                                requireActivity().getApplicationContext(), getActivity(), recommended_products);
                        homeRecyclerView.setAdapter(rpAdapter);
                    });
                }
                else
                {
                    // response code = NOT Successful
                    loading_dialog.dismiss();

                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        uiHandler.post(() -> {
                            handleOnGetRecommendedProducts(init_time);
                            try {
                                Log.e("DEBUG", "Home Recommended Products Response code: " + httpsURLConnection.getResponseCode());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else
                    {
                        uiHandler.post(() -> {
                            Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        });
                    }
                }

            } catch(Exception e) {
                loading_dialog.dismiss();
                uiHandler.post(() -> {
                    Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Server Related Exception Error: " + e);
                });
            }
        });
    }

//    private void loadASetOfProducts()
//    {
//        new Handler(Looper.getMainLooper()).post(() -> {
//            products.add(Utils.LOADING_BAR_TAG);
//            productListRecyclerViewAdapter.notifyItemInserted(products.size() - 1);
//        });
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            products.remove(products.size() - 1);
//            productListRecyclerViewAdapter.notifyItemRemoved(products.size() - 1);
//
//            for(int i = 0; i < 5; i++)
//            {
//                products.add(new Product("54", i + "", "Martin & Pleasance", "Rest & Quiet Calm Pastilles", 7.99f, "Health Products", true));
//                productListRecyclerViewAdapter.notifyItemInserted(products.size() - 1);
//            }
//
//            isLoading = false;
//        }, 1000);
//
//
//        //productListRecyclerViewAdapter.notifyDataSetChanged();
//    }
}