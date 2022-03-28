package com.example.food_product_comparison_android_app.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;

import com.example.food_product_comparison_android_app.AccountInfoActivity;
import com.example.food_product_comparison_android_app.R;

import java.util.Objects;

public class LoadingDialogFragment extends DialogFragment {
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(true);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
