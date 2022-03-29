package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductComparisonsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_comparisons);

        this.setUpToolbar();
        this.findViews();
        this.setUpListeners();

        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            setUpContent();
            uiHandler.post(loading_dialog::dismiss);
        });
    }

    private void findViews()
    {
    }

    private void setUpListeners()
    {
    }

    private void setUpContent()
    {

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