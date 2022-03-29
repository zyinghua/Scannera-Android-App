package com.example.food_product_comparison_android_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeListRecyclerViewAdapter extends RecyclerView.Adapter<HomeListRecyclerViewAdapter.ViewHolder> {
    Context appContext;
    List<Product> products;

    public HomeListRecyclerViewAdapter(Context context, List<Product> products)
    {
        appContext = context;
        this.products = products;
    }

    public HomeListRecyclerViewAdapter(Context context)
    {
        appContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTv.setText("Product No. " + this.products.get(position).getBarcode());
    }

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        else
            return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView product_cv;
        private TextView nameTv;
        private TextView brandTv;
        private TextView priceTv;
        private ImageButton star_btn;

        public ViewHolder(View view) {
            super(view);

            this.product_cv = view.findViewById(R.id.product_card_view);
            this.nameTv = view.findViewById(R.id.cardview_product_name);
            this.brandTv = view.findViewById(R.id.cardview_product_brand);
            this.priceTv = view.findViewById(R.id.cardview_product_price);
            this.star_btn = view.findViewById(R.id.star_btn);
        }
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }
}
