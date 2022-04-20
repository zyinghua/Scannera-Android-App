package com.example.food_product_comparison_android_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.LoadingDialog;
import com.example.food_product_comparison_android_app.Product;
import com.example.food_product_comparison_android_app.ProductInformationActivity;
import com.example.food_product_comparison_android_app.ProductListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.StarredProductActivity;
import com.example.food_product_comparison_android_app.Utils;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AutoCompleteTextView sort_by_input;
    private SwitchCompat sort_desc_switch;
    private Product product;
    private ArrayList<Object> similar_products;

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

        this.setUpSortSelectionAdapter();
        this.setUpContent();

        this.sort_by_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(requireActivity(), "Selected: " + parent.getSelectedItem(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
            products.add(new Product("123", i + "", "Martin & Pleasance", "Rest & Quiet Calm Pastilles", 7.99, "Health Products", true));
        }

        ProductListRecyclerViewAdapter productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(requireActivity().getApplicationContext(),getActivity(), products);
        this.recyclerView.setAdapter(productListRecyclerViewAdapter);
    }

    private void setUpSortSelectionAdapter()
    {

        ArrayList<String> nutritionAttributesAl = new ArrayList<>(product.getNutritionAttributes().keySet());

        String[] nutritionAttributes = new String[nutritionAttributesAl.size()];
        nutritionAttributesAl.toArray(nutritionAttributes);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, nutritionAttributes);
        this.sort_by_input.setAdapter(arrayAdapter);
    }

    private void handleOnGetSimilarProducts(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(requireActivity());
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<ResponseBody> call = Utils.getServerAPI(requireActivity()).getSimilarProducts(
                Utils.getLoggedUser(requireActivity()).getId(), product.getProductId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    similar_products = Utils.parseProductsFromResponse(requireActivity(), response.body());
                    ProductListRecyclerViewAdapter productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(
                            requireActivity().getApplicationContext(),getActivity(), similar_products);
                    recyclerView.setAdapter(productListRecyclerViewAdapter);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        handleOnGetSimilarProducts(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), getString(R.string.similar_product_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(requireActivity(), getString(R.string.similar_product_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void performSorting(String factor)
    {
        // Quick sort (Dutch National Flag sort)
        Boolean descending = this.sort_desc_switch.isChecked();


    }
}