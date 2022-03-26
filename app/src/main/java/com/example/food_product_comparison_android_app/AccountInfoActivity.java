package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AccountInfoActivity extends AppCompatActivity {
    private ImageButton top_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        this.findViews();
        this.setUpListeners();
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
    }

    private void setUpListeners()
    {
        this.top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Send edited user account information to the server here
    }
}