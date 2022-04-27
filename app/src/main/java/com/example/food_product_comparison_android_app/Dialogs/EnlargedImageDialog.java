package com.example.food_product_comparison_android_app.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.food_product_comparison_android_app.R;
import com.squareup.picasso.Picasso;

public class EnlargedImageDialog extends Dialog {
    Context context;

    public EnlargedImageDialog(@NonNull Context context, String img_url) {
        super(context);

        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(true);
        this.setContentView(R.layout.dialog_enlarged_image);

        ImageView imageView = findViewById(R.id.enlarged_image);
        Picasso.get().load(img_url).into(imageView); // img_url is guaranteed non-null when calling this Dialog
    }
}
