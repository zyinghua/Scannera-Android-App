package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.LoadingDialog;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StarredProductActivity extends AppCompatActivity {
    private ImageButton top_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_with_product_list);

        ((TextView) findViewById(R.id.activity_title)).setText(getString(R.string.starred_products));

        this.top_back_btn = findViewById(R.id.top_back_btn);
        top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            setUpContent();
            uiHandler.post(loading_dialog::dismiss);
        });
    }

    private void setUpContent()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Product> starred_products = new ArrayList<>();

        //******************************************************
        starred_products.add(new Product("12431435235", "abcasgagahgewheasrhsadrhsedrthsedr", "dd", 55.5f, false));
        starred_products.add(new Product("12342342334","Monash", "Cokecola chocolate milk balala", 66.6f, true));
        //******************************************************

        StarredProductListRecyclerViewAdapter spAdapter = new StarredProductListRecyclerViewAdapter(getApplicationContext(), starred_products);
        recyclerView.setAdapter(spAdapter);
    }
}