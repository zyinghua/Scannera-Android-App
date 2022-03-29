package com.example.food_product_comparison_android_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScanHistoryListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TITLE_VIEW_TYPE = 0;
    public static final int ITEM_VIEW_TYPE = 1;
    Context appContext;
    List<Object> items;

    public ScanHistoryListRecyclerViewAdapter(Context context, List<Object> items) {
        this.appContext = context;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_history_date_title, parent, false);
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
            itemViewHolder.nameTv.setText(product.getBarcode());
            itemViewHolder.star_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // itemViewHolder.star_btn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public TitleViewHolder(View view) {
            super(view);

            this.tv = view.findViewById(R.id.date_title_tv);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView product_cv;
        private TextView nameTv;
        private TextView brandTv;
        private TextView priceTv;
        private ImageButton star_btn;

        public ItemViewHolder(View view) {
            super(view);

            this.product_cv = view.findViewById(R.id.product_card_view);
            this.nameTv = view.findViewById(R.id.cardview_product_name);
            this.brandTv = view.findViewById(R.id.cardview_product_brand);
            this.priceTv = view.findViewById(R.id.cardview_product_price);
            this.star_btn = view.findViewById(R.id.star_btn);
        }
    }

    public void setProducts(List<Object> items){
        this.items = items;
    }
}
