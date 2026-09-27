package com.example.rentfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PropertyAdapter
        extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private final ArrayList<Property> propertyList;
    private final Context context;

    public PropertyAdapter(
            ArrayList<Property> propertyList,
            Context context) {

        this.propertyList = propertyList;
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_property,
                                parent,
                                false
                        );

        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PropertyViewHolder holder,
            int position) {

        Property property =
                propertyList.get(position);

        holder.tvPropertyName.setText(
                property.name
        );

        holder.tvLocation.setText(
                "📍 " + property.location
        );

        /*
         * IMPORTANT:
         * Clicking anywhere on property card
         * opens PropertyDetailsActivity.
         */
        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            PropertyDetailsActivity.class
                    );

            // Send property ID
            intent.putExtra(
                    "property_id",
                    property.id
            );

            // Get student email
            String studentEmail =
                    context.getSharedPreferences(
                            "RentalFinder",
                            Context.MODE_PRIVATE
                    ).getString(
                            "student_email",
                            ""
                    );

            // Send student email
            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return propertyList.size();
    }

    public static class PropertyViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvPropertyName;
        TextView tvLocation;

        public PropertyViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvPropertyName =
                    itemView.findViewById(
                            R.id.tvPropertyName
                    );

            tvLocation =
                    itemView.findViewById(
                            R.id.tvLocation
                    );
        }
    }
}