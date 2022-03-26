package com.example.food_product_comparison_android_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.ScanHistoryListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.StarredProductListRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class StarredProductListContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pure_recyclerview, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Product> starred_products = new ArrayList<>();

        //******************************************************
        starred_products.add(new Product("12431435235", "abcasgagahgewheasrhsadrhsedrthsedr", "dd", 55.5f, false));
        starred_products.add(new Product("12342342334","Monash", "Cokecola chocolate milk balala", 66.6f, true));
        //******************************************************

        StarredProductListRecyclerViewAdapter spAdapter = new StarredProductListRecyclerViewAdapter(requireActivity().getApplicationContext(), starred_products);
        recyclerView.setAdapter(spAdapter);

        return view;
    }
}