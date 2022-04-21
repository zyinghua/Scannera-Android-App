package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.food_product_comparison_android_app.Adapters.GeneralVPAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class AppGuideActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        this.tabLayout = findViewById(R.id.app_guide_tab_layout);
        this.viewPager = findViewById(R.id.app_guide_vp_container);

        setUpToolbar();
        setUpTabLayoutViewPager();
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

    private void setUpTabLayoutViewPager()
    {
        this.tabLayout.setupWithViewPager(this.viewPager);

        GeneralVPAdapter vpAdapter = new GeneralVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //vpAdapter.addFragment(new SimilarProductsFragment(), getString(R.string.similar_products));

        this.viewPager.setAdapter(vpAdapter);
    }
}