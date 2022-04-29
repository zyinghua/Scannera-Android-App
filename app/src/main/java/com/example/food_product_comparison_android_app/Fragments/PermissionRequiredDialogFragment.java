package com.example.food_product_comparison_android_app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.Utils;

import java.util.Objects;

public class PermissionRequiredDialogFragment extends DialogFragment {
    private final String app_package_name;
    private final String permissionName;
    private final int onDenyAction;

    public PermissionRequiredDialogFragment(String app_package_name, String permissionName, int onDenyAction)
    {
        this.app_package_name = app_package_name;
        this.permissionName = permissionName;
        this.onDenyAction = onDenyAction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(String.format(getString(R.string.dialog_permission_required_msg), permissionName))
                .setTitle(String.format(getString(R.string.dialog_permission_required_title), permissionName))
                .setPositiveButton(R.string.dialog_btn_enable, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + app_package_name));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_btn_reject, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(onDenyAction == Utils.ON_PERMISSION_DENIED_BACK)
                        {
                            requireActivity().onBackPressed();
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}