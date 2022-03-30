package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductComparisonsActivity extends AppCompatActivity {
    private ImageView product_look;
    private TextView category_tv;
    private TextView name_tv;
    private TextView brand_tv;
    private TextView price_tv;
    private ImageButton star_btn;
    private RecyclerView nutrition_recycler_view;

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
        this.product_look = findViewById(R.id.comparisons_product_look_pic);
        this.category_tv = findViewById(R.id.product_category_value);
        this.name_tv = findViewById(R.id.product_name_value);
        this.brand_tv = findViewById(R.id.product_brand_value);
        this.price_tv = findViewById(R.id.product_price_value);
        this.star_btn = findViewById(R.id.star_btn);
        this.nutrition_recycler_view = findViewById(R.id.nutrition_recyclerView);
    }

    private void setUpListeners()
    {
        this.product_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnlargedImageDialog image_dialog = new EnlargedImageDialog(ProductComparisonsActivity.this);
                image_dialog.show();
            }
        });
    }

    private void setUpContent()
    {
        this.product_look.setImageDrawable(getDrawable(R.drawable.monash_uni_img));
        this.category_tv.setText("Energy Pizza");
        this.name_tv.setText("Chocolate Pizza");
        this.brand_tv.setText("Swisse");
        this.price_tv.setText("$888.88");

        // Nutrition Recycler View Set Up
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.nutrition_recycler_view.setLayoutManager(layoutManager);

        ArrayList<NutritionalAttribute> nutritional_attributes = new ArrayList<>();

        //******************************************************
        nutritional_attributes.add(new NutritionalAttribute("Energy", "100 kJ"));
        nutritional_attributes.add(new NutritionalAttribute("Sugar", "0.5g"));
        nutritional_attributes.add(new NutritionalAttribute("Fat Total", "0.5g"));
        nutritional_attributes.add(new NutritionalAttribute("Sodium", "600mg"));
        nutritional_attributes.add(new NutritionalAttribute("Dietary Fibre", "40g"));
        //******************************************************

        NutritionListRecyclerViewAdapter nuAdapter = new NutritionListRecyclerViewAdapter(nutritional_attributes);
        this.nutrition_recycler_view.setAdapter(nuAdapter);
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