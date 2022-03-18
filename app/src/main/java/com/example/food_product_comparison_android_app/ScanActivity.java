package com.example.food_product_comparison_android_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 814736521;
    private CodeScanner mCodeScanner;
    private ImageButton close_btn;
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
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
                Handler uiHandler = new Handler(Looper.getMainLooper());

                singleExecutor.execute(() -> {
                    recordScan(result);

                    uiHandler.post(() -> {
                        hint.setText("");
                        Toast.makeText(ScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        if (!find_product())
                        {
                            mCodeScanner.stopPreview();
                            showPNFDialog();
                        }
                    });
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }
        });

        close_btn = findViewById(R.id.top_close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
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
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    DialogFragment cameraDialogFragment = new CameraPermissionRequiredDialogFragment(getApplicationContext().getPackageName());
                    cameraDialogFragment.show(getSupportFragmentManager(), "Camera Permission");
                }
                break;
            default:
                break;
        }
    }

    private boolean find_product()
    {
        // Send a request to the server here
        return false;
    }

    private void recordScan(Result result)
    {
        SharedPreferences sp = getSharedPreferences("ScanHistory", 0);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        String str_products = sp.getString("ScanHistoryProducts", "");
        String str_dates = sp.getString("ScanHistoryDates", "");
        Type ptype = new TypeToken<ArrayList<Product>>() {}.getType();
        Type dtype = new TypeToken<ArrayList<String>>() {}.getType();

        ArrayList<Product> products = gson.fromJson(str_products, ptype);
        ArrayList<String> dates = gson.fromJson(str_dates, dtype);

        products.add(new Product(result.getText()));
        dates.add(new SimpleDateFormat("d MMM yyyy, EEEE", Locale.getDefault()).format(new Date()));
        editor.putString("ScanHistoryProducts", gson.toJson(products));
        editor.putString("ScanHistoryDates", gson.toJson(dates));

        editor.apply();
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
            }
        });

        dialog.show();
    }
}