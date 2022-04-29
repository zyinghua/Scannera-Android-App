package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.food_product_comparison_android_app.Dialogs.EditDialog;
import com.example.food_product_comparison_android_app.Dialogs.EnlargedImageDialog;
import com.example.food_product_comparison_android_app.Dialogs.LoadingDialog;
import com.example.food_product_comparison_android_app.Fragments.CameraPermissionRequiredDialogFragment;
import com.example.food_product_comparison_android_app.Fragments.DeleteAccountConfirmDialogFragment;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInfoActivity extends AppCompatActivity {
    private static final int SELECT_FROM_PHOTO_ALBUM = 0;
    private static final int TAKE_A_PHOTO = 1;
    private User user;
    private CircularImageView user_profile_img;
    private TextView username_tv;
    private TextView password_tv;
    private TextView email_address_tv;
    private TextView firstname_tv;
    private TextView lastname_tv;
    private FloatingActionButton user_profile_img_edit_fab;
    private MaterialButton edit_username_btn;
    private MaterialButton edit_password_btn;
    private MaterialButton edit_firstname_btn;
    private MaterialButton edit_lastname_btn;
    private MaterialButton delete_acc_btn;
    private File selected_profile_image_file;
    public static final String SELECTED_PROFILE_IMAGE_FILENAME = "selectedImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        // Get user information
        user = Utils.getLoggedUser(this);

        this.setUpToolbar();
        this.findViews();
        this.populateWithUserData();
        this.setUpListeners();
    }

    private void findViews()
    {
        this.user_profile_img = findViewById(R.id.user_profile_img);
        this.username_tv = findViewById(R.id.username_tv);
        this.password_tv = findViewById(R.id.password_tv);
        this.email_address_tv = findViewById(R.id.email_address_tv);
        this.firstname_tv = findViewById(R.id.firstname_tv);
        this.lastname_tv = findViewById(R.id.lastname_tv);
        this.user_profile_img_edit_fab = findViewById(R.id.user_profile_img_edit_fab);
        this.edit_username_btn = findViewById(R.id.edit_username_btn);
        this.edit_password_btn = findViewById(R.id.edit_password_btn);
        this.edit_firstname_btn = findViewById(R.id.edit_firstname_btn);
        this.edit_lastname_btn = findViewById(R.id.edit_lastname_btn);
        this.delete_acc_btn = findViewById(R.id.delete_acc_btn);
    }

    private void setUpListeners()
    {
        this.user_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getProfile_img_url() != null && !user.getProfile_img_url().equals("null") && !user.getProfile_img_url().isEmpty())
                {
                    final EnlargedImageDialog image_dialog = new EnlargedImageDialog(AccountInfoActivity.this, user.getProfile_img_url());
                    image_dialog.show();
                }
                else
                {
                    Toast.makeText(AccountInfoActivity.this, getString(R.string.user_image_empty_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.user_profile_img_edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectProfileImgBottomSheetDialog();
            }
        });

        this.edit_username_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(getString(R.string.username_label), Utils.USERNAME_INPUT);
            }
        });

        this.edit_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(getString(R.string.password_label), Utils.PASSWORD_INPUT);
            }
        });

        this.edit_firstname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(getString(R.string.firstname_label), Utils.FIRSTNAME_INPUT);
            }
        });

        this.edit_lastname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(getString(R.string.lastname_label), Utils.LASTNAME_INPUT);
            }
        });

        this.delete_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountConfirmDialogFragment deletionConfirmDialog = new DeleteAccountConfirmDialogFragment();
                deletionConfirmDialog.show(getSupportFragmentManager(), "Delete Account Confirm Dialog");
            }
        });
    }

    private void populateWithUserData()
    {
        if (user.getProfile_img_url() != null && !user.getProfile_img_url().equals("null") && !user.getProfile_img_url().isEmpty())
            Picasso.get().load(user.getProfile_img_url()).into(user_profile_img);

        this.username_tv.setText(this.user.getUsername());
        this.password_tv.setText(this.user.getPassword() == null ? "" : new String(new char[this.user.getPassword().length()]).replace("\0", "*"));
        this.email_address_tv.setText(this.user.getEmail());
        this.firstname_tv.setText(this.user.getFirstname());
        this.lastname_tv.setText(this.user.getLastname());
    }

    private void showEditDialog(String title, int field)
    {
        final EditDialog dialog = new EditDialog(this, title);

        switch (field)
        {
            case Utils.USERNAME_INPUT:
                dialog.setMaxInputLength(Utils.MAX_LEN_USERNAME);
                break;
            case Utils.PASSWORD_INPUT:
                dialog.setMaxInputLength(Utils.MAX_LEN_PASSWORD);
                break;
            case Utils.FIRSTNAME_INPUT:
                dialog.setMaxInputLength(Utils.MAX_LEN_FIRSTNAME);
                break;
            case Utils.LASTNAME_INPUT:
                dialog.setMaxInputLength(Utils.MAX_LEN_LASTNAME);
                break;
            default:
                break;
        }

        dialog.setConfirmBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText edited_input_et = dialog.findViewById(R.id.edit_et);
                TextInputLayout edit_input_layout = dialog.findViewById(R.id.edit_text_input_layout);
                String new_data = Objects.requireNonNull(edited_input_et.getText()).toString();
                edit_input_layout.setError(null);
                String result = Utils.validateUserInfoInput(AccountInfoActivity.this, new_data, field);

                if (!result.equals(getString(R.string.valid_user_input))) {
                    // If input not valid, checked locally.
                    edit_input_layout.setError(result);
                }
                else
                {
                    // Send an update request to the server here...
                    updateUserInfo(System.currentTimeMillis(), new_data, field, dialog);
                }
            }
        });

        dialog.show();
    }

    private void revokeGoogleAccess() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignIn.getClient(this, gso).revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        mGoogleSignInClient.signOut();
                    }
                });
    }

    private void revokeFacebookAccess() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null)
            return;

        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                String.format(getString(R.string.facebook_delete_api), accessToken.getUserId()),
                response -> {
                    // Response: On Complete
                    LoginManager.getInstance().logOut();
                }
        );

        request.setHttpMethod(HttpMethod.DELETE);
        request.executeAsync();
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

    private void showSelectProfileImgBottomSheetDialog()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet_profile_img_select);

        LinearLayout select_from_album = bottomSheetDialog.findViewById(R.id.select_from_photo_album_layout);
        LinearLayout take_a_photo = bottomSheetDialog.findViewById(R.id.take_a_photo_layout);

        if (select_from_album != null) {
            select_from_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    checkPermissionsBeforeExecution(SELECT_FROM_PHOTO_ALBUM);
                }
            });
        }

        if (take_a_photo != null) {
            take_a_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    checkPermissionsBeforeExecution(TAKE_A_PHOTO);
                }
            });
        }

        bottomSheetDialog.show();
    }

    private void updateUserInfo(Long init_time, String new_data, int field, EditDialog dialog)
    {
        Call<Void> call;
        TextView field_tv;
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        if (field == Utils.USERNAME_INPUT)
        {
            call = Utils.getServerAPI(this).updateUsernameById(this.user.getId(), new_data);
            field_tv = username_tv;
        }
        else if(field == Utils.PASSWORD_INPUT)
        {
            call = Utils.getServerAPI(this).updateUserPasswordById(this.user.getId(), new_data);
            field_tv = password_tv;
        }
        else if(field == Utils.FIRSTNAME_INPUT)
        {
            call = Utils.getServerAPI(this).updateUserFirstnameById(this.user.getId(), new_data);
            field_tv = firstname_tv;
        }
        else { // Last name
            call = Utils.getServerAPI(this).updateUserLastnameById(this.user.getId(), new_data);
            field_tv = lastname_tv;
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful())
                {
                    if (field == Utils.USERNAME_INPUT)
                        user.setUsername(new_data);
                    else if(field == Utils.PASSWORD_INPUT)
                        user.setPassword(new_data);
                    else if(field == Utils.FIRSTNAME_INPUT)
                        user.setFirstname(new_data);
                    else user.setLastname(new_data); // Smart Android IDE

                    field_tv.setText(field == Utils.PASSWORD_INPUT?
                            new String(new char[new_data.length()]).replace("\0", "*") : new_data);
                    dialog.dismiss();
                }
                else if(response.code() == 405)
                {
                    // Username already exists
                    TextInputLayout edit_input_layout = dialog.findViewById(R.id.edit_text_input_layout);
                    edit_input_layout.setError(getString(R.string.username_taken_error));
                }
                else {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        updateUserInfo(init_time, new_data, field, dialog);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(AccountInfoActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(AccountInfoActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteUserAccount(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<Void> call = Utils.getServerAPI(this).deleteUserById(user.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    if(user.getLoginFlag() == Utils.FACEBOOK_LOGIN)
                    {
                        revokeFacebookAccess();
                    }
                    else if(user.getLoginFlag() == Utils.GOOGLE_LOGIN)
                    {
                        revokeGoogleAccess();
                    }

                    user = null;
                    Utils.removeUserLoginStatus(AccountInfoActivity.this);

                    // Log out and start login activity without a way back
                    Intent intent = new Intent(AccountInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(AccountInfoActivity.this, getString(R.string.farewell_user_on_delete), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        deleteUserAccount(init_time);
                        Log.e("DEBUG", "Delete user response:" + response.code());
                    }
                    else
                    {
                        Toast.makeText(AccountInfoActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(AccountInfoActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkPermissionsBeforeExecution(int tag) {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, Utils.CAMERA_REQUEST_CODE);
        }
        else
        {
            if (tag == SELECT_FROM_PHOTO_ALBUM)
            {

            }
            else if(tag == TAKE_A_PHOTO)
            {
                captureProfilePhoto();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            DialogFragment cameraDialogFragment = new CameraPermissionRequiredDialogFragment(getApplicationContext().getPackageName(), Utils.ON_PERMISSION_DENIED_STAY);
            cameraDialogFragment.show(getSupportFragmentManager(), "Camera Permission");
        }
        else
        {
            if(requestCode == Utils.CAMERA_REQUEST_CODE) {
                captureProfilePhoto();
            }
            else if(requestCode == Utils.ALBUM_ACCESS_REQUEST_CODE) {

            }
        }
}

    private void captureProfilePhoto() {
        // SHOULD BE CALLED WHEN CAMERA PERMISSION IS GRANTED

        // Initialise the intent to use the camera App
        Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            selected_profile_image_file = getProfilePhotoFile(); // Get the file
        } catch (IOException e) {
            Toast.makeText(AccountInfoActivity.this, getString(R.string.capture_photo_io_error), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Get Uri for the file and put with the particular key to allow the camera app we are
        // delegating to, to be able to access the file and put the output there
        Uri fpUri = FileProvider.getUriForFile(this, getString(R.string.app_authority), selected_profile_image_file);
        capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, fpUri); // Specify extra output to the file

        if (capturePicIntent.resolveActivity(getPackageManager()) != null)  // Make sure there exists an app that is able to handle such
            startActivityForResult(capturePicIntent, Utils.USER_PROFILE_PIC_REQUEST); // Result will be passed to onActivityResult(...)
    }

    private File getProfilePhotoFile() throws IOException {
        // Use 'getExternalFilesDir' on Context to access package-specific directories
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(AccountInfoActivity.SELECTED_PROFILE_IMAGE_FILENAME, ".jpg", storageDirectory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.USER_PROFILE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            // Get the image from the file path we set up previously rather than the intent itself as a bitmap
            navigateToConfirmProfileImageActivity();
        }
    }

    private void navigateToConfirmProfileImageActivity()
    {
        Intent intent = new Intent(this, ConfirmProfileImageActivity.class);
        intent.putExtra(Utils.IMAGE_FILE_TRANSFER_TAG, new Gson().toJson(selected_profile_image_file));
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(user != null)
            Utils.updateUserLoginStatus(this, this.user); // Update the local sharedPreferences
    }
}