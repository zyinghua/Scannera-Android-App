package com.example.food_product_comparison_android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StarredProductListRecyclerViewAdapter extends RecyclerView.Adapter<StarredProductListRecyclerViewAdapter.ViewHolder> {
    List<Product> starred_products;

    public StarredProductListRecyclerViewAdapter(List<Product> starred_products) {
        this.starred_products = starred_products;
    }

    @NonNull
    @Override
    public StarredProductListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cardview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StarredProductListRecyclerViewAdapter.ViewHolder holder, int position) {
        Product product = starred_products.get(position);
        holder.tv.setText(product.getBarcode());
    }

    @Override
    public int getItemCount() {
        return starred_products == null ? 0 : starred_products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(View view) {
            super(view);
            this.tv = view.findViewById(R.id.text);
        }
    }

    public void setStarredProducts(List<Product> starred_products) {
        this.starred_products = starred_products;
    }
}
