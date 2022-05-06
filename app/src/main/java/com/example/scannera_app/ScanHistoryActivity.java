package com.example.scannera_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scannera_app.Adapters.ProductListRecyclerViewAdapter;
import com.example.scannera_app.Dialogs.LoadingDialog;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_with_product_list);

        this.setUpToolbar();
        ((TextView) findViewById(R.id.activity_title)).setText(getString(R.string.scan_history));

        this.setUpContent();
    }

    private void setUpContent()
    {
        this.recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);

        // Send a GET request via HttpsURLConnection
        this.handleOnGetScannedProducts(System.currentTimeMillis());
    }

    private void handleOnGetScannedProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<ResponseBody> call = Utils.getServerAPI(this).getScannedProducts(Utils.getLoggedUser(this).getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    ArrayList<Object> items = Utils.parseProductsFromResponse(ScanHistoryActivity.this, response.body());
                    ProductListRecyclerViewAdapter shAdapter = new ProductListRecyclerViewAdapter(getApplicationContext(),
                            ScanHistoryActivity.this, items);
                    recyclerView.setAdapter(shAdapter);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        handleOnGetScannedProducts(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(ScanHistoryActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(ScanHistoryActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
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