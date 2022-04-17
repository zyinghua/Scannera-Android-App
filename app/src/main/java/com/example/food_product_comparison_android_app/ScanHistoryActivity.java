package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class ScanHistoryActivity extends AppCompatActivity {
    private static final String SCAN_HISTORY_END_POINT = "api/";
    private URL webServiceUrl;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_with_product_list);

        this.setUpToolbar();
        ((TextView) findViewById(R.id.activity_title)).setText(getString(R.string.scan_history));

        this.setUpContent();
    }

    private void setUpContent()
    {
        this.recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);

        // Send a GET request via HttpsURLConnection
        this.handleOnGetScannedProducts(System.currentTimeMillis());
    }

    private void handleOnGetScannedProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ArrayList<Object> items;

            try {
                webServiceUrl = new URL(getString(R.string.server_base_url) + SCAN_HISTORY_END_POINT);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) webServiceUrl.openConnection();

                if (httpsURLConnection.getResponseCode() >= 200 && httpsURLConnection.getResponseCode() < 300) // If successful
                {
                    loading_dialog.dismiss();

                    // -----------------------------------------------
                    // Parse Data
                    items = Utils.parseProductsFromResponse(this, httpsURLConnection);
                    // -----------------------------------------------

                    uiHandler.post(() -> {
                        ProductListRecyclerViewAdapter shAdapter = new ProductListRecyclerViewAdapter(getApplicationContext(), this, items);
                        recyclerView.setAdapter(shAdapter);
                    });
                }
                else
                {
                    // response code = NOT Successful
                    loading_dialog.dismiss();

                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        uiHandler.post(() -> {
                            handleOnGetScannedProducts(init_time);
                            try {
                                Log.e("DEBUG", "Update Password Response code: " + httpsURLConnection.getResponseCode());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else
                    {
                        uiHandler.post(() -> {
                            Toast.makeText(ScanHistoryActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        });
                    }
                }

            } catch(Exception e) {
                loading_dialog.dismiss();
                uiHandler.post(() -> {
                    Toast.makeText(ScanHistoryActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Server Related Exception Error: " + e);
                });
            }
        });
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
}