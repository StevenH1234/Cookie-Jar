package com.mobileapp.cookie_jar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Recipe {
    @ColumnInfo(name = "recipe_id")
    @PrimaryKey(autoGenerate = true)
    public int recipe_id;

    @ColumnInfo(name = "recipe_name")
    public String recipeName;
    @ColumnInfo(name = "prep_time")
    public int prepTime;
    @ColumnInfo(name = "cook_time")
    public int cookTime;
    @ColumnInfo(name = "total_time")
    public int totalTime;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "imageURI")
    public String imageURI;

    public Recipe(int recipe_id, String recipeName, int prepTime,
                  int cookTime, int totalTime, String description, String imageURI) {
        this.recipe_id = recipe_id;
        this.recipeName = recipeName;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.totalTime = totalTime;
        this.description = description;
        this.imageURI = imageURI;
    }

    public int getId() {
        return recipe_id;
    }

    public void setId(int id) {
        this.recipe_id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURI(){ return imageURI; }

    public void setImageURI(String imageURI){ this.imageURI = imageURI; }
}

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = ForeignKey.CASCADE))

class Ingredient {
    @ColumnInfo(name = "recipe_id")
    @NonNull
    public int recipe_id;
    @ColumnInfo(name = "ingredient_id")
    @PrimaryKey(autoGenerate = true)
    public int ingredient_id;

    @ColumnInfo(name = "ingredient_name")
    public String ingredientName;
    @ColumnInfo(name = "ingredient_quantity")
    public int ingredientQuantity;
    @ColumnInfo(name = "ingredient_measure")
    public String ingredientMeasure;

    public Ingredient(int recipe_id, int ingredient_id, String ingredientName, int ingredientQuantity, String ingredientMeasure) {
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMeasure = ingredientMeasure;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public int getRecipe_id(){ return recipe_id; }

    public void setRecipe_id(int recipe_id){ this.recipe_id = recipe_id; }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(int ingredientQuantity) { this.ingredientQuantity = ingredientQuantity; }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public void setIngredientMeasure(String ingredientMeasure) { this.ingredientMeasure = ingredientMeasure; }
}


@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = ForeignKey.CASCADE))

class Steps {
    @ColumnInfo(name = "recipe_id")
    @NonNull
    public int recipe_id;
    @PrimaryKey(autoGenerate = true)
    public int step_id;
    @ColumnInfo(name = "step_no")
    public int stepNo;
    @ColumnInfo(name = "step_description")
    public String stepDescription;

    public Steps(int recipe_id, int step_id, int stepNo, String stepDescription) {
        this.recipe_id = recipe_id;
        this.step_id = step_id;
        this.stepNo = stepNo;
        this.stepDescription = stepDescription;
    }

    public int getRecipeId() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id){ this.recipe_id = recipe_id; }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }
}
