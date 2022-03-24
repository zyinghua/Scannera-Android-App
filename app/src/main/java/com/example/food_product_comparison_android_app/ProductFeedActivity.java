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
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Fragments.CameraPermissionRequiredDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

public class ProductFeedActivity extends AppCompatActivity {
    private ImageButton top_back_btn;
    private ConstraintLayout mainConstraintLayout;
    private ConstraintLayout dynamic_input_prompt;
    private ConstraintLayout brand_view_group;
    private ConstraintLayout name_view_group;
    private ConstraintLayout price_view_group;
    private ConstraintLayout category_view_group;
    private ConstraintLayout nutri_info_title_views;
    private ConstraintLayout product_look_title_views;
    private ImageView nutrition_info_pic;
    private ImageView product_look_pic;
    private MaterialButton confirm_btn;
    private File nutrition_pic_file;
    private File product_look_file;
    private static final String NUTRITION_PIC_FILE_NAME = "nutrition_info.jpg";
    private static final String PRODUCT_LOOK_FILE_NAME = "product_look.jpg";
    private static final String CAPTURE_PHOTO_IO_EXCEPTION_MSG = "Capture Photo IO Exception";

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
        this.nutri_info_title_views = findViewById(R.id.product_nutritional_info_title_views);
        this.product_look_title_views = findViewById(R.id.product_look_title_views);
        this.nutrition_info_pic = findViewById(R.id.nutritional_info_pic);
        this.product_look_pic = findViewById(R.id.product_look_pic);
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

        this.nutri_info_title_views.findViewById(R.id.nutri_retake_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    capturePhoto(NUTRITION_PIC_FILE_NAME);
                } catch (IOException e) {
                    Toast.makeText(ProductFeedActivity.this, CAPTURE_PHOTO_IO_EXCEPTION_MSG, Toast.LENGTH_LONG).show();
                }
            }
        });

        this.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.dynamic_input_prompt.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView input_title = dynamic_input_prompt.findViewById(R.id.input_title);
                TextInputLayout input_layout = dynamic_input_prompt.findViewById(R.id.input_layout);
                TextInputEditText input_et = dynamic_input_prompt.findViewById(R.id.input_et);

                if (input_et != null && input_et.getText().toString().isEmpty()) {
                    input_layout.setError(getString(R.string.error_input_cannot_be_empty));
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
                        ((TextView) price_view_group.findViewById(R.id.product_price_input)).setText("$" + input_et.getText().toString());

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
                            category_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
                        }
                        else
                        {
                            category_view_group.setVisibility(View.VISIBLE);
                            ((TextView) category_view_group.findViewById(R.id.product_category_input)).setText(acTv.getText().toString());

                            moveDynamicInputPrompt(mainConstraintLayout, nutri_info_title_views);
                            input_title.setText(getString(R.string.product_nutritional_info));

                            // Remove the input edittext as we don't need it anymore
                            dynamic_input_prompt.removeView(dynamic_input_prompt.findViewById(R.id.category_dropdown_menu));
                            ConstraintSet set = new ConstraintSet();
                            set.clone(dynamic_input_prompt);
                            set.connect(R.id.next_btn, ConstraintSet.TOP, R.id.input_title, ConstraintSet.BOTTOM);
                            set.applyTo(dynamic_input_prompt);

                            // change next  button text and add an icon to it
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setText(getString(R.string.capture));
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setIcon(getDrawable(R.drawable.ic_capture_photo));
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
                        }
                    } else if (nutrition_info_pic.getDrawable() == null) {
                        /*Process Nutritional Information Picture input*/
                        try {
                            capturePhoto(NUTRITION_PIC_FILE_NAME);
                        } catch (IOException e) {
                            Toast.makeText(ProductFeedActivity.this, CAPTURE_PHOTO_IO_EXCEPTION_MSG, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        /*Process Product Look Picture input*/
                        try {
                            capturePhoto(PRODUCT_LOOK_FILE_NAME);
                        } catch (IOException e) {
                            Toast.makeText(ProductFeedActivity.this, CAPTURE_PHOTO_IO_EXCEPTION_MSG, Toast.LENGTH_LONG).show();
                        }
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
            // Get the image from the file path we set up previously rather than the intent itself as a bitmap
            Bitmap imgBitmap = BitmapFactory.decodeFile(nutrition_pic_file.getAbsolutePath());
            this.nutrition_info_pic.setImageBitmap(imgBitmap);

            onNutritionPicReceived();
        }
        else if (requestCode == Utils.PRODUCT_LOOK_PIC_REQUEST && resultCode == RESULT_OK)
        {
            // Get the image from the file path we set up previously rather than the intent itself as a bitmap
            Bitmap imgBitmap = BitmapFactory.decodeFile(product_look_file.getAbsolutePath());
            this.product_look_pic.setImageBitmap(imgBitmap);

            onProductLookPicReceived();
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
                    edit_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
                }
                else
                {
                    input.setText((isNumerical ? "$" : "") + edited_input_et.getText().toString());
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
                    edit_input_layout.setError(getString(R.string.error_input_cannot_be_empty));
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

    private void capturePhoto(String file_name) throws IOException {
        checkPermissions();

        Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (file_name.equals(NUTRITION_PIC_FILE_NAME))
        {
            this.nutrition_pic_file = getPhotoFile(file_name); // Get file
        }
        else
        {
            this.product_look_file = getPhotoFile(file_name); // Get file
        }

        // Get Uri for the file and put with the particular key to allow the camera app we are
        // delegating to, to be able to access the file and put the output there
        Uri fpUri = FileProvider.getUriForFile(this, getString(R.string.app_authority), file_name.equals(NUTRITION_PIC_FILE_NAME) ? this.nutrition_pic_file : this.product_look_file);
        capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, fpUri);

        if (capturePicIntent.resolveActivity(getPackageManager()) != null)  // Make sure there exists an app that is able to handle such
            startActivityForResult(capturePicIntent, file_name.equals(NUTRITION_PIC_FILE_NAME) ? Utils.NUTRITION_TABLE_PIC_REQUEST : Utils.PRODUCT_LOOK_PIC_REQUEST);
    }

    private File getPhotoFile(String file_name) throws IOException {
        // Use 'getExternalFilesDir' on Context to access package-specific directories
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(file_name, ".jpg", storageDirectory);
    }

    private void onNutritionPicReceived()
    {
        this.nutri_info_title_views.setVisibility(View.VISIBLE);

        moveDynamicInputPrompt(mainConstraintLayout, nutrition_info_pic);
        ((TextView) dynamic_input_prompt.findViewById(R.id.input_title)).setText(getString(R.string.product_look));
    }

    private void onProductLookPicReceived()
    {
        mainConstraintLayout.removeView(dynamic_input_prompt);
        this.product_look_title_views.setVisibility(View.VISIBLE);
        // Show up the confirm button only when this as the last step is successfully completed
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