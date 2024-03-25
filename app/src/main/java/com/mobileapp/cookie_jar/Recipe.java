package com.mobileapp.cookie_jar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @ColumnInfo(name = "recipe_id")
    @PrimaryKey(autoGenerate = true)
    public int id;

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

    public Recipe(int id, String recipeName, int prepTime,
                  int cookTime, int totalTime, String description) {
        this.id = id;
        this.recipeName = recipeName;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.totalTime = totalTime;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

@Entity
class Ingredient {
    @ColumnInfo(name = "ingredient_id")
    @PrimaryKey(autoGenerate = true)
    public int ingredient_id;

    @ColumnInfo(name = "ingredient_name")
    public String ingredientName;

    public Ingredient(int ingredient_id, String ingredientName) {
        this.ingredient_id = ingredient_id;
        this.ingredientName = ingredientName;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}


@Entity
class Steps {
    @ColumnInfo(name = "recipe_id")
    @PrimaryKey(autoGenerate = false)
    public int recipeId;

    @ColumnInfo(name = "step_no")
    public int stepNo;
    @ColumnInfo(name = "step_description")
    public String stepDescription;

    public Steps(int recipeId, int stepNo, String stepDescription) {
        this.recipeId = recipeId;
        this.stepNo = stepNo;
        this.stepDescription = stepDescription;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

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
