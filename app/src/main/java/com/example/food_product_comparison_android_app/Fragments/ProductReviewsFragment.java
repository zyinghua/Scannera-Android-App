package com.example.food_product_comparison_android_app.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.ProductInformationActivity;
import com.example.food_product_comparison_android_app.ProductReview;
import com.example.food_product_comparison_android_app.ProductReviewListRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.User;
import com.example.food_product_comparison_android_app.Utils;
import com.google.android.material.button.MaterialButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductReviewsFragment extends Fragment {
    private String product_id;
    private MaterialButton write_review_btn;
    private TextView total_num_of_reviews_tv;
    private CircularImageView user_profile_img;
    private RecyclerView recyclerView;
    private User user;
    private ArrayList<ProductReview> reviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            this.product_id = getArguments().getString(ProductInformationActivity.PRODUCT_ID_TRANSFER_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_reviews, container, false);
        this.findViews(v);
        this.setUpContent();

        return v;
    }

    private void findViews(View v)
    {
        this.write_review_btn = v.findViewById(R.id.write_a_review_btn);
        this.total_num_of_reviews_tv = v.findViewById(R.id.total_num_of_review_tv);
        this.user_profile_img = v.findViewById(R.id.user_profile_img);
        this.recyclerView = v.findViewById(R.id.recyclerView);
    }

    private void setUpContent()
    {
        this.user = Utils.getLoggedUser(requireActivity());
        if (user.getProfile_img_url() != null && !user.getProfile_img_url().equals("null"))
            Picasso.get().load(user.getProfile_img_url()).into(user_profile_img);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(layoutManager);

        reviews = new ArrayList<>();
        this.getProductReviews(System.currentTimeMillis());

        this.write_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(requireActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_product_review_writing);
                dialog.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RatingBar rating_bar = dialog.findViewById(R.id.product_review_rating);
                        EditText review_desc_et = dialog.findViewById(R.id.review_desc_et);

                        float rating = rating_bar.getRating();
                        String review_desc = review_desc_et.getText().toString();

                        if (rating == 0.0)
                        {
                            Animation shake = AnimationUtils.loadAnimation(dialog.getContext(), R.anim.shake);
                            rating_bar.startAnimation(shake);

                            Toast.makeText(dialog.getContext(), getString(R.string.error_input_cannot_be_empty), Toast.LENGTH_LONG).show();
                        }
                        else if (review_desc.isEmpty())
                        {
                            Animation shake = AnimationUtils.loadAnimation(dialog.getContext(), R.anim.shake);
                            review_desc_et.startAnimation(shake);

                            Toast.makeText(dialog.getContext(), getString(R.string.error_input_cannot_be_empty), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            postProductReview(System.currentTimeMillis(), dialog);
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    private void getProductReviews(Long init_time)
    {
        // Send a GET request here to the server to get all the reviews of the current product
        for (int i = 0; i < 10; i++)
        {
            reviews.add(new ProductReview(user.getUsername(), user.getProfile_img_url(), "18/04/2022", 4.2f, "This is a very tasty food!"));
        }

        ProductReviewListRecyclerViewAdapter reviewAdapter = new ProductReviewListRecyclerViewAdapter(reviews);
        this.recyclerView.setAdapter(reviewAdapter);
        this.total_num_of_reviews_tv.setText(String.format(getString(R.string.total_num_of_reviews), reviews.size()));
    }

    private void postProductReview(Long init_time, Dialog dialog)
    {
        // Send a POST request to the server
        dialog.dismiss();
        Toast.makeText(requireActivity(), getString(R.string.review_successful_submit), Toast.LENGTH_LONG).show();
    }
}