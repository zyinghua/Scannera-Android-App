package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.CameraPermissionRequiredDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProductFeedActivity extends AppCompatActivity {
    private ImageButton top_back_btn;
    private ConstraintLayout mainConstraintLayout;
    private ConstraintLayout dynamic_input_prompt;
    private ConstraintLayout brand_view_group;
    private ConstraintLayout name_view_group;
    private ConstraintLayout price_view_group;
    private ConstraintLayout category_view_group;
    private ConstraintLayout nutri_table_title_views;
    private ImageView nutrition_table_pic;
    private MaterialButton confirm_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_feed);

        this.findViews();
        this.setUpListeners();
    }

    private void findViews()
    {
        this.top_back_btn = findViewById(R.id.top_back_btn);
        this.dynamic_input_prompt = findViewById(R.id.dynamic_input_prompt);
        this.brand_view_group = findViewById(R.id.product_brand_views);
        this.name_view_group = findViewById(R.id.product_name_views);
        this.price_view_group = findViewById(R.id.product_price_views);
        this.category_view_group = findViewById(R.id.product_category_views);
        this.nutri_table_title_views = findViewById(R.id.product_nutritional_table_title_views);
        this.nutrition_table_pic = findViewById(R.id.nutritional_table_pic);
        this.confirm_btn = findViewById(R.id.confirm_btn);
        this.mainConstraintLayout = findViewById(R.id.product_feed_constraint_layout);
    }

    private void setUpListeners()
    {
        this.top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.brand_view_group.findViewById(R.id.product_brand_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(brand_view_group.findViewById(R.id.product_brand_input), getString(R.string.product_brand), false);
            }
        });

        this.name_view_group.findViewById(R.id.product_name_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(name_view_group.findViewById(R.id.product_name_input), getString(R.string.product_name),false);
            }
        });

        this.price_view_group.findViewById(R.id.product_price_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(price_view_group.findViewById(R.id.product_price_input), getString(R.string.product_price),true);
            }
        });

        this.category_view_group.findViewById(R.id.product_category_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryEditDialog(category_view_group.findViewById(R.id.product_category_input));
            }
        });

        this.nutri_table_title_views.findViewById(R.id.retake_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureAPhoto();
            }
        });

        this.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.dynamic_input_prompt.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView input_title = dynamic_input_prompt.findViewById(R.id.input_title);
                TextInputLayout input_layout = dynamic_input_prompt.findViewById(R.id.input_layout);
                TextInputEditText input_et = dynamic_input_prompt.findViewById(R.id.input_et);

                if (input_et != null && input_et.getText().toString().isEmpty()) {
                    input_layout.setError(getString(R.string.error_input_must_not_be_empty));
                }
                else {
                    if (input_layout != null)
                        input_layout.setError(null);

                    if (((TextView) brand_view_group.findViewById(R.id.product_brand_input)).getText().toString().isEmpty()) {
                        /*Process brand input*/

                        brand_view_group.setVisibility(View.VISIBLE);
                        ((TextView) brand_view_group.findViewById(R.id.product_brand_input)).setText(input_et.getText().toString());

                        Barrier barrier = mainConstraintLayout.findViewById(R.id.name_barrier);
                        moveDynamicInputPrompt(mainConstraintLayout, barrier);

                        input_et.setText("");
                        input_title.setText(getString(R.string.product_name));
                    } else if (((TextView) name_view_group.findViewById(R.id.product_name_input)).getText().toString().isEmpty()) {
                        /*Process name input*/

                        name_view_group.setVisibility(View.VISIBLE);
                        ((TextView) name_view_group.findViewById(R.id.product_name_input)).setText(input_et.getText().toString());

                        Barrier barrier = mainConstraintLayout.findViewById(R.id.price_barrier);
                        moveDynamicInputPrompt(mainConstraintLayout, barrier);

                        input_et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        input_et.setText("");
                        input_title.setText(getString(R.string.product_price));
                    } else if (((TextView) price_view_group.findViewById(R.id.product_price_input)).getText().toString().isEmpty()) {
                        /*Process price input*/

                        price_view_group.setVisibility(View.VISIBLE);
                        ((TextView) price_view_group.findViewById(R.id.product_price_input)).setText(input_et.getText().toString());

                        Barrier barrier = mainConstraintLayout.findViewById(R.id.category_barrier);
                        moveDynamicInputPrompt(mainConstraintLayout, barrier);

                        input_title.setText(getString(R.string.product_category));
                        transformTextInputToDropDownMenu();
                    } else if (((TextView) category_view_group.findViewById(R.id.product_category_input)).getText().toString().isEmpty()) {
                        /*Process category input*/

                        AutoCompleteTextView acTv = dynamic_input_prompt.findViewById(R.id.cdm_autoCompTv);

                        if (acTv.getText().toString().isEmpty())
                        {
                            TextInputLayout category_input_layout = dynamic_input_prompt.findViewById(R.id.category_dropdown_menu);
                            category_input_layout.setError(getString(R.string.error_input_must_not_be_empty));
                        }
                        else
                        {
                            category_view_group.setVisibility(View.VISIBLE);
                            ((TextView) category_view_group.findViewById(R.id.product_category_input)).setText(acTv.getText().toString());

                            moveDynamicInputPrompt(mainConstraintLayout, nutri_table_title_views);
                            input_title.setText(getString(R.string.product_nutritional_table));

                            dynamic_input_prompt.removeView(dynamic_input_prompt.findViewById(R.id.category_dropdown_menu));
                            ConstraintSet set = new ConstraintSet();
                            set.clone(dynamic_input_prompt);
                            set.connect(R.id.next_btn, ConstraintSet.TOP, R.id.input_title, ConstraintSet.BOTTOM);
                            set.applyTo(dynamic_input_prompt);

                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setText(getString(R.string.capture));
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setIcon(getDrawable(R.drawable.ic_capture_photo));
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
                        }
                    } else {
                        captureAPhoto();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.NUTRITION_TABLE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bitmap imgBitmap = (Bitmap) data.getExtras().get("data");
            this.nutrition_table_pic.setImageBitmap(imgBitmap);

            onNutritionPicReceived();
        }
    }

    private void moveDynamicInputPrompt(ConstraintLayout constraintLayout, View view)
    {
        ConstraintSet set = new ConstraintSet();
        // The layout has a ConstraintSet already, so we have to get a clone of it to manipulate.
        set.clone(constraintLayout);
        // Now we can make the connections. All of our views and their ids are available in the
        // ConstraintSet.
        set.connect(dynamic_input_prompt.getId(), ConstraintSet.TOP, view.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);
    }

    private void showEditDialog(TextView input, String title, boolean isNumerical)
    {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_product_feed_tv_edit);

        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.edit) + " " + title);

        dialog.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (isNumerical) ((TextInputEditText) dialog.findViewById(R.id.edit_et)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        dialog.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText edited_input_et = dialog.findViewById(R.id.edit_et);

                if (edited_input_et.getText().toString().isEmpty()) {
                    TextInputLayout edit_input_layout = dialog.findViewById(R.id.edit_text_input_layout);
                    edit_input_layout.setError(getString(R.string.error_input_must_not_be_empty));
                }
                else
                {
                    input.setText(edited_input_et.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void showCategoryEditDialog(TextView input)
    {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_product_feed_tv_edit_category);

        setCategoryAdapter(dialog.findViewById(R.id.cdm_autoCompTv_edit));
        dialog.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView acTv = dialog.findViewById(R.id.cdm_autoCompTv_edit);

                if (acTv.getText().toString().isEmpty()) {
                    TextInputLayout edit_input_layout = dialog.findViewById(R.id.category_dropdown_menu_edit);
                    edit_input_layout.setError(getString(R.string.error_input_must_not_be_empty));
                }
                else
                {
                    input.setText(acTv.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void transformTextInputToDropDownMenu()
    {
        this.dynamic_input_prompt.removeView(this.dynamic_input_prompt.findViewById(R.id.input_layout));
        this.dynamic_input_prompt.findViewById(R.id.category_dropdown_menu).setVisibility(View.VISIBLE);

        setCategoryAdapter(this.dynamic_input_prompt.findViewById(R.id.category_dropdown_menu).findViewById(R.id.cdm_autoCompTv));

        ConstraintSet set = new ConstraintSet();
        set.clone(dynamic_input_prompt);
        set.connect(R.id.next_btn, ConstraintSet.TOP, R.id.category_dropdown_menu, ConstraintSet.BOTTOM);
        set.applyTo(dynamic_input_prompt);
    }

    private void setCategoryAdapter(AutoCompleteTextView acTv)
    {
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        acTv.setAdapter(categoryArrayAdapter);
    }

    private void captureAPhoto()
    {
        checkPermissions();

        Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//      if (capturePicIntent.resolveActivity(getPackageManager()) != null)
        startActivityForResult(capturePicIntent, Utils.NUTRITION_TABLE_PIC_REQUEST);
    }

    private void onNutritionPicReceived()
    {
        // Show up the confirm button only when this as the last step is successfully completed

        mainConstraintLayout.removeView(dynamic_input_prompt);
        this.nutri_table_title_views.setVisibility(View.VISIBLE);
        this.confirm_btn.setVisibility(View.VISIBLE);
    }

    private void checkPermissions()
    {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, Utils.CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Utils.CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    DialogFragment cameraDialogFragment = new CameraPermissionRequiredDialogFragment(getApplicationContext().getPackageName());
                    cameraDialogFragment.show(getSupportFragmentManager(), "Camera Permission");
                }
                break;
            default:
                break;
        }
    }
}