package com.mobileapp.cookie_jar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Recipe_RecyclerViewAdapater extends RecyclerView.Adapter<Recipe_RecyclerViewAdapater.MyViewHolder> {
    Context context;
    List<Recipe> recipes;
    ArrayList<String> URIpaths;
    recipeModel recipeModel;
    List<Recipe> OGrecipes;
    OnDeleteClickListener deleteListener;
    OnEditRecipeClickedListener editListener;
    OnItemClickListener itemListener;

    // constructor
    public Recipe_RecyclerViewAdapater(Context context, recipeModel recipeViewModel){
        this.context = context;
        this.recipes = new ArrayList<>();
        this.OGrecipes = new ArrayList<>();
        this.URIpaths = new ArrayList<>();
        this.recipeModel = recipeViewModel;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int recipe_id);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.itemListener = listener;
    }

    void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    void setOnEditRecipeClickListener(OnEditRecipeClickedListener listener) {
        this.editListener = listener;
    }

    void setURIpaths(ArrayList<String> data){
        this.URIpaths = data;
        notifyDataSetChanged();
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
            //TODO: currently only puts down the images from the gallery not regardless of choice
            String imagePath = URIpaths.get(position);
            for (String paths: URIpaths){
                Log.d("paths are", paths);
            }

            Recipe current = recipes.get(position);
            holder.recipe.setText(current.getRecipeName());
            holder.time.setText(String.valueOf(current.getCookTime()) + " Minutes");
            holder.description.setText(current.getDescription());
            Glide.with(holder.imageView)
                    .load(imagePath)
                    .into(holder.imageView);

            holder.editRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onEditRecipeClick(position, current);
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteListener != null) {
                        deleteListener.onDeleteClick(position);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    recipeModel.refreshRecipes();
                    Log.d("You clicked", Integer.toString(position));
                    Log.d("You got", Integer.toString(recipes.get(position).getId()));
                    itemListener.onItemClick(position, recipes.get(position).getId());
                }
            });
        } else {
            holder.recipe.setText("No Recipes");
        }
    }

    public interface OnEditRecipeClickedListener {
        void onEditRecipeClick(int position, Recipe recipe);
    }

    void setFilteredRecipes(List<Recipe> filteredRecipes) {
        recipes = filteredRecipes;
        notifyDataSetChanged();
    }
    void setRecipe(List<Recipe> data){
        recipes = data;
        for( Recipe recipe: data){
            Log.d("loop data", recipe.getRecipeName());
        }
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
        ImageButton deleteButton, editRecipe;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recipe = itemView.findViewById(R.id.recipe_name);
            time = itemView.findViewById(R.id.recipe_cook_time);
            description = itemView.findViewById(R.id.recipe_description);
            deleteButton = itemView.findViewById(R.id.delete_recipe);
            editRecipe = itemView.findViewById(R.id.edit_recipe);
            imageView = itemView.findViewById(R.id.food_image);
        }
    }
}
