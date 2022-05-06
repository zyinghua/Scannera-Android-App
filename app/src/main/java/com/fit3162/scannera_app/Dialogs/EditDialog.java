package com.fit3162.scannera_app.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fit3162.scannera_app.R;
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

    public EditDialog(@NonNull Context context, String titleText, String autoFillInputText) {
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

        this.setAutoFillInputText(autoFillInputText);
    }

    public void setTitleText(String titleText)
    {
        ((TextView) this.findViewById(R.id.dialog_title)).setText(context.getString(R.string.edit) + " " + titleText);
    }

    public void setAutoFillInputText(String autoFillInputText)
    {
        ((TextInputEditText) this.findViewById(R.id.edit_et)).setText(autoFillInputText);
    }

    public void setInputType(int inputType)
    {
        ((TextInputEditText) this.findViewById(R.id.edit_et)).setInputType(inputType);
    }

    public void setConfirmBtnOnClickListener(View.OnClickListener l)
    {
        this.findViewById(R.id.confirm_btn).setOnClickListener(l);
    }

    public void setMaxInputLength(int maxLength)
    {
        EditText editText = findViewById(R.id.edit_et);

        InputFilter[] editFilters = editText.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(newFilters);
    }
}
