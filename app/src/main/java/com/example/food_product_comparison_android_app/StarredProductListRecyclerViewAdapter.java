package com.example.food_product_comparison_android_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StarredProductListRecyclerViewAdapter extends RecyclerView.Adapter<StarredProductListRecyclerViewAdapter.ViewHolder> {
    Context appContext;
    List<Product> starred_products;

    public StarredProductListRecyclerViewAdapter(Context context, List<Product> starred_products) {
        this.appContext = context;
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

        holder.product_look.setImageDrawable(appContext.getDrawable(R.drawable.monash_uni_img));
        holder.nameTv.setText(product.getName());
        holder.brandTv.setText(product.getBrand());
        holder.priceTv.setText("$ " + product.getPrice());
        holder.star_btn.setImageDrawable(ContextCompat.getDrawable(appContext,
                product.getIs_starred() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off));
        holder.star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getIs_starred())
                {
                    holder.star_btn.setImageDrawable(ContextCompat.getDrawable(appContext, android.R.drawable.btn_star_big_off));
                } else {
                    holder.star_btn.setImageDrawable(ContextCompat.getDrawable(appContext, android.R.drawable.btn_star_big_on));
                }
                product.setIs_starred(!product.getIs_starred());
            }
        });
    }

    @Override
    public int getItemCount() {
        return starred_products == null ? 0 : starred_products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_look;
        private TextView nameTv;
        private TextView brandTv;
        private TextView priceTv;
        private ImageButton star_btn;

        public ViewHolder(View view) {
            super(view);

            this.product_look = view.findViewById(R.id.cardview_product_look_pic);
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
