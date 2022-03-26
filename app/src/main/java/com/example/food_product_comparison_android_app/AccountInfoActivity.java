package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.food_product_comparison_android_app.Fragments.CameraPermissionRequiredDialogFragment;
import com.example.food_product_comparison_android_app.Fragments.DeleteAccountConfirmDialogFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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

public class AccountInfoActivity extends AppCompatActivity {
    private static final int EDIT_USERNAME = 0;
    private static final int EDIT_PASSWORD = 1;
    private static final int EDIT_EMAIL_ADDRESS = 2;
    private static final int EDIT_FIRSTNAME = 3;
    private static final int EDIT_LASTNAME = 4;
    private User user;
    private Boolean isEdited;
    private ImageButton top_back_btn;
    private MaterialButton edit_username_btn;
    private TextView username_tv;
    private TextView password_tv;
    private TextView email_address_tv;
    private TextView firstname_tv;
    private TextView lastname_tv;
    private MaterialButton edit_password_btn;
    private MaterialButton edit_email_address_btn;
    private MaterialButton edit_firstname_btn;
    private MaterialButton edit_lastname_btn;
    private MaterialButton delete_acc_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        this.isEdited = false;
        this.findViews();
        this.setUpListeners();

        // Get user information
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        this.user = gson.fromJson(getIntent().getStringExtra(Utils.USER_INFO_KEY), type);

        // Populate user information to views
        this.username_tv.setText(this.user.getUsername());
        this.password_tv.setText(this.user.getPassword() == null ? "" : new String(new char[this.user.getPassword().length()]).replace("\0", "*"));
        this.email_address_tv.setText(this.user.getEmail());
        this.firstname_tv.setText(this.user.getFirstname());
        this.lastname_tv.setText(this.user.getLastname());
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
        this.username_tv = findViewById(R.id.username_tv);
        this.password_tv = findViewById(R.id.password_tv);
        this.email_address_tv = findViewById(R.id.email_address_tv);
        this.firstname_tv = findViewById(R.id.firstname_tv);
        this.lastname_tv = findViewById(R.id.lastname_tv);
        this.edit_username_btn = findViewById(R.id.edit_username_btn);
        this.edit_password_btn = findViewById(R.id.edit_password_btn);
        this.edit_email_address_btn = findViewById(R.id.edit_email_address_btn);
        this.edit_firstname_btn = findViewById(R.id.edit_firstname_btn);
        this.edit_lastname_btn = findViewById(R.id.edit_lastname_btn);
        this.delete_acc_btn = findViewById(R.id.delete_acc_btn);
    }

    private void setUpListeners()
    {
        this.top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        this.edit_email_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(email_address_tv, getString(R.string.email_address_label), EDIT_EMAIL_ADDRESS);
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
                    input.setText(edited_input_et.getText().toString());
                    isEdited = true;
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isEdited)
        {
            // Send edited user account information to the server here
            this.user.updateUserInfo(username_tv.getText().toString(), firstname_tv.getText().toString(), lastname_tv.getText().toString(), email_address_tv.getText().toString());

        }
    }

    public void deleteUserAccount()
    {
        google_revokeAccess();

        // Log out and start login activity without a way back
        finish();
        Intent intent = new Intent(AccountInfoActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void google_revokeAccess() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        GoogleSignIn.getClient(this, gso).revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                    }
                });
    }
}