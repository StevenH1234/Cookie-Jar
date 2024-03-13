package com.mobileapp.cookie_jar;

public class recipeModel {
    String recipeName;
    String recipeCookTime;
    String recipeDescription;
    int recipeImage;

    public recipeModel(String recipeName, String recipeCookTime,
                       String recipeDescription, int recipeImage) {
        this.recipeName = recipeName;
        this.recipeCookTime = recipeCookTime;
        this.recipeDescription = recipeDescription;
        this.recipeImage = recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeCookTime() {
        return recipeCookTime;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public int getRecipeImage() {
        return recipeImage;
    }
}
