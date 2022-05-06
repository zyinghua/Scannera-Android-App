package com.fit3162.scannera_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fit3162.scannera_app.Dialogs.LoadingDialog;
import com.fit3162.scannera_app.GeneralJavaClasses.User;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmProfileImageActivity extends AppCompatActivity {
    private CircularImageView user_profile_img;
    private MaterialButton confirm_btn;
    private MaterialButton cancel_btn;
    private File selected_image_file;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_profile_image);

        selected_image_file = new Gson().fromJson(getIntent().getStringExtra(Utils.IMAGE_FILE_TRANSFER_TAG),
                new TypeToken<File>() {}.getType());

        this.user = Utils.getLoggedUser(this);
        this.findViews();
        this.setUpToolbar();
        this.setUpContent();
    }

    private void findViews()
    {
        this.user_profile_img = findViewById(R.id.user_profile_img);
        this.confirm_btn = findViewById(R.id.confirm_btn);
        this.cancel_btn = findViewById(R.id.cancel_btn);
    }

    private void setUpContent()
    {
        Bitmap imgBitmap = BitmapFactory.decodeFile(selected_image_file.getAbsolutePath());
        this.user_profile_img.setImageBitmap(imgBitmap);

        this.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUserProfileImageUpdate(System.currentTimeMillis());
            }
        });

        this.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void requestUserProfileImageUpdate(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }
        
        RequestBody user_id_rb = RequestBody.create(MediaType.parse("text/plain"), user.getId());
        MultipartBody.Part user_pimg_file_part = MultipartBody.Part.createFormData(
                ServerRetrofitAPI.PROFILE_IMG_SERVER, selected_image_file.getName(), RequestBody.create(MediaType.parse("image/*"), selected_image_file));

        Call<ResponseBody> call = Utils.getServerAPI(this).updateUserProfileImage(user_id_rb, user_pimg_file_part);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful() && response.body() != null)
                {
                    try {
                        InputStream responseBodyIS = response.body().byteStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBodyIS, StandardCharsets.UTF_8);
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        String keyName;
                        String new_pimg_url = "";

                        jsonReader.beginObject();

                        while (jsonReader.hasNext()) {
                            keyName = jsonReader.nextName();

                            if (keyName.equals("user_pimg_url"))
                            {
                                new_pimg_url = jsonReader.nextString();
                            }
                            else
                            {
                                jsonReader.skipValue();
                            }
                        }

                        jsonReader.endObject();

                        if(!new_pimg_url.isEmpty())
                        {
                            user.setProfile_img_url(new_pimg_url);
                            Utils.updateUserLoginStatus(ConfirmProfileImageActivity.this, user);
                            Toast.makeText(ConfirmProfileImageActivity.this, getString(R.string.user_pimg_updated), Toast.LENGTH_LONG).show();
                        }
                        else {
                            showErrorToast();
                        }

                        onBackPressed();
                    } catch(IOException e) {
                        showErrorToast();
                    }
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        requestUserProfileImageUpdate(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        showErrorToast();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                showErrorToast();
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

    private void showErrorToast()
    {
        Toast.makeText(ConfirmProfileImageActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
    }
}