package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Dialogs.EditDialog;
import com.example.food_product_comparison_android_app.Dialogs.LoadingDialog;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInfoActivity extends AppCompatActivity {
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

        // Get user information
        user = Utils.getLoggedUser(this);

        this.setUpToolbar();
        this.findViews();
        this.setUpListeners();
        this.populateWithUserData();
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

    @Override
    protected void onPause() {
        super.onPause();

        if(user != null)
            Utils.updateUserLoginStatus(this, this.user); // Update the local sharedPreferences
    }
}