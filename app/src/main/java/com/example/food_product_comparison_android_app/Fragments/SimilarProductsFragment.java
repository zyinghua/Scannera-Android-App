package com.example.food_product_comparison_android_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimilarProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pure_recyclerview, container, false);
        this.recyclerView = view.findViewById(R.id.recyclerView);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            this.setUpContent();
            uiHandler.post(() -> {

            });
        });

        return view;
    }

    private void setUpContent()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(layoutManager);

        ArrayList<Object> products = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            products.add(new Product(i + "", "Martin & Pleasance", "Rest & Quiet Calm Pastilles", 7.99f, "Health Products", true));
        }

        ProductListRecyclerViewAdapter productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(requireActivity().getApplicationContext(),getActivity(), products);
        this.recyclerView.setAdapter(productListRecyclerViewAdapter);
    }
}