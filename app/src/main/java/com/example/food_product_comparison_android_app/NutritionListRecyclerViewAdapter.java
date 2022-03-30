package com.example.food_product_comparison_android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NutritionListRecyclerViewAdapter extends RecyclerView.Adapter<NutritionListRecyclerViewAdapter.ViewHolder> {
    private List<NutritionalAttribute> attributes;

    public NutritionListRecyclerViewAdapter(List<NutritionalAttribute> attributes) {
        this.attributes = attributes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_nutritional_attribute, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.value.setText(attributes.get(position).getAttribute_value());
        holder.name.setText(attributes.get(position).getAttribute_name());
    }

    @Override
    public int getItemCount() {
        return attributes == null ? 0 : attributes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView value;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            this.value = view.findViewById(R.id.attribute_value);
            this.name = view.findViewById(R.id.attribute_name);
        }
    }

    public List<NutritionalAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NutritionalAttribute> attributes) {
        this.attributes = attributes;
    }
}
