package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    private TextView forgotten_password_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_fragment, new LoginFragment()).addToBackStack("login_frag").commit();

        this.findInstantiatedViews();
        this.setupDefaultListeners();
    }

    private void findInstantiatedViews()
    {
        this.forgotten_password_tv = findViewById(R.id.forgotten_password_tv);
    }

    private void setupDefaultListeners()
    {
        this.forgotten_password_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "forgotten password textview clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}