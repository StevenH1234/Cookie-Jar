package com.mobileapp.cookie_jar;

import android.app.Application;

import androidx.annotation.NonNull;
//import androidx.compose.runtime.external.kotlinx.collections.immutable.implementations.persistentOrderedMap.LinkedValue;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class recipeModel extends AndroidViewModel{

    RecipeDAO recipeDAO;
    RecipeDatabase recipeDB;
    LiveData<List<Recipe>> allRecipes;

    public recipeModel(Application application) {
        super(application);

        recipeDB = RecipeDatabase.getInstance(application);
        recipeDAO = recipeDB.getRecipeDAO();
        allRecipes = recipeDAO.getAllRecipes();
    }

    LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }
}
