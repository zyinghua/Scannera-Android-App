package com.example.food_product_comparison_android_app;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

public class EditDialog extends Dialog {
    Context context;

    public EditDialog(@NonNull Context context, String titleText) {
        super(context);

        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(true);
        this.setContentView(R.layout.dialog_edit);
        this.setTitleText(titleText);

        this.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitleText(String titleText)
    {
        ((TextView) this.findViewById(R.id.dialog_title)).setText(context.getString(R.string.edit) + " " + titleText);
    }

    public void setInputType(int inputType)
    {
        ((TextInputEditText) this.findViewById(R.id.edit_et)).setInputType(inputType);
    }

    public void setConfirmBtnOnClickListener(View.OnClickListener l)
    {
        this.findViewById(R.id.confirm_btn).setOnClickListener(l);
    }
}
