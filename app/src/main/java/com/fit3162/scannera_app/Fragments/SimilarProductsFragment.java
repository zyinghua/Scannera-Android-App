package com.fit3162.scannera_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import com.fit3162.scannera_app.GeneralJavaClasses.Product;
import com.fit3162.scannera_app.ProductInformationActivity;
import com.fit3162.scannera_app.Adapters.ProductListRecyclerViewAdapter;
import com.fit3162.scannera_app.R;
import com.fit3162.scannera_app.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;

public class SimilarProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductListRecyclerViewAdapter productListRecyclerViewAdapter;
    private AutoCompleteTextView sort_by_input;
    private SwitchCompat sort_desc_switch;
    private Product product;
    private ArrayList<Object> similarProducts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        product = ((ProductInformationActivity) requireActivity()).getProduct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_similar_products, container, false);

        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.sort_by_input = view.findViewById(R.id.sort_by_autoCompleteTv);
        this.sort_desc_switch = view.findViewById(R.id.sort_desc_switch);

        this.setUpContent();

        this.sort_by_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                performSimilarProductsSorting(parent.getItemAtPosition(position) + "");
            }
        });

        this.sort_desc_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!sort_by_input.getText().toString().isEmpty())
                {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        Collections.reverse(similarProducts);

                        new Handler(Looper.getMainLooper()).post(() -> {
                            productListRecyclerViewAdapter.notifyDataSetChanged();
                        });
                    });
                }
            }
        });

        return view;
    }

    private void setUpContent()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(layoutManager);

        this.setUpSortSelectionAdapter();

        this.similarProducts = ((ProductInformationActivity) requireActivity()).getSimilarProducts();
        productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(
                requireActivity().getApplicationContext(),getActivity(), similarProducts);
        recyclerView.setAdapter(productListRecyclerViewAdapter);
    }

    private void setUpSortSelectionAdapter()
    {
        ArrayList<String> productFactorsAl = new ArrayList<>(product.getNutritionAttributes().keySet());
        productFactorsAl.add(0, Utils.PRODUCT_PRICE);

        String[] productFactors = new String[productFactorsAl.size()];
        productFactorsAl.toArray(productFactors);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productFactors);
        this.sort_by_input.setAdapter(arrayAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void performSimilarProductsSorting(String factor)
    {
        if (similarProducts != null)
        {
            Executors.newSingleThreadExecutor().execute(() -> {
                Comparator<Object> comparator = (o1, o2) ->
                        Float.compare(((Product) o1).getSpecificProductValue(factor), ((Product) o2).getSpecificProductValue(factor));

                if (this.sort_desc_switch.isChecked())
                    comparator = comparator.reversed();

                similarProducts.sort(comparator);

                new Handler(Looper.getMainLooper()).post(() -> {
                    productListRecyclerViewAdapter.setFactorOfComparison(factor);
                    productListRecyclerViewAdapter.notifyDataSetChanged();
                });
            });
        }
    }
}