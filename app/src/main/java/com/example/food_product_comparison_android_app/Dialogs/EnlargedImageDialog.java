package com.example.food_product_comparison_android_app.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.food_product_comparison_android_app.R;

public class EnlargedImageDialog extends Dialog {
    Context context;

    public EnlargedImageDialog(@NonNull Context context) {
        super(context);

        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(true);
        this.setContentView(R.layout.dialog_enlarged_image);

        ImageView image = findViewById(R.id.enlarged_image);
        image.setImageDrawable(context.getDrawable(R.drawable.product_sample));
    }
}
