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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_with_product_list);

        this.setUpToolbar();
        ((TextView) findViewById(R.id.activity_title)).setText(getString(R.string.scan_history));

        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            this.setUpContent();
            uiHandler.post(loading_dialog::dismiss);
        });
    }

    private void setUpContent()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // -----------------------------------------------
        SharedPreferences sp = getSharedPreferences("ScanHistory", 0);

        Gson gson = new Gson();
        String str_products = sp.getString("ScanHistoryProducts", "");
        String str_dates = sp.getString("ScanHistoryDates", "");

        Type ptype = new TypeToken<ArrayList<Product>>() {}.getType();
        Type dtype = new TypeToken<ArrayList<String>>() {}.getType();

        ArrayList<Product> products = gson.fromJson(str_products, ptype);
        ArrayList<String> dates = gson.fromJson(str_dates, dtype);
        ArrayList<Object> items = new ArrayList<>();
        // -----------------------------------------------

        for (int i = products.size() - 1; i >= 0; i--)
        {
            if (i == products.size() - 1 || dates.get(i).compareTo(dates.get(i + 1)) < 0)
            {
                items.add(dates.get(i));
            }

            items.add(products.get(i));
        }
        // -----------------------------------------------

        ProductListRecyclerViewAdapter shAdapter = new ProductListRecyclerViewAdapter(getApplicationContext(),this, items);
        recyclerView.setAdapter(shAdapter);
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