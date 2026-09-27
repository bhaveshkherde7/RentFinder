package com.example.rentfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingAdapter
        extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private final Context context;

    private final int[] images = {
            R.drawable.ic_homw2,
            R.drawable.ic_search1,
            R.drawable.ic_people1
    };

    private final String[] titles = {
            "Find Your Perfect Home",
            "Search Easily",
            "Connect With Owners"
    };

    private final String[] descriptions = {
            "Find comfortable rooms and properties near your college.",
            "Search rooms, PGs and rental properties according to your needs.",
            "Contact property owners and make your stay easier."
    };

    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_onboarding,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        holder.image.setImageResource(images[position]);

        holder.title.setText(titles[position]);

        holder.description.setText(
                descriptions[position]
        );
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(
                    R.id.ivOnboarding
            );

            title = itemView.findViewById(
                    R.id.tvTitle
            );

            description = itemView.findViewById(
                    R.id.tvDescription
            );
        }
    }
}