package com.example.food_product_comparison_android_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_product_comparison_android_app.HomeListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.ScanHistoryListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScanHistoryListContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pure_recyclerview, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Object> items = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            if (i % 2 == 0)
                items.add(new Product(i + "| Product"));
            else
                items.add(i + "");
        }

        items.add(new Product(10 + "| Product"));
        items.add(new Product(11 + "| Product"));

        ScanHistoryListRecyclerViewAdapter shAdapter = new ScanHistoryListRecyclerViewAdapter(items);
        recyclerView.setAdapter(shAdapter);

        return view;
    }
}