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
import com.example.food_product_comparison_android_app.Dialogs.LoadingDialog;
import com.example.food_product_comparison_android_app.Fragments.PermissionRequiredDialogFragment;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.Product;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.User;
import com.google.zxing.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private TextView hint;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        this.user = Utils.getLoggedUser(this);
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
                DialogFragment cameraDialogFragment = new PermissionRequiredDialogFragment(
                        getApplicationContext().getPackageName(), getString(R.string.camera), Utils.ON_PERMISSION_DENIED_BACK);
                cameraDialogFragment.show(getSupportFragmentManager(), "Camera Permission Dialog");
            }
        }
    }

    private void getProduct(Long init_time, String product_barcode)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            if (loading_dialog.isShowing())
                loading_dialog.dismiss();
        }

        Call<ResponseBody> call = Utils.getServerAPI(this).getASingleProduct(product_barcode, user.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (loading_dialog.isShowing())
                    loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    // -----------------------------------------------
                    // Parse Data | IT IS GUARANTEED THERE EXISTS ONE PRODUCT IN THE RESPONSE
                    recordScan(System.currentTimeMillis(), product_barcode);
                    Product product = Utils.parseASingleProductFromResponse(ScanActivity.this, response.body());
                    // -----------------------------------------------

                    Utils.navigateToProductInfoActivity(ScanActivity.this, product);
                }
                else if(response.code() == 405)
                {
                    // Product Not Found
                    showPNFDialog(product_barcode);
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
                if (loading_dialog.isShowing())
                    loading_dialog.dismiss();

                mCodeScanner.startPreview();
                Toast.makeText(ScanActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recordScan(Long init_time, String product_barcode)
    {
        // Send a post request to the server
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<Void> call = Utils.getServerAPI(this).addScannedProduct(user.getId(), product_barcode);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if(!response.isSuccessful())
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        recordScan(init_time, product_barcode);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(ScanActivity.this, getString(R.string.record_scan_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(ScanActivity.this, getString(R.string.record_scan_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showPNFDialog(String product_barcode)
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

                Intent intent = new Intent(ScanActivity.this, ProductFeedActivity.class);
                intent.putExtra(Utils.PRODUCT_BARCODE_TRANSFER_TAG, product_barcode);
                startActivity(intent);
            }
        });

        dialog.show();
    }
}