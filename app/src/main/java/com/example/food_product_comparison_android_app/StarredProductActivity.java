package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
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

public class StarredProductActivity extends AppCompatActivity {
    private static final String STARRED_PRODUCTS_END_POINT =
            ServerAPI.GET_STARRED_PRODUCTS_SERVER + "?user_id=";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_with_product_list);

        this.setUpToolbar();
        ((TextView) findViewById(R.id.activity_title)).setText(getString(R.string.starred_products));

        this.setUpContent();
    }

    private void setUpContent()
    {
        this.recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);

        //******************************************************
        this.handleOnGetStarredProducts(System.currentTimeMillis());
        //******************************************************
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            // This is to sync the toolbar up button with the back button
            onBackPressed();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    private void handleOnGetStarredProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor(); // get a thread to execute the request
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ArrayList<Object> starred_products;

            try {
                URL webServiceUrl = new URL(getString(R.string.server_base_url) + STARRED_PRODUCTS_END_POINT + Utils.getLoggedUser(this).getId());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) webServiceUrl.openConnection();

                if (httpsURLConnection.getResponseCode() >= 200 && httpsURLConnection.getResponseCode() < 300) // If successful
                {
                    loading_dialog.dismiss();

                    // -----------------------------------------------
                    // Parse Data
                    starred_products = Utils.parseProductsFromResponse(this, httpsURLConnection);
                    // -----------------------------------------------

                    uiHandler.post(() -> {
                        ProductListRecyclerViewAdapter spAdapter = new ProductListRecyclerViewAdapter(
                                getApplicationContext(), this, starred_products);
                        recyclerView.setAdapter(spAdapter);
                    });
                }
                else
                {
                    // response code = NOT Successful
                    loading_dialog.dismiss();

                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        uiHandler.post(() -> {
                            handleOnGetStarredProducts(init_time);
                            try {
                                Log.e("DEBUG", "Starred Products Response code: " + httpsURLConnection.getResponseCode());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else
                    {
                        uiHandler.post(() -> {
                            Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        });
                    }
                }

            } catch(Exception e) {
                loading_dialog.dismiss();
                uiHandler.post(() -> {
                    Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Server Related Exception Error: " + e);
                });
            }
        });
    }
}