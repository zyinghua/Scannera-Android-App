package com.example.scannera_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scannera_app.GeneralJavaClasses.ProductReview;
import com.example.scannera_app.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductReviewListRecyclerViewAdapter extends RecyclerView.Adapter<ProductReviewListRecyclerViewAdapter.ViewHolder>{
    private ArrayList<ProductReview> reviews;

    public ProductReviewListRecyclerViewAdapter() {
    }

    public ProductReviewListRecyclerViewAdapter(ArrayList<ProductReview> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductReview review = reviews.get(position);
        if (review.getUserPImgUrl() != null && !review.getUserPImgUrl().isEmpty())
            Picasso.get().load(review.getUserPImgUrl()).into(holder.user_profile_img);

        holder.username_tv.setText(review.getUsername());
        holder.rating_bar.setRating(review.getRating());
        holder.review_date_tv.setText(review.getDate());
        holder.review_desc_tv.setText(review.getDescription());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size() ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircularImageView user_profile_img;
        private final TextView username_tv;
        private final RatingBar rating_bar;
        private final TextView review_date_tv;
        private final TextView review_desc_tv;

        public ViewHolder(@NonNull View view) {
            super(view);

            this.username_tv = view.findViewById(R.id.review_username_tv);
            this.user_profile_img = view.findViewById(R.id.user_profile_img);
            this.rating_bar = view.findViewById(R.id.review_rating);
            this.review_date_tv = view.findViewById(R.id.review_date_tv);
            this.review_desc_tv = view.findViewById(R.id.review_desc_tv);
        }
    }

    public void setReviews(ArrayList<ProductReview> reviews)
    {
        this.reviews = reviews;
    }
}
