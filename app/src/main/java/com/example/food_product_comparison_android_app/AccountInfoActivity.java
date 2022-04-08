package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.CameraPermissionRequiredDialogFragment;
import com.example.food_product_comparison_android_app.Fragments.DeleteAccountConfirmDialogFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInfoActivity extends AppCompatActivity {
    private static final int EDIT_USERNAME = 0;
    private static final int EDIT_PASSWORD = 1;
    private static final int EDIT_FIRSTNAME = 2;
    private static final int EDIT_LASTNAME = 3;
    private User user;
    private MaterialButton edit_username_btn;
    private TextView username_tv;
    private TextView password_tv;
    private TextView email_address_tv;
    private TextView firstname_tv;
    private TextView lastname_tv;
    private MaterialButton edit_password_btn;
    private MaterialButton edit_firstname_btn;
    private MaterialButton edit_lastname_btn;
    private MaterialButton delete_acc_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        this.setUpToolbar();
        this.findViews();
        this.setUpListeners();

        // Get user information
        user = Utils.getLoggedUser(this);

        // Populate user information to views
        this.username_tv.setText(this.user.getUsername());
        this.password_tv.setText(this.user.getPassword() == null ? "" : new String(new char[this.user.getPassword().length()]).replace("\0", "*"));
        this.email_address_tv.setText(this.user.getEmail());
        this.firstname_tv.setText(this.user.getFirstname());
        this.lastname_tv.setText(this.user.getLastname());
    }

    private void findViews()
    {
        this.username_tv = findViewById(R.id.username_tv);
        this.password_tv = findViewById(R.id.password_tv);
        this.email_address_tv = findViewById(R.id.email_address_tv);
        this.firstname_tv = findViewById(R.id.firstname_tv);
        this.lastname_tv = findViewById(R.id.lastname_tv);
        this.edit_username_btn = findViewById(R.id.edit_username_btn);
        this.edit_password_btn = findViewById(R.id.edit_password_btn);
        this.edit_firstname_btn = findViewById(R.id.edit_firstname_btn);
        this.edit_lastname_btn = findViewById(R.id.edit_lastname_btn);
        this.delete_acc_btn = findViewById(R.id.delete_acc_btn);
    }

    private void setUpListeners()
    {
        this.edit_username_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(username_tv, getString(R.string.username_label), EDIT_USERNAME);
            }
        });

        this.edit_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(password_tv, getString(R.string.password_label), EDIT_PASSWORD);
            }
        });

        this.edit_firstname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(firstname_tv, getString(R.string.firstname_label), EDIT_FIRSTNAME);
            }
        });

        this.edit_lastname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(lastname_tv, getString(R.string.lastname_label), EDIT_LASTNAME);
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

    private void showEditDialog(TextView input, String title, int field)
    {
        final EditDialog dialog = new EditDialog(this, title);

        switch (field)
        {
            case EDIT_USERNAME:
                dialog.setMaxInputLength(Utils.MAX_LEN_USERNAME);
                break;
            case EDIT_PASSWORD:
                dialog.setMaxInputLength(Utils.MAX_LEN_PASSWORD);
                break;
            case EDIT_FIRSTNAME:
                dialog.setMaxInputLength(Utils.MAX_LEN_FIRSTNAME);
                break;
            case EDIT_LASTNAME:
                dialog.setMaxInputLength(Utils.MAX_LEN_LASTNAME);
                break;
            default:
                break;
        }

        dialog.setConfirmBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText edited_input_et = dialog.findViewById(R.id.edit_et);

                if (Objects.requireNonNull(edited_input_et.getText()).toString().isEmpty()) {
                    TextInputLayout edit_input_layout = dialog.findViewById(R.id.edit_text_input_layout);
                    edit_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
                }
                else
                {
                    // Send an update request to the server here...


                    input.setText(edited_input_et.getText().toString());
                    dialog.dismiss();
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

    public void deleteUserAccount(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        loading_dialog.show();

        Call<Void> call = Utils.getServerAPI(this).deleteUserById(user.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    Utils.removeUserLoginStatus(AccountInfoActivity.this);

                    if(user.getLoginFlag() == Utils.FACEBOOK_LOGIN)
                    {
                        revokeFacebookAccess();
                    }
                    else if(user.getLoginFlag() == Utils.GOOGLE_LOGIN)
                    {
                        revokeGoogleAccess();
                    }

                    // Log out and start login activity without a way back
                    Intent intent = new Intent(AccountInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
}