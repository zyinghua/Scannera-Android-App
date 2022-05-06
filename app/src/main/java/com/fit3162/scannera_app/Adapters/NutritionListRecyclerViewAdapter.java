package com.fit3162.scannera_app.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fit3162.scannera_app.GeneralJavaClasses.NutritionAttribute;
import com.fit3162.scannera_app.R;
import com.fit3162.scannera_app.Utils;

import java.util.HashMap;
import java.util.List;

public class NutritionListRecyclerViewAdapter extends RecyclerView.Adapter<NutritionListRecyclerViewAdapter.ViewHolder> {
    private Activity activityContext;
    private List<NutritionAttribute> attributes;
    private final HashMap<String, Float> nutritionAverages;

    public NutritionListRecyclerViewAdapter(Activity activityContext, List<NutritionAttribute> attributes, HashMap<String, Float> nutritionAverages) {
        this.activityContext = activityContext;
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
                if (holder.degree.getVisibility() == View.GONE)
                {
                    holder.degree.setVisibility(View.VISIBLE);

                    if (diff < 1 - Utils.NUTRITION_DEGREE_DIFF_TOLERANCE)
                    {
                        holder.nutrition_attribute_layout.setBackground(AppCompatResources.getDrawable(activityContext, R.drawable.nutrition_attribute_low_degree_bg));
                        holder.degree.setText(activityContext.getString(R.string.nutrition_degree_low));
                        holder.degree.setTextColor(activityContext.getResources().getColor(R.color.grey));
                    }
                    else if (diff > 1 + Utils.NUTRITION_DEGREE_DIFF_TOLERANCE)
                    {
                        holder.degree.setText(activityContext.getString(R.string.nutrition_degree_normal));
                        holder.degree.setTextColor(activityContext.getResources().getColor(R.color.denim));
                    }
                    else
                    {
                        holder.nutrition_attribute_layout.setBackground(AppCompatResources.getDrawable(activityContext, R.drawable.nutrition_attribute_high_degree_bg));
                        holder.degree.setText(activityContext.getString(R.string.nutrition_degree_high));
                        holder.degree.setTextColor(activityContext.getResources().getColor(R.color.pinky_red));
                    }
                }
                else
                {
                    holder.degree.setText("");
                    holder.degree.setVisibility(View.GONE);
                    holder.nutrition_attribute_layout.setBackground(AppCompatResources.getDrawable(activityContext, R.drawable.nutritional_attribute_cardview_background));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributes == null ? 0 : attributes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView nutrition_attribute_cv;
        private final ConstraintLayout nutrition_attribute_layout;
        private final TextView value;
        private final TextView name;
        private final TextView degree;

        public ViewHolder(View view) {
            super(view);

            this.nutrition_attribute_cv = view.findViewById(R.id.nutrition_attribute_card_view);
            this.nutrition_attribute_layout = view.findViewById(R.id.nutrition_attribute_layout);
            this.value = view.findViewById(R.id.attribute_value);
            this.name = view.findViewById(R.id.attribute_name);
            this.degree = view.findViewById(R.id.attribute_degree);
        }
    }

    public List<NutritionAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NutritionAttribute> attributes) {
        this.attributes = attributes;
    }
}
