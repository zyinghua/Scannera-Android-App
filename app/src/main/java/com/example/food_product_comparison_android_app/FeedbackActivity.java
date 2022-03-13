package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class FeedbackActivity extends AppCompatActivity {
    private ImageButton top_back_btn;
    private RatingBar rating_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        this.findViews();

        top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
        this.rating_bar = findViewById(R.id.about_us_rating_bar);
    }
}