package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarredProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Object> starred_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_with_product_list);

        this.setUpToolbar();
        ((TextView) findViewById(R.id.activity_title)).setText(getString(R.string.starred_products));

        this.setUpContent();
    }

    private void setUpContent()
    {
        this.recyclerView = findViewById(R.id.recyclerView);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        starred_products = new ArrayList<>();

        //******************************************************
        this.handleOnGetStarredProducts(System.currentTimeMillis());
        //******************************************************
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

    private void handleOnGetStarredProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        Call<List<Product>> call = Utils.getServerAPI(this).getStarredProducts(Utils.getLoggedUser(this).getId());

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    for(int i = 0; i < Objects.requireNonNull(response.body()).size(); i++)
                    {
                        starred_products.add(response.body().get(i));
                    }

                    ProductListRecyclerViewAdapter spAdapter = new ProductListRecyclerViewAdapter(getApplicationContext(),
                            StarredProductActivity.this, starred_products);
                    recyclerView.setAdapter(spAdapter);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        handleOnGetStarredProducts(init_time);
                        Log.e("DEBUG", "Get Starred products code: " + response.code());
                    }
                    else
                    {
                        Toast.makeText(StarredProductActivity.this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(StarredProductActivity.this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}