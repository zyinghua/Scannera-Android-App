package com.example.food_product_comparison_android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        holder.nameTv.setText(product.getBarcode());
    }

    @Override
    public int getItemCount() {
        return starred_products == null ? 0 : starred_products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private TextView brandTv;
        private TextView priceTv;
        private ImageButton star_btn;

        public ViewHolder(View view) {
            super(view);

            this.nameTv = view.findViewById(R.id.cardview_product_name);
            this.brandTv = view.findViewById(R.id.cardview_product_brand);
            this.priceTv = view.findViewById(R.id.cardview_product_price);
            this.star_btn = view.findViewById(R.id.star_btn);
        }
    }

    public void setStarredProducts(List<Product> starred_products) {
        this.starred_products = starred_products;
    }
}
