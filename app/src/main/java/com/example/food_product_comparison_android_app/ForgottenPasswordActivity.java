package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ForgottenPasswordActivity extends AppCompatActivity {
    private ImageButton top_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        this.findViews();
        this.setDefaultListeners();
        this.setAnimationsOnStart();
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
    }

    private void setDefaultListeners()
    {
        this.top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setAnimationsOnStart()
    {

    }
}