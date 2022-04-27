package com.example.food_product_comparison_android_app.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_product_comparison_android_app.GeneralJavaClasses.NutritionAttribute;
import com.example.food_product_comparison_android_app.R;
import com.example.food_product_comparison_android_app.Utils;

import java.util.HashMap;
import java.util.List;

public class NutritionListRecyclerViewAdapter extends RecyclerView.Adapter<NutritionListRecyclerViewAdapter.ViewHolder> {
    private List<NutritionAttribute> attributes;
    private final HashMap<String, Float> nutritionAverages;

    public NutritionListRecyclerViewAdapter(List<NutritionAttribute> attributes, HashMap<String, Float> nutritionAverages) {
        this.attributes = attributes;
        this.nutritionAverages = nutritionAverages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_nutritional_attribute, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.value.setText(attributes.get(position).getAttributePresentation());
        holder.name.setText(attributes.get(position).getAttributeName());

        float diff = attributes.get(position).getAttributeValue()
                / nutritionAverages.get(attributes.get(position).getAttributeName());
        holder.nutrition_attribute_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diff < 1 - Utils.NUTRITION_DEGREE_DIFF_TOLERANCE)
                    Log.e("DEBUG", "LOW");
                else if (diff > 1 + Utils.NUTRITION_DEGREE_DIFF_TOLERANCE)
                    Log.e("DEBUG", "HIGH");
                else
                    Log.e("DEBUG", "NORMAL");
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributes == null ? 0 : attributes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView nutrition_attribute_cv;
        private final TextView value;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            this.nutrition_attribute_cv = view.findViewById(R.id.nutrition_attribute_card_view);
            this.value = view.findViewById(R.id.attribute_value);
            this.name = view.findViewById(R.id.attribute_name);
        }
    }

    public List<NutritionAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NutritionAttribute> attributes) {
        this.attributes = attributes;
    }
}
