package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.LoadingFragment;
import com.example.food_product_comparison_android_app.Fragments.ScanHistoryListContentFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanHistoryActivity extends AppCompatActivity {
    private ImageButton top_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.SH_content_container, new LoadingFragment()).commit();
        this.top_back_btn = findViewById(R.id.top_back_btn);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        executor.execute(()->{
            ScanHistoryListContentFragment content_frag = new ScanHistoryListContentFragment();
            uiHandler.post(()->{
                getSupportFragmentManager().beginTransaction().replace(R.id.SH_content_container, content_frag).commit();
            });
        });
    }
}