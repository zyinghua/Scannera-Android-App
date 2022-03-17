package com.example.food_product_comparison_android_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.HomeListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.ScanHistoryListRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanHistoryListContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pure_recyclerview, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // -----------------------------------------------
        SharedPreferences sp = getActivity().getSharedPreferences("ScanHistory", 0);

        Gson gson = new Gson();
        String str_products = sp.getString("ScanHistoryProducts", "");
        String str_dates = sp.getString("ScanHistoryDates", "");

        Type ptype = new TypeToken<ArrayList<Product>>() {}.getType();
        Type dtype = new TypeToken<ArrayList<String>>() {}.getType();

        ArrayList<Product> products = gson.fromJson(str_products, ptype);
        ArrayList<String> dates = gson.fromJson(str_dates, dtype);
        ArrayList<Object> items = new ArrayList<>();
        // -----------------------------------------------

        for (int i = products.size() - 1; i >= 0; i--)
        {
            if (i == products.size() - 1 || dates.get(i).compareTo(dates.get(i + 1)) < 0)
            {
                items.add(dates.get(i));
            }

            items.add(products.get(i));
        }
        // -----------------------------------------------

        ScanHistoryListRecyclerViewAdapter shAdapter = new ScanHistoryListRecyclerViewAdapter(items);
        recyclerView.setAdapter(shAdapter);

        return view;
    }
}