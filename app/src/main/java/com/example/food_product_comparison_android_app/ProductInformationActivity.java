package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Adapters.GeneralVPAdapter;
import com.example.food_product_comparison_android_app.Adapters.NutritionListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.Adapters.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.Dialogs.EnlargedImageDialog;
import com.example.food_product_comparison_android_app.Dialogs.LoadingDialog;
import com.example.food_product_comparison_android_app.Fragments.ProductReviewsFragment;
import com.example.food_product_comparison_android_app.Fragments.SimilarProductsFragment;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.NutritionAttribute;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.Product;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInformationActivity extends AppCompatActivity {
    private Product product;
    private ImageView product_pic;
    private TextView category_tv;
    private TextView name_tv;
    private TextView brand_tv;
    private TextView price_tv;
    private ImageButton star_btn;
    private RecyclerView nutrition_recycler_view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HashMap<String, Float> nutritionAverages;
    private ArrayList<Object> similarProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);

        this.getProductInfo();
        this.setUpToolbar();
        this.findViews();
        this.setUpListeners();
        this.setUpContent();
    }

    private void getProductInfo()
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Product>() {}.getType();
        this.product = gson.fromJson(getIntent().getStringExtra(Utils.PRODUCT_TRANSFER_TAG), type);
    }

    private void findViews()
    {
        this.product_pic = findViewById(R.id.product_look_pic);
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
        this.product_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getProductImgUrl() != null && !product.getProductImgUrl().isEmpty())
                {
                    final EnlargedImageDialog image_dialog = new EnlargedImageDialog(ProductInformationActivity.this, product.getProductImgUrl());
                    image_dialog.show();
                }
                else
                {
                    Toast.makeText(ProductInformationActivity.this, getString(R.string.product_image_empty_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        this.star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toggleProductStar(System.currentTimeMillis(), ProductInformationActivity.this,
                        getApplicationContext(), star_btn, product);
            }
        });
    }

    private void setUpContent()
    {
        try{
            this.star_btn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    product.getStarred() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off));

            if(product.getProductImgUrl() != null && !product.getProductImgUrl().isEmpty())
                Picasso.get().load(product.getProductImgUrl()).into(this.product_pic);

            this.category_tv.setText(product.getCategory());
            this.name_tv.setText(product.getName());
            this.brand_tv.setText(product.getBrand());
            this.price_tv.setText(product.getPriceInString());

        } catch (NullPointerException e) {
            Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        handleOnGetSimilarProducts(System.currentTimeMillis());
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

        NutritionListRecyclerViewAdapter nuAdapter = new NutritionListRecyclerViewAdapter(this, new ArrayList<>(product.getNutritionAttributes().values()), nutritionAverages);
        this.nutrition_recycler_view.setAdapter(nuAdapter);
    }

    private void setUpTabLayoutViewPager()
    {
        this.tabLayout.setupWithViewPager(this.viewPager);

        GeneralVPAdapter vpAdapter = new GeneralVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new SimilarProductsFragment(), getString(R.string.similar_products));
        vpAdapter.addFragment(new ProductReviewsFragment(), getString(R.string.product_reviews));

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

    public Product getProduct() {
        return product;
    }

    public ArrayList<Object> getSimilarProducts() {
        return similarProducts;
    }

    private void handleOnGetSimilarProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<ResponseBody> call = Utils.getServerAPI(this).getSimilarProducts(
                product.getProductId(), Utils.getLoggedUser(this).getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    // All the following items are dependent on the similar products.
                    similarProducts = Utils.parseProductsFromResponse(ProductInformationActivity.this, response.body());
                    setUpNutritionAverages();

                    // Nutrition Recycler View Set Up
                    setUpNutritionRecyclerView();

                    //Tab Layout & ViewPager Set Up
                    setUpTabLayoutViewPager();
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        handleOnGetSimilarProducts(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(ProductInformationActivity.this, getString(R.string.similar_product_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(ProductInformationActivity.this, getString(R.string.similar_product_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setUpNutritionAverages() {
        nutritionAverages = new HashMap<>();

        if (product != null)
        {
            for (String key : product.getNutritionAttributes().keySet())
            {
                // Initialise nutritionAverages hashmap
                nutritionAverages.put(key, 0.0f);
            }

            for (int i = 0; i < similarProducts.size(); i++)
            {
                Product similarProduct = (Product) similarProducts.get(i);

                // Incrementally update the value of each nutrition attribute,
                // eventually will end up with values in total among the similar products.
                nutritionAverages.replaceAll((k, v) -> nutritionAverages.get(k) + similarProduct.getSpecificProductValue(k));
            }

            if(similarProducts.size() != 0)
                nutritionAverages.replaceAll((k, v) -> nutritionAverages.get(k) / similarProducts.size());
        }
    }
}