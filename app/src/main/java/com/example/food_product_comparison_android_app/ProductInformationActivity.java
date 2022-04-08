package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.Fragments.SimilarProductsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductInformationActivity extends AppCompatActivity {
    private ImageView product_look;
    private TextView category_tv;
    private TextView name_tv;
    private TextView brand_tv;
    private TextView price_tv;
    private ImageButton star_btn;
    private RecyclerView nutrition_recycler_view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);

        this.setUpToolbar();
        this.findViews();
        this.setUpListeners();

        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        this.product_look.setImageDrawable(getDrawable(R.drawable.product_sample));
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
        this.tabLayout = findViewById(R.id.comparisons_tab_layout);
        this.viewPager = findViewById(R.id.comparisons_view_pager);
    }

    private void setUpListeners()
    {
        this.product_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnlargedImageDialog image_dialog = new EnlargedImageDialog(ProductInformationActivity.this);
                image_dialog.show();
            }
        });
    }

    private void setUpContent()
    {
        //******************************************************
        this.category_tv.setText("Energy Pizza");
        this.name_tv.setText("Chocolate Pizza");
        this.brand_tv.setText("Swisse");
        this.price_tv.setText("$888.88");
        //******************************************************

        // Nutrition Recycler View Set Up
        this.setUpNutritionRecyclerView();

        //Tab Layout & ViewPager Set Up
        this.setUpTabLayoutViewPager();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpNutritionRecyclerView()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.nutrition_recycler_view.setLayoutManager(layoutManager);

        ArrayList<NutritionAttribute> nutritional_attributes = new ArrayList<>();

        //******************************************************
        nutritional_attributes.add(new NutritionAttribute("Energy", "100 kJ"));
        nutritional_attributes.add(new NutritionAttribute("Sugar", "0.5g"));
        nutritional_attributes.add(new NutritionAttribute("Fat Total", "0.5g"));
        nutritional_attributes.add(new NutritionAttribute("Sodium", "600mg"));
        nutritional_attributes.add(new NutritionAttribute("Dietary Fibre", "40g"));
        //******************************************************

        NutritionListRecyclerViewAdapter nuAdapter = new NutritionListRecyclerViewAdapter(nutritional_attributes);
        this.nutrition_recycler_view.setAdapter(nuAdapter);
    }

    private void setUpTabLayoutViewPager()
    {
        this.tabLayout.setupWithViewPager(this.viewPager);

        ComparisonsVPAdapter vpAdapter = new ComparisonsVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new SimilarProductsFragment(), getString(R.string.similar_products));

        this.viewPager.setAdapter(vpAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            // This is to sync the toolbar up button with the back button
            onBackPressed();
            return true;
        }
        else if (item.getItemId() == R.id.home_toolbar)
        {
            Intent intent = new Intent(ProductInformationActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Set up bottom View Pager's height, otherwise nothing will be shown.
        // Allow scrolling down the outer scroll view up to the tab layout.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = this.viewPager.getLayoutParams();
        params.height = displayMetrics.heightPixels - this.tabLayout.getHeight();
        this.viewPager.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comparisons_toolbar_menu, menu);
        return true;
    }
}