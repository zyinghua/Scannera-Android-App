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
        // Send a request to the server here
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Product product;

            try {
                URL webServiceUrl = new URL(getString(R.string.server_base_url) +
                        String.format(Utils.GET_PRODUCT_END_POINT, product_barcode));
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) webServiceUrl.openConnection();

                if (httpsURLConnection.getResponseCode() >= 200 && httpsURLConnection.getResponseCode() < 300) // If successful
                {
                    loading_dialog.dismiss();

                    // -----------------------------------------------
                    // Parse Data | IT IS GUARANTEED THERE EXISTS ONE PRODUCT IN THE RESPONSE
                    product = Utils.parseASingleProductFromResponse(this, httpsURLConnection);
                    // -----------------------------------------------

                    uiHandler.post(() -> {
                        Utils.navigateToProductInfoActivity(this, product);
                    });
                }
                else if (httpsURLConnection.getResponseCode() == 405)
                {
                    // Product Not Found
                    loading_dialog.dismiss();
                    uiHandler.post(this::showPNFDialog);
                }
                else
                {
                    // response code = NOT Successful
                    loading_dialog.dismiss();

                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        uiHandler.post(() -> {
                            getProduct(init_time, product_barcode);
                            try {
                                Log.e("DEBUG", "Scan Product Barcode Response code: " + httpsURLConnection.getResponseCode());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else
                    {
                        uiHandler.post(() -> {
                            mCodeScanner.startPreview();
                            Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        });
                    }
                }

            } catch(Exception e) {
                loading_dialog.dismiss();
                uiHandler.post(() -> {
                    mCodeScanner.startPreview();
                    Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Server Related Exception Error: " + e);
                });
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