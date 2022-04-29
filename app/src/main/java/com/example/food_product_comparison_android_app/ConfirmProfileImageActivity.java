package com.example.food_product_comparison_android_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.io.File;
import java.util.Objects;

import retrofit2.Call;

public class ConfirmProfileImageActivity extends AppCompatActivity {
    private CircularImageView user_profile_img;
    private MaterialButton confirm_btn;
    private MaterialButton cancel_btn;
    private File selected_image_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_profile_image);

        selected_image_file = new Gson().fromJson(getIntent().getStringExtra(Utils.IMAGE_FILE_TRANSFER_TAG),
                new TypeToken<File>() {}.getType());

        this.findViews();
        this.setUpToolbar();
        this.setUpContent();
    }

    private void findViews()
    {
        this.user_profile_img = findViewById(R.id.user_profile_img);
        this.confirm_btn = findViewById(R.id.confirm_btn);
        this.cancel_btn = findViewById(R.id.cancel_btn);
    }

    private void setUpContent()
    {
        Bitmap imgBitmap = BitmapFactory.decodeFile(selected_image_file.getAbsolutePath());
        this.user_profile_img.setImageBitmap(imgBitmap);

        this.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUserProfileImageUpdate(System.currentTimeMillis());
            }
        });

        this.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void requestUserProfileImageUpdate(Long init_time)
    {
        onBackPressed();


        //Call<String> call = Utils.getServerAPI(this).updateUserProfileImage();
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