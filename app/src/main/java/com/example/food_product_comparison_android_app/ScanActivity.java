package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.food_product_comparison_android_app.Fragments.CameraPermissionRequiredDialogFragment;
import com.google.zxing.Result;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private TextView hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        checkPermissions();
        hint = findViewById(R.id.scanner_hint);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    recordScan(result.getText());
                    hint.setText("");
                    mCodeScanner.stopPreview();
                    getProduct(System.currentTimeMillis(), result.getText());
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }
        });

        ImageButton back_btn = findViewById(R.id.top_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources(); // avoid memory leaks
    }

    private void checkPermissions()
    {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, Utils.CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utils.CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                DialogFragment cameraDialogFragment = new CameraPermissionRequiredDialogFragment(getApplicationContext().getPackageName());
                cameraDialogFragment.show(getSupportFragmentManager(), "Camera Permission Dialog");
            }
        }
    }

    private void getProduct(Long init_time, String product_barcode)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        Call<ResponseBody> call = Utils.getServerAPI(this).getASingleProduct(product_barcode);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    // -----------------------------------------------
                    // Parse Data | IT IS GUARANTEED THERE EXISTS ONE PRODUCT IN THE RESPONSE
                    Product product = Utils.parseASingleProductFromResponse(ScanActivity.this, response.body());
                    // -----------------------------------------------

                    Utils.navigateToProductInfoActivity(ScanActivity.this, product);
                }
                else if(response.code() == 405)
                {
                    // Product Not Found
                    showPNFDialog();
                }
                else
                {
                    // response code = NOT Successful
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        getProduct(init_time, product_barcode);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        mCodeScanner.startPreview();
                        Toast.makeText(ScanActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                mCodeScanner.startPreview();
                Toast.makeText(ScanActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recordScan(String barcode)
    {
        // Send a post request to the server
    }

    private void showPNFDialog()
    {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_product_not_found);

        dialog.findViewById(R.id.not_now_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(ScanActivity.this, ProductFeedActivity.class));
            }
        });

        dialog.show();
    }
}