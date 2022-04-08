package com.example.food_product_comparison_android_app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.DialogFragment;

import com.example.food_product_comparison_android_app.AccountInfoActivity;
import com.example.food_product_comparison_android_app.R;

import java.util.Objects;

public class DeleteAccountConfirmDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_account_msg)
                .setTitle(R.string.dialog_delete_account_title)
                .setPositiveButton(R.string.confirm_red, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((AccountInfoActivity) requireActivity()).deleteUserAccount(System.currentTimeMillis());;
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
