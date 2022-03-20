package com.example.food_product_comparison_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProductFeedActivity extends AppCompatActivity {
    private ImageButton top_back_btn;
    private LinearLayout dynamic_input_prompt;
    private ConstraintLayout brand_view_group;
    private ConstraintLayout name_view_group;
    private ConstraintLayout price_view_group;
    private ConstraintLayout category_view_group;
    private ConstraintLayout nutri_table_title_views;
    private ImageView nutritional_table_pic;
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
        this.nutritional_table_pic = findViewById(R.id.nutritional_table_pic);
        this.confirm_btn = findViewById(R.id.confirm_btn);
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
                showEditDialog(brand_view_group.findViewById(R.id.product_brand_input));
            }
        });

        this.name_view_group.findViewById(R.id.product_name_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(name_view_group.findViewById(R.id.product_name_input));
            }
        });

        this.price_view_group.findViewById(R.id.product_price_edit_tvbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(price_view_group.findViewById(R.id.product_price_input));
            }
        });

        this.dynamic_input_prompt.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView input_title = dynamic_input_prompt.findViewById(R.id.input_title);
                TextInputLayout input_layout = dynamic_input_prompt.findViewById(R.id.input_layout);
                TextInputEditText input_et = dynamic_input_prompt.findViewById(R.id.input_et);

                if (input_et.getText().toString().isEmpty()) {
                    input_layout.setError(getString(R.string.error_input_must_not_be_empty));
                }
                else {
                    input_layout.setError(null);
                    ConstraintLayout constraintLayout = findViewById(R.id.product_feed_constraint_layout);

                    if (((TextView) brand_view_group.findViewById(R.id.product_brand_input)).getText().toString().isEmpty()) {
                        brand_view_group.setVisibility(View.VISIBLE);
                        ((TextView) brand_view_group.findViewById(R.id.product_brand_input)).setText(input_et.getText().toString());

                        Barrier barrier = constraintLayout.findViewById(R.id.name_barrier);
                        moveDynamicInputPrompt(constraintLayout, barrier);

                        input_et.setText("");
                        input_title.setText(getString(R.string.product_name));
                    } else if (((TextView) name_view_group.findViewById(R.id.product_name_input)).getText().toString().isEmpty()) {
                        name_view_group.setVisibility(View.VISIBLE);
                        ((TextView) name_view_group.findViewById(R.id.product_name_input)).setText(input_et.getText().toString());

                        Barrier barrier = constraintLayout.findViewById(R.id.price_barrier);
                        moveDynamicInputPrompt(constraintLayout, barrier);

                        input_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        input_et.setText("");
                        input_title.setText(getString(R.string.product_price));
                    } else if (((TextView) price_view_group.findViewById(R.id.product_price_input)).getText().toString().isEmpty()) {
                        price_view_group.setVisibility(View.VISIBLE);
                        ((TextView) price_view_group.findViewById(R.id.product_price_input)).setText(input_et.getText().toString());

                        Barrier barrier = constraintLayout.findViewById(R.id.category_barrier);
                        moveDynamicInputPrompt(constraintLayout, barrier);

                        input_et.setText("");
                        input_title.setText(getString(R.string.product_category));
                    }
                }

            }
        });
    }

    private void moveDynamicInputPrompt(ConstraintLayout constraintLayout, Barrier barrier)
    {
        ConstraintSet set = new ConstraintSet();
        // The layout has a ConstraintSet already, so we have to get a clone of it to manipulate.
        set.clone(constraintLayout);
        // Now we can make the connections. All of our views and their ids are available in the
        // ConstraintSet.
        set.connect(dynamic_input_prompt.getId(), ConstraintSet.TOP, barrier.getId(), ConstraintSet.BOTTOM);
        set.applyTo(constraintLayout);
    }

    private void showEditDialog(TextView input)
    {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_product_feed_tv_edit);

        dialog.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText edited_input_et = dialog.findViewById(R.id.edit_et);

                if (edited_input_et.getText().toString().isEmpty()) {
                    edited_input_et.setError(getString(R.string.error_input_must_not_be_empty));
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
}