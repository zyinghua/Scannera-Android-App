package com.example.food_product_comparison_android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeListRecyclerViewAdapter extends RecyclerView.Adapter<HomeListRecyclerViewAdapter.ViewHolder> {
    List<String> products;

    public HomeListRecyclerViewAdapter(List<String> products)
    {
        this.products = products;
    }

    public HomeListRecyclerViewAdapter()
    {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recom_product_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv.setText(holder.tv.getText() + " " + this.products.get(position));
    }

    @Override
    public int getItemCount() {
        if (products == null) //In case of the pcs arraylist is not initialised
            return 0;
        else
            return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(View view) {
            super(view);

            this.tv = view.findViewById(R.id.text);
        }
    }

    public void setProducts(List<String> products){
        this.products = products;
    }
}
