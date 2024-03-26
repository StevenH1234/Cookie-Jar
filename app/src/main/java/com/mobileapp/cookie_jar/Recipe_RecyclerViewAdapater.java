package com.mobileapp.cookie_jar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Recipe_RecyclerViewAdapater extends RecyclerView.Adapter<Recipe_RecyclerViewAdapater.MyViewHolder> {
    Context context;
    List<Recipe> recipes;
    recipeModel recipeModel;
    // temp solution: copy of OG list
    List<Recipe> OGrecipes;

    // constructor
    public Recipe_RecyclerViewAdapater(Context context, recipeModel recipeViewModel){
        this.context = context;
        this.recipes = new ArrayList<>();
        this.OGrecipes = new ArrayList<>();
        this.recipeModel = recipeViewModel;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    OnItemClickListener deleteListener;
    void setOnItemClickListener(OnItemClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public Recipe_RecyclerViewAdapater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.overview_recycler_view_row, parent, false);
        return new Recipe_RecyclerViewAdapater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recipe_RecyclerViewAdapater.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (recipes != null) {
            Recipe current = recipes.get(position);
            holder.recipe.setText(current.getRecipeName());
            holder.time.setText(String.valueOf(current.getCookTime()));
            holder.description.setText(current.getDescription());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Log.d("clicked", String.valueOf(recipes.get(holder.getAdapterPosition()).getId()));
//                    int id = recipes.get(holder.getAdapterPosition()).getId();
//                    recipeModel.deleteRecipe(id);
//                    notifyDataSetChanged();
                    if (deleteListener != null) {
                        deleteListener.onItemClick(position);
                    }
                }
            });
        } else {
            holder.recipe.setText("No Recipes");
        }
    }

    void setFilteredRecipes(List<Recipe> filteredRecipes) {
        recipes = filteredRecipes;
        notifyDataSetChanged();
    }
    void setRecipe(List<Recipe> data){
        recipes = data;
        OGrecipes = data;
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

    public List<Recipe> getOGrecipes() { return OGrecipes; }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView recipe, time, description;
        ImageButton deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.food_image);
            recipe = itemView.findViewById(R.id.recipe_name);
            time = itemView.findViewById(R.id.recipe_cook_time);
            description = itemView.findViewById(R.id.recipe_description);
            deleteButton = itemView.findViewById(R.id.delete_recipe);

        }
    }
}
