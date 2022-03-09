package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.food_product_comparison_android_app.Fragments.HomeFragment;
import com.example.food_product_comparison_android_app.Fragments.MeFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String HOME_FRAG_TAG = "home_fragment";
    public static final String ME_FRAG_TAG = "me_fragment";
    private GoogleSignInClient mGoogleSignInClient;
    private int login_option;
    private String user_info;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViews();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        this.user_info = getIntent().getStringExtra(LoginActivity.USER_INFO_KEY);
        this.login_option = getIntent().getIntExtra(LoginActivity.LOGIN_OPTION_KEY, -1);

        this.initialiseFragments();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, this.homeFragment).addToBackStack(HOME_FRAG_TAG).commit();

        this.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home)
            {
                if (bottomNavigationView.getSelectedItemId() != id)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, this.homeFragment).addToBackStack(HOME_FRAG_TAG).commit();

                return true;
            }
            else if (id == R.id.me)
            {
                if (bottomNavigationView.getSelectedItemId() != id)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, this.meFragment).addToBackStack(ME_FRAG_TAG).commit();

                return true;
            }

            return false;
        });
    }

    public void googleLogOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    public void facebookLogOut() {
        LoginManager.getInstance().logOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void google_revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void findViews()
    {
        this.bottomNavigationView = findViewById(R.id.main_bottom_nav);
    }

    private void initialiseFragments()
    {
        this.homeFragment = new HomeFragment();

        this.meFragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LoginActivity.LOGIN_OPTION_KEY, login_option);
        bundle.putString(LoginActivity.USER_INFO_KEY, user_info);
        meFragment.setArguments(bundle);
    }
}