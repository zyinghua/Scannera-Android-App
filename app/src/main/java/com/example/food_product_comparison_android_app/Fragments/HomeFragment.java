package com.example.food_product_comparison_android_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Dialogs.LoadingDialog;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.Product;
import com.example.food_product_comparison_android_app.Adapters.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.User;
import com.example.food_product_comparison_android_app.Utils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private User user;
    private TextView welcome_username_tv;
    private CircularImageView home_user_img;
    private RecyclerView homeRecyclerView;
    // private boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.findViews(view);
        this.setUpToolbar(view);
        this.setUpContent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        user = Utils.getLoggedUser(requireActivity());
        this.loadUserProfile();
    }

    private void findViews(View view)
    {
        this.welcome_username_tv = view.findViewById(R.id.welcome_username_tv);
        this.home_user_img = view.findViewById(R.id.home_user_profile_img);
        this.homeRecyclerView = view.findViewById(R.id.home_recyclerView);
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.home_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadUserProfile() {
        if (user.getProfile_img_url() != null && !user.getProfile_img_url().equals("null") && !user.getProfile_img_url().isEmpty())
            Picasso.get().load(user.getProfile_img_url()).into(home_user_img);

        welcome_username_tv.setText(String.format(getString(R.string.home_greeting), user.getUsername()));
    }

    private void setUpContent()
    {
        // isLoading = false;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(layoutManager);

        handleOnGetRecommendedProducts(System.currentTimeMillis());

//        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if(!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == products.size() - 1)
//                {
//                    loadASetOfProducts();
//                    isLoading = true;
//                }
//            }
//        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void handleOnGetRecommendedProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(requireActivity());
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<ResponseBody> call = Utils.getServerAPI(requireActivity()).getRecommendedProducts(Utils.getLoggedUser(requireActivity()).getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    ArrayList<Object> recommended_products = Utils.parseProductsFromResponse(requireActivity(), response.body());

                    if (recommended_products.size() == 0)
                        ((TextView)requireActivity().findViewById(R.id.recommendation_title)).
                                setText(requireActivity().getString(R.string.home_no_recommendation_title));

                    ProductListRecyclerViewAdapter rpAdapter = new ProductListRecyclerViewAdapter(
                            requireActivity().getApplicationContext(), getActivity(), recommended_products);
                    homeRecyclerView.setAdapter(rpAdapter);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        handleOnGetRecommendedProducts(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(requireActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void loadASetOfProducts()
//    {
//        new Handler(Looper.getMainLooper()).post(() -> {
//            products.add(Utils.LOADING_BAR_TAG);
//            productListRecyclerViewAdapter.notifyItemInserted(products.size() - 1);
//        });
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            products.remove(products.size() - 1);
//            productListRecyclerViewAdapter.notifyItemRemoved(products.size() - 1);
//
//            for(int i = 0; i < 5; i++)
//            {
//                products.add(new Product("54", i + "", "Martin & Pleasance", "Rest & Quiet Calm Pastilles", 7.99f, "Health Products", true));
//                productListRecyclerViewAdapter.notifyItemInserted(products.size() - 1);
//            }
//
//            isLoading = false;
//        }, 1000);
//
//
//        //productListRecyclerViewAdapter.notifyDataSetChanged();
//    }
}