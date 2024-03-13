package com.mobileapp.cookie_jar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recipe_RecyclerViewAdapater extends RecyclerView.Adapter<Recipe_RecyclerViewAdapater.MyViewHolder> {
    Context context;
    ArrayList<recipeModel> recipeModels;
    public Recipe_RecyclerViewAdapater(Context context, ArrayList<recipeModel> recipeModels){
        this.context = context;
        this.recipeModels = recipeModels;
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
        holder.recipe.setText(recipeModels.get(position).getRecipeName());
        holder.time.setText(recipeModels.get(position).getRecipeCookTime());
        holder.description.setText((recipeModels.get(position).getRecipeDescription()));
        holder.imageView.setImageResource(recipeModels.get(position).getRecipeImage());
    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
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
