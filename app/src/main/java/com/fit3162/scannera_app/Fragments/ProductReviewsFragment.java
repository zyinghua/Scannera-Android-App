package com.fit3162.scannera_app.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
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

import com.fit3162.scannera_app.Dialogs.LoadingDialog;
import com.fit3162.scannera_app.ProductInformationActivity;
import com.fit3162.scannera_app.GeneralJavaClasses.ProductReview;
import com.fit3162.scannera_app.Adapters.ProductReviewListRecyclerViewAdapter;
import com.fit3162.scannera_app.R;
import com.fit3162.scannera_app.ServerRetrofitAPI;
import com.fit3162.scannera_app.GeneralJavaClasses.User;
import com.fit3162.scannera_app.Utils;
import com.google.android.material.button.MaterialButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductReviewsFragment extends Fragment {
    private MaterialButton write_review_btn;
    private TextView total_num_of_reviews_tv;
    private CircularImageView user_profile_img;
    private RecyclerView recyclerView;
    private User user;
    private ArrayList<ProductReview> reviews;

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
        if (user.getProfile_img_url() != null && !user.getProfile_img_url().equals("null") && !user.getProfile_img_url().isEmpty())
            Picasso.get().load(user.getProfile_img_url()).into(user_profile_img);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(layoutManager);

        reviews = new ArrayList<>();
        this.getProductReviews(System.currentTimeMillis());

        ProductReviewListRecyclerViewAdapter reviewAdapter = new ProductReviewListRecyclerViewAdapter(reviews);
        this.recyclerView.setAdapter(reviewAdapter);
        setTotal_num_of_reviews_tv();

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
                            postProductReview(System.currentTimeMillis(), rating, review_desc, dialog);
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
        LoadingDialog loading_dialog = new LoadingDialog(requireActivity());
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<ResponseBody> call = Utils.getServerAPI(requireActivity()).getProductReviews(
                ((ProductInformationActivity) requireActivity()).getProduct().getProductId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    parseProductReviews(response.body());

                    ProductReviewListRecyclerViewAdapter reviewAdapter = new ProductReviewListRecyclerViewAdapter(reviews);
                    recyclerView.setAdapter(reviewAdapter);
                    total_num_of_reviews_tv.setText(String.format(getString(R.string.total_num_of_reviews), reviews.size()));
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        getProductReviews(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), getString(R.string.get_product_review_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(requireActivity(), getString(R.string.get_product_review_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseProductReviews(ResponseBody responseBody)
    {
        try {
            InputStream responseBodyIS = responseBody.byteStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBodyIS, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            String keyName;

            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                // Parse a single product review
                ProductReview productReview = new ProductReview();

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    keyName = jsonReader.nextName();

                    switch (keyName) {
                        case ServerRetrofitAPI.USER_USERNAME_SERVER:
                            productReview.setUsername(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.USER_PIMG_URL_SERVER:
                            if(jsonReader.peek() != JsonToken.NULL)
                                productReview.setUserPImgUrl(jsonReader.nextString());
                            else
                                jsonReader.skipValue();

                            break;
                        case ServerRetrofitAPI.PRODUCT_REVIEW_RATING_SERVER:
                            productReview.setRating(Float.parseFloat(jsonReader.nextString()));
                            break;
                        case ServerRetrofitAPI.PRODUCT_REVIEW_DESC_SERVER:
                            productReview.setDescription(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.PRODUCT_REVIEW_DATE_SERVER:
                            Date date = ServerRetrofitAPI.DATE_FORMAT_SERVER.parse(jsonReader.nextString());

                            if (date != null)
                                productReview.setDate(Utils.PRODUCT_REVIEW_DATE_FORMAT_DISPLAYED.format(date));
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }

                reviews.add(productReview);
                jsonReader.endObject();
            }

            jsonReader.endArray();
            responseBody.close();
        } catch (IOException | ParseException e) {
            Toast.makeText(requireActivity(), getString(R.string.general_error), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void postProductReview(Long init_time, float rating, String review_desc, Dialog dialog)
    {
        // Send a POST request to the server
        LoadingDialog loading_dialog = new LoadingDialog(requireActivity());
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<Void> call = Utils.getServerAPI(requireActivity()).addProductReview(
                Utils.getLoggedUser(requireActivity()).getId(),
                ((ProductInformationActivity) requireActivity()).getProduct().getProductId(),
                rating,
                review_desc
                );

        call.enqueue(new Callback<Void>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful())
                {
                    dialog.dismiss();
                    Toast.makeText(requireActivity(), String.format(getString(R.string.review_successful_submit), Utils.PRODUCT_REVIEW_CONTRIBUTION_POINTS), Toast.LENGTH_LONG).show();
                    user.setContributionScore(user.getContributionScore() + Utils.PRODUCT_REVIEW_CONTRIBUTION_POINTS);
                    Utils.updateUserLoginStatus(requireActivity(), user);

                    reviews.add(0, new ProductReview(user.getUsername(), user.getProfile_img_url(),
                            Utils.PRODUCT_REVIEW_DATE_FORMAT_DISPLAYED.format(new Date()), rating, review_desc));

                    if (recyclerView.getAdapter() != null)
                        recyclerView.getAdapter().notifyDataSetChanged();

                    setTotal_num_of_reviews_tv();
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        postProductReview(init_time, rating, review_desc, dialog);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), getString(R.string.post_product_review_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(requireActivity(), getString(R.string.post_product_review_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setTotal_num_of_reviews_tv()
    {
        this.total_num_of_reviews_tv.setText(String.format(getString(R.string.total_num_of_reviews), reviews.size()));
    }
}