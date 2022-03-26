package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.AccountFragment;
import com.example.food_product_comparison_android_app.Fragments.HomeFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String HOME_FRAG_TAG = "home_fragment";
    public static final String ACCOUNT_FRAG_TAG = "account_fragment";
    private GoogleSignInClient mGoogleSignInClient;
    private String user_info;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private FloatingActionButton scan_fab;
    private static final int HOME_FRAG = 0;
    private static final int ACCOUNT_FRAG = 1;
    private int current_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // -----------------------------------------------
        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences("ScanHistory", 0);
        SharedPreferences.Editor editor = sp.edit();
        ArrayList<Product> products;
        ArrayList<String> dates;
        dates = new ArrayList<>();
        dates.add("16 Mar 2022, Wednesday");
        products = new ArrayList<>();
        products.add(new Product("14210247109512"));
        editor.putString("ScanHistoryProducts", gson.toJson(products));
        editor.putString("ScanHistoryDates", gson.toJson(dates));
        editor.apply();
        // -----------------------------------------------

        this.findViews();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        this.user_info = getIntent().getStringExtra(Utils.USER_INFO_KEY);

        this.initialiseFragments();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, this.homeFragment).commit();
            current_frag = HOME_FRAG;

        this.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home)
            {
                navigateTo(HOME_FRAG);
                return true;
            }
            else if (id == R.id.account)
            {
                navigateTo(ACCOUNT_FRAG);
                return true;
            }

            return false;
        });

        this.scan_fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
            }
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

    private void findViews()
    {
        this.bottomNavigationView = findViewById(R.id.main_bottom_nav);
        this.scan_fab = findViewById(R.id.scan_fab);
    }

    private void initialiseFragments()
    {
        Bundle bundle = new Bundle();
        bundle.putString(Utils.USER_INFO_KEY, user_info);

        this.homeFragment = new HomeFragment();
        this.accountFragment = new AccountFragment();

        homeFragment.setArguments(bundle);
        accountFragment.setArguments(bundle);
    }

    private void navigateTo(int frag)
    {
        if (current_frag != frag)
        {
            if (frag == HOME_FRAG)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, this.homeFragment).addToBackStack(HOME_FRAG_TAG).commit();
                current_frag = HOME_FRAG;
            }
            else if(frag == ACCOUNT_FRAG)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, this.accountFragment).addToBackStack(ACCOUNT_FRAG_TAG).commit();
                current_frag = ACCOUNT_FRAG;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        current_frag = current_frag == HOME_FRAG ? ACCOUNT_FRAG : HOME_FRAG;
        this.bottomNavigationView.setSelectedItemId(current_frag == HOME_FRAG ? R.id.home : R.id.account);
    }
}