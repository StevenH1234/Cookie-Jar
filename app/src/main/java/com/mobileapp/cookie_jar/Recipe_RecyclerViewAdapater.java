package com.mobileapp.cookie_jar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Recipe_RecyclerViewAdapater extends RecyclerView.Adapter<Recipe_RecyclerViewAdapater.MyViewHolder> {
    Context context;
    List<Recipe> recipes;

    // constructor
    public Recipe_RecyclerViewAdapater(Context context){
        this.context = context;
        this.recipes = new ArrayList<>();
    }
    @NonNull
    @Override
    public Recipe_RecyclerViewAdapater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.overview_recycler_view_row, parent, false);
        return new Recipe_RecyclerViewAdapater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recipe_RecyclerViewAdapater.MyViewHolder holder, int position) {
        if (recipes != null) {
            Recipe current = recipes.get(position);
            holder.recipe.setText(current.getRecipeName());
            holder.time.setText(String.valueOf(current.getCookTime()));
            holder.description.setText(current.getDescription());
        } else {
            holder.recipe.setText("No Recipes");
        }
    }

    void setRecipe(List<Recipe> data){
        recipes = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (recipes != null){
            return recipes.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView recipe, time, description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.food_image);
            recipe = itemView.findViewById(R.id.recipe_name);
            time = itemView.findViewById(R.id.recipe_cook_time);
            description = itemView.findViewById(R.id.recipe_description);

        }
    }
}
