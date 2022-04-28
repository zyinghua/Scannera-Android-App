package com.example.food_product_comparison_android_app.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_product_comparison_android_app.GeneralJavaClasses.User;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Objects;


public class ConfirmProfileImgFragment extends Fragment {
    private CircularImageView user_profile_img;
    private MaterialButton confirm_btn;
    private MaterialButton cancel_btn;
    private File selected_image_file;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<File>() {}.getType();
            selected_image_file = gson.fromJson(getArguments().getString(Utils.IMAGE_FILE_TRANSFER_TAG), type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_profile_img, container, false);

        this.findViews(v);
        this.setUpContent();

        return v;
    }

    private void findViews(View v)
    {
        this.user_profile_img = v.findViewById(R.id.user_profile_img);
        this.confirm_btn = v.findViewById(R.id.confirm_btn);
        this.cancel_btn = v.findViewById(R.id.cancel_btn);
    }

    private void setUpContent()
    {
        Bitmap imgBitmap = BitmapFactory.decodeFile(selected_image_file.getAbsolutePath());
        this.user_profile_img.setImageBitmap(imgBitmap);

        this.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(ConfirmProfileImgFragment.this).commit();
            }
        });
    }
}