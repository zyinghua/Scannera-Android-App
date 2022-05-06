package com.fit3162.scannera_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fit3162.scannera_app.Dialogs.EditDialog;
import com.fit3162.scannera_app.Dialogs.LoadingDialog;
import com.fit3162.scannera_app.Fragments.PermissionRequiredDialogFragment;
import com.fit3162.scannera_app.GeneralJavaClasses.Product;
import com.fit3162.scannera_app.GeneralJavaClasses.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFeedActivity extends AppCompatActivity {
    private ConstraintLayout mainConstraintLayout;
    private ConstraintLayout dynamic_input_prompt;
    private ConstraintLayout brand_view_group;
    private ConstraintLayout name_view_group;
    private ConstraintLayout price_view_group;
    private ConstraintLayout category_view_group;
    private ConstraintLayout nutrition_info_title_views;
    private ConstraintLayout product_look_title_views;
    private ImageView nutrition_info_pic;
    private ImageView product_look_pic;
    private MaterialButton confirm_btn;
    private String product_barcode;
    private String product_brand;
    private String product_name;
    private Float product_price;
    private String product_category;
    private File nutrition_pic_file;
    private File product_pic_file;
    private static final String NUTRITION_PIC_FILE_NAME = "nutritionInfo";
    private static final String PRODUCT_PIC_FILE_NAME = "productPic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_feed);

        this.product_barcode = getIntent().getStringExtra(Utils.PRODUCT_BARCODE_TRANSFER_TAG);

        this.setUpToolbar();
        this.findViews();
        this.setUpListeners();
    }

    private void findViews()
    {
        this.dynamic_input_prompt = findViewById(R.id.dynamic_input_prompt);
        this.brand_view_group = findViewById(R.id.product_brand_views);
        this.name_view_group = findViewById(R.id.product_name_views);
        this.price_view_group = findViewById(R.id.product_price_views);
        this.category_view_group = findViewById(R.id.product_category_views);
        this.nutrition_info_title_views = findViewById(R.id.product_nutritional_info_title_views);
        this.product_look_title_views = findViewById(R.id.product_look_title_views);
        this.nutrition_info_pic = findViewById(R.id.nutritional_info_pic);
        this.product_look_pic = findViewById(R.id.product_look_pic);
        this.confirm_btn = findViewById(R.id.confirm_btn);
        this.mainConstraintLayout = findViewById(R.id.product_feed_constraint_layout);
    }

    private void setUpListeners()
    {
        this.brand_view_group.findViewById(R.id.product_brand_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView product_brand_input = brand_view_group.findViewById(R.id.product_brand_input);
                showEditDialog(brand_view_group.findViewById(R.id.product_brand_input), getString(R.string.product_brand), product_brand_input.getText().toString(),  false);
            }
        });

        this.name_view_group.findViewById(R.id.product_name_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView product_name_input = name_view_group.findViewById(R.id.product_name_input);
                showEditDialog(name_view_group.findViewById(R.id.product_name_input), getString(R.string.product_name), product_name_input.getText().toString(), false);
            }
        });

        this.price_view_group.findViewById(R.id.product_price_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView product_price_input = price_view_group.findViewById(R.id.product_price_input);
                showEditDialog(price_view_group.findViewById(R.id.product_price_input), getString(R.string.product_price), product_price_input.getText().toString().substring(1), true);
            }
        });

        this.category_view_group.findViewById(R.id.product_category_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView product_category_input = category_view_group.findViewById(R.id.product_category_input);
                showCategoryEditDialog(category_view_group.findViewById(R.id.product_category_input), product_category_input.getText().toString());
            }
        });

        this.nutrition_info_title_views.findViewById(R.id.nutri_retake_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsBeforeExecution(Utils.NUTRITION_TABLE_PIC_REQUEST);
            }
        });

        this.product_look_title_views.findViewById(R.id.plook_retake_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsBeforeExecution(Utils.PRODUCT_LOOK_PIC_REQUEST);
            }
        });

        this.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_barcode == null || product_brand == null ||
                        product_name == null || product_price == null ||
                        product_category == null || nutrition_pic_file == null || product_pic_file == null)
                {
                    Toast.makeText(ProductFeedActivity.this, getString(R.string.collect_contributed_product_data_error), Toast.LENGTH_LONG).show();
                }
                else
                {
                    postProductDataToServer(System.currentTimeMillis());
                }
            }
        });

        this.dynamic_input_prompt.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                TextView input_title = dynamic_input_prompt.findViewById(R.id.input_title);
                TextInputLayout input_layout = dynamic_input_prompt.findViewById(R.id.input_layout);
                TextInputEditText input_et = dynamic_input_prompt.findViewById(R.id.input_et);

                if (input_et != null && Objects.requireNonNull(input_et.getText()).toString().isEmpty()) {
                    input_layout.setError(getString(R.string.error_input_cannot_be_empty));
                }
                else {
                    if (input_layout != null)
                        input_layout.setError(null);

                    if (input_et != null && ((TextView) brand_view_group.findViewById(R.id.product_brand_input)).getText().toString().isEmpty()) {
                        /*Process brand input*/

                        brand_view_group.setVisibility(View.VISIBLE);
                        product_brand = input_et.getText().toString();
                        ((TextView) brand_view_group.findViewById(R.id.product_brand_input)).setText(product_brand);

                        Barrier barrier = mainConstraintLayout.findViewById(R.id.name_barrier);
                        moveDynamicInputPrompt(mainConstraintLayout, barrier);

                        input_et.setText("");
                        input_title.setText(getString(R.string.product_name));
                    } else if (input_et != null && ((TextView) name_view_group.findViewById(R.id.product_name_input)).getText().toString().isEmpty()) {
                        /*Process name input*/

                        name_view_group.setVisibility(View.VISIBLE);
                        product_name = input_et.getText().toString();
                        ((TextView) name_view_group.findViewById(R.id.product_name_input)).setText(product_name);

                        Barrier barrier = mainConstraintLayout.findViewById(R.id.price_barrier);
                        moveDynamicInputPrompt(mainConstraintLayout, barrier);

                        input_et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        input_et.setText("");
                        input_title.setText(getString(R.string.product_price));
                    } else if (input_et != null && ((TextView) price_view_group.findViewById(R.id.product_price_input)).getText().toString().isEmpty()) {
                        /*Process price input*/

                        price_view_group.setVisibility(View.VISIBLE);
                        product_price = Float.parseFloat(input_et.getText().toString());
                        ((TextView) price_view_group.findViewById(R.id.product_price_input)).setText("$" + product_price);

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
                            product_category = acTv.getText().toString();
                            ((TextView) category_view_group.findViewById(R.id.product_category_input)).setText(product_category);

                            moveDynamicInputPrompt(mainConstraintLayout, nutrition_info_title_views);
                            input_title.setText(getString(R.string.product_nutrition_info));
                            // Remove the input edittext as we don't need it anymore
                            dynamic_input_prompt.removeView(dynamic_input_prompt.findViewById(R.id.category_dropdown_menu));
                            ConstraintSet set = new ConstraintSet();
                            set.clone(dynamic_input_prompt);
                            set.connect(R.id.next_btn, ConstraintSet.TOP, R.id.input_title, ConstraintSet.BOTTOM);
                            set.applyTo(dynamic_input_prompt);

                            // change next button text and add an icon to it
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setText(getString(R.string.capture));
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_capture_photo));
                            ((MaterialButton) dynamic_input_prompt.findViewById(R.id.next_btn)).setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
                        }
                    } else if (nutrition_info_pic.getDrawable() == null) {
                        /*Process Nutritional Information Picture input*/
                        checkPermissionsBeforeExecution(Utils.NUTRITION_TABLE_PIC_REQUEST);
                    } else {
                        /*Process Product Look Picture input*/
                        checkPermissionsBeforeExecution(Utils.PRODUCT_LOOK_PIC_REQUEST);
                    }
                }
            }
        });
    }

    private void postProductDataToServer(Long init_time)
    {
        LoadingDialog loading_dialog = new LoadingDialog(this);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        User user = Utils.getLoggedUser(this);
        RequestBody user_id_rb = RequestBody.create(MediaType.parse("text/plain"), user.getId());
        RequestBody barcode_rb = RequestBody.create(MediaType.parse("text/plain"), product_barcode);
        RequestBody brand_rb = RequestBody.create(MediaType.parse("text/plain"), product_brand);
        RequestBody name_rb = RequestBody.create(MediaType.parse("text/plain"), product_name);
        RequestBody price_rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product_price));
        RequestBody category_rb = RequestBody.create(MediaType.parse("text/plain"), product_category);

        MultipartBody.Part nutrition_pic_file_part = MultipartBody.Part.createFormData(
                ServerRetrofitAPI.PRODUCT_NUTRITION_PIC_SERVER, nutrition_pic_file.getName(), RequestBody.create(MediaType.parse("image/*"), nutrition_pic_file));

        MultipartBody.Part product_pic_file_part = MultipartBody.Part.createFormData(
                ServerRetrofitAPI.PRODUCT_DISPLAY_PIC_SERVER, product_pic_file.getName(), RequestBody.create(MediaType.parse("image/*"), product_pic_file));

        Call<ResponseBody> call = Utils.getServerAPI(this).postProduct(user_id_rb, barcode_rb, brand_rb, name_rb, price_rb, category_rb, nutrition_pic_file_part, product_pic_file_part);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful() && response.body() != null)
                {
                    Toast.makeText(ProductFeedActivity.this, String.format(getString(R.string.on_successful_product_contribution_msg), Utils.PRODUCT_CONTRIBUTION_POINTS), Toast.LENGTH_LONG).show();
                    user.setContributionScore(user.getContributionScore() + Utils.PRODUCT_CONTRIBUTION_POINTS);
                    Utils.updateUserLoginStatus(ProductFeedActivity.this, user);
                    finish();

                    Product product = Utils.parseASingleProductFromResponse(ProductFeedActivity.this, response.body());
                    Utils.navigateToProductInfoActivity(ProductFeedActivity.this, product);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC)
                    {
                        postProductDataToServer(init_time);
                        Log.e("DEBUG", response.code() + "");
                    }
                    else
                    {
                        Toast.makeText(ProductFeedActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(ProductFeedActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
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
            Bitmap imgBitmap = BitmapFactory.decodeFile(product_pic_file.getAbsolutePath());
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

    private void showEditDialog(TextView input, String title, String autoFillInputText, boolean isNumerical)
    {
        final EditDialog dialog = new EditDialog(this, title, autoFillInputText);

        if (isNumerical)
            dialog.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        dialog.setConfirmBtnOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                TextInputEditText edited_input_et = dialog.findViewById(R.id.edit_et);

                if (Objects.requireNonNull(edited_input_et.getText()).toString().isEmpty()) {
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

    private void showCategoryEditDialog(TextView input, String autoFillInputText)
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

        ((AutoCompleteTextView) dialog.findViewById(R.id.cdm_autoCompTv_edit)).setText(autoFillInputText);

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

    private void capturePhoto(String file_name){
        // SHOULD BE CALLED WHEN CAMERA PERMISSION IS GRANTED

        Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            if (file_name.equals(NUTRITION_PIC_FILE_NAME))
            {
                this.nutrition_pic_file = getPhotoFile(file_name); // Get file
            }
            else
            {
                this.product_pic_file = getPhotoFile(file_name); // Get file
            }
        } catch (IOException e)
        {
            Toast.makeText(ProductFeedActivity.this, getString(R.string.capture_photo_io_error), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Get Uri for the file and put with the particular key to allow the camera app we are
        // delegating to, to be able to access the file and put the output there
        Uri fpUri = FileProvider.getUriForFile(this, getString(R.string.app_authority), file_name.equals(NUTRITION_PIC_FILE_NAME) ? this.nutrition_pic_file : this.product_pic_file);
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
        this.nutrition_info_title_views.setVisibility(View.VISIBLE);

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

    private void checkPermissionsBeforeExecution(int requestCode)
    {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, requestCode);
        }
        else
        {
            // Permission already granted
            if (requestCode == Utils.NUTRITION_TABLE_PIC_REQUEST) {
                capturePhoto(NUTRITION_PIC_FILE_NAME);
            } else {
                capturePhoto(PRODUCT_PIC_FILE_NAME);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            DialogFragment cameraDialogFragment = new PermissionRequiredDialogFragment(
                    getApplicationContext().getPackageName(), getString(R.string.camera), Utils.ON_PERMISSION_DENIED_STAY);
            cameraDialogFragment.show(getSupportFragmentManager(), "Camera Permission");
        }
        else
        {
            if (requestCode == Utils.NUTRITION_TABLE_PIC_REQUEST) {
                capturePhoto(NUTRITION_PIC_FILE_NAME);
            } else {
                capturePhoto(PRODUCT_PIC_FILE_NAME);
            }
        }
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            // This is to sync the toolbar up button with the back button
            onBackPressed();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}