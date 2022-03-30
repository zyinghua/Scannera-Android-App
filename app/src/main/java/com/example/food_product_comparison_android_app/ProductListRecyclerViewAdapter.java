package com.example.food_product_comparison_android_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ViewHolder> {
    Context appContext;
    List<Product> products;

    public ProductListRecyclerViewAdapter(Context appContext) {
        this.appContext = appContext;
    }

    public ProductListRecyclerViewAdapter(Context context, List<Product> products) {
        this.appContext = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cardview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListRecyclerViewAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);

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
                    // Un-star the product
                    holder.star_btn.setImageDrawable(ContextCompat.getDrawable(appContext, android.R.drawable.btn_star_big_off));
                } else {
                    // Star the product
                    holder.star_btn.setImageDrawable(ContextCompat.getDrawable(appContext, android.R.drawable.btn_star_big_on));
                }
                product.setIs_starred(!product.getIs_starred());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView product_cv;
        private final ImageView product_look;
        private final TextView nameTv;
        private final TextView brandTv;
        private final TextView priceTv;
        private final ImageButton star_btn;

        public ViewHolder(View view) {
            super(view);

            this.product_cv = view.findViewById(R.id.product_card_view);
            this.product_look = view.findViewById(R.id.cardview_product_look_pic);
            this.nameTv = view.findViewById(R.id.cardview_product_name);
            this.brandTv = view.findViewById(R.id.cardview_product_brand);
            this.priceTv = view.findViewById(R.id.cardview_product_price);
            this.star_btn = view.findViewById(R.id.star_btn);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
