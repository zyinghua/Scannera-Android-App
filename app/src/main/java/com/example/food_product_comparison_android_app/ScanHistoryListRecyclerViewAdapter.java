package com.example.food_product_comparison_android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScanHistoryListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TITLE_VIEW_TYPE = 0;
    public static final int ITEM_VIEW_TYPE = 1;
    List<Object> items;

    public ScanHistoryListRecyclerViewAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String)
            return TITLE_VIEW_TYPE;
        else
            return ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TITLE_VIEW_TYPE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new TitleViewHolder(v);
        }
        else // actual product item
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cardview, parent, false);
            return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TITLE_VIEW_TYPE) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.tv.setText(items.get(position).toString());
        }
        else
        {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Product product = (Product) items.get(position);
            itemViewHolder.tv.setText(product.getBarcode());
        }
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public TitleViewHolder(View view) {
            super(view);

            this.tv = view.findViewById(android.R.id.text1);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ItemViewHolder(View view) {
            super(view);

            this.tv = view.findViewById(R.id.text);
        }
    }

    public void setProducts(List<Object> items){
        this.items = items;
    }
}
