package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {
    private MaterialButton provide_feedback_btn;
    private TextView privacy_policy_btn;
    private TextView terms_and_conditions_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        this.setUpToolbar();
        this.findViews();

        provide_feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AboutUsActivity.this, FeedbackActivity.class));
            }
        });

        privacy_policy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, WebActivity.class);
                intent.putExtra(Utils.WEB_ACTIVITY_URL_TRANSFER_TAG, getString(R.string.scannera_privacy_policy_url));
                startActivity(intent);
            }
        });

        terms_and_conditions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUsActivity.this, WebActivity.class);
                intent.putExtra(Utils.WEB_ACTIVITY_URL_TRANSFER_TAG, getString(R.string.scannera_terms_and_conditions_url));
                startActivity(intent);
            }
        });
    }

    private void findViews()
    {
        this.provide_feedback_btn = findViewById(R.id.provide_feedback_btn);
        this.privacy_policy_btn = findViewById(R.id.privacy_policy_tv);
        this.terms_and_conditions_btn = findViewById(R.id.terms_and_conditions_tv);
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