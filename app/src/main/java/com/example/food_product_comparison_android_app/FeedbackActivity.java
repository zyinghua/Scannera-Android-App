package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.facebook.login.Login;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    private RatingBar rating_bar;
    private EditText feedback_et;
    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        this.setUpToolbar();
        this.findViews();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = rating_bar.getRating();
                String feedback = feedback_et.getText().toString();

                if (rating == 0.0)
                {
                    Animation shake = AnimationUtils.loadAnimation(FeedbackActivity.this, R.anim.shake);
                    rating_bar.startAnimation(shake);

                    Toast.makeText(FeedbackActivity.this, getString(R.string.on_feedback_empty_msg), Toast.LENGTH_LONG).show();
                }
                else if (feedback.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(FeedbackActivity.this, R.anim.shake);
                    feedback_et.startAnimation(shake);

                    Toast.makeText(FeedbackActivity.this, getString(R.string.on_feedback_empty_msg), Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Send the rating and feedback to the server here
                    postFeedback(System.currentTimeMillis(),
                            new Feedback(Utils.getLoggedUser(FeedbackActivity.this).getId(), rating, feedback));
                }
            }
        });
    }

    private void findViews()
    {
        this.rating_bar = findViewById(R.id.about_us_rating_bar);
        this.feedback_et = findViewById(R.id.feedback_et);
        this.submit_btn = findViewById(R.id.submit_btn);
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

    private void postFeedback(Long init_time, Feedback feedback)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        Call<Void> call = Utils.getServerAPI(this).postFeedback(feedback.getUserId(), feedback.getRating(), feedback.getDescription());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    Toast.makeText(FeedbackActivity.this, getString(R.string.feedback_success), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        postFeedback(init_time, feedback);
                        Log.e("DEBUG", "Feedback response code: " + response.code());
                    }
                    else
                    {
                        Toast.makeText(FeedbackActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(FeedbackActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}