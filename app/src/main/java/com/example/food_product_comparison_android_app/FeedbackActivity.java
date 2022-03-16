package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.facebook.login.Login;

public class FeedbackActivity extends AppCompatActivity {
    private ImageButton top_back_btn;
    private RatingBar rating_bar;
    private EditText feedback_et;
    private Button submit_btn;

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

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = rating_bar.getRating();
                String feedback = feedback_et.getText().toString();

                if (rating == 0.0)
                {
                    Animation shake = AnimationUtils.loadAnimation(FeedbackActivity.this, R.anim.shake);
                    rating_bar.startAnimation(shake);

                    Toast.makeText(FeedbackActivity.this, getString(R.string.rating_empty), Toast.LENGTH_LONG).show();
                }
                else if (feedback.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(FeedbackActivity.this, R.anim.shake);
                    feedback_et.startAnimation(shake);

                    Toast.makeText(FeedbackActivity.this, getString(R.string.feedback_empty), Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Store the rating and feedback here
                    Toast.makeText(FeedbackActivity.this, getString(R.string.feedback_success), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
        this.rating_bar = findViewById(R.id.about_us_rating_bar);
        this.feedback_et = findViewById(R.id.feedback_et);
        this.submit_btn = findViewById(R.id.submit_btn);
    }
}