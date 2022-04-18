package com.example.food_product_comparison_android_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TITLE_VIEW_TYPE = 0;
    public static final int ITEM_VIEW_TYPE = 1;
    public static final int LOADING_BAR_VIEW_TYPE = 2;
    private final Context appContext;
    private final Activity activityContext;
    private List<Object> items;
    private final User user;

    public ProductListRecyclerViewAdapter(Context appContext, Activity activityContext) {
        this.appContext = appContext;
        this.activityContext = activityContext;
        this.user = Utils.getLoggedUser(activityContext);
    }

    public ProductListRecyclerViewAdapter(Context context, Activity activityContext, List<Object> items) {
        this.appContext = context;
        this.activityContext = activityContext;
        this.items = items;
        this.user = Utils.getLoggedUser(activityContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String)
        {
            return items.get(position).equals(Utils.LOADING_BAR_TAG)? LOADING_BAR_VIEW_TYPE : TITLE_VIEW_TYPE;
        }
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
        else if (viewType == ITEM_VIEW_TYPE) // actual product item
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product, parent, false);
            return new ProductViewHolder(v);
        }
        else
        {
            // LOADING_BAR_VIEW_TYPE
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_bar, parent, false);
            return new LoadingBarViewHolder(v);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TITLE_VIEW_TYPE)
        {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.tv.setText(items.get(position).toString());
        }
        else if(holder.getItemViewType() == ITEM_VIEW_TYPE)
        {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = (Product) items.get(position);

            try{
                productViewHolder.product_look.setImageDrawable(appContext.getDrawable(R.drawable.product_sample));
                productViewHolder.nameTv.setText(product.getName());
                productViewHolder.brandTv.setText(product.getBrand());
                productViewHolder.priceTv.setText("$ " + product.getPrice());
                productViewHolder.star_btn.setImageDrawable(ContextCompat.getDrawable(appContext,
                        product.getStarred() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off));

                productViewHolder.star_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.toggleProductStar(System.currentTimeMillis(), activityContext, appContext, productViewHolder.star_btn, product);
                    }
                });

                productViewHolder.product_cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.navigateToProductInfoActivity(activityContext, product);
                    }
                });
            }
            catch (NullPointerException e) {
                Toast.makeText(activityContext, "NullPointerException: There exists product(s) not fully initialised.", Toast.LENGTH_LONG).show();
                activityContext.onBackPressed();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public TitleViewHolder(View view) {
            super(view);

            this.tv = view.findViewById(R.id.date_title_tv);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final CardView product_cv;
        private final ImageView product_look;
        private final TextView nameTv;
        private final TextView brandTv;
        private final TextView priceTv;
        private final ImageButton star_btn;

        public ProductViewHolder(View view) {
            super(view);

            this.product_cv = view.findViewById(R.id.product_card_view);
            this.product_look = view.findViewById(R.id.cardview_product_look_pic);
            this.nameTv = view.findViewById(R.id.cardview_product_name);
            this.brandTv = view.findViewById(R.id.cardview_product_brand);
            this.priceTv = view.findViewById(R.id.cardview_product_price);
            this.star_btn = view.findViewById(R.id.star_btn);
        }
    }

    public static class LoadingBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingBarViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar =itemView.findViewById(R.id.loading_bar);
        }
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
