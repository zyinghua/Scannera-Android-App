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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.ProductInformationActivity;
import com.example.food_product_comparison_android_app.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimilarProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AutoCompleteTextView sort_by_input;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_similar_products, container, false);

        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.sort_by_input = view.findViewById(R.id.sort_by_autoCompleteTv);

        this.setUpSortSelectionAdapter();

        this.setUpContent();

        return view;
    }

    private void setUpContent()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(layoutManager);

        ArrayList<Object> products = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            products.add(new Product("123", i + "", "Martin & Pleasance", "Rest & Quiet Calm Pastilles", 7.99, "Health Products", true));
        }

        ProductListRecyclerViewAdapter productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(requireActivity().getApplicationContext(),getActivity(), products);
        this.recyclerView.setAdapter(productListRecyclerViewAdapter);
    }

    private void setUpSortSelectionAdapter()
    {
        ArrayList<String> nutritionAttributesAl = new ArrayList<>();
        Product product = ((ProductInformationActivity) requireActivity()).getProduct();

        for(int i = 0; i < product.getNutritionAttributes().size(); i++)
        {
            nutritionAttributesAl.add(product.getNutritionAttributes().get(i).getAttribute_name());
        }

        String[] nutritionAttributes = new String[nutritionAttributesAl.size()];
        nutritionAttributesAl.toArray(nutritionAttributes);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, nutritionAttributes);
        this.sort_by_input.setAdapter(arrayAdapter);
    }
}