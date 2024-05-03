package com.mobileapp.cookie_jar;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
//import androidx.compose.runtime.external.kotlinx.collections.immutable.implementations.persistentOrderedMap.LinkedValue;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class recipeModel extends AndroidViewModel{

    RecipeDAO recipeDAO;
    RecipeDatabase recipeDB;
    LiveData<List<Recipe>> allRecipes;
    LiveData<List<String>> allURIs;

    public recipeModel(Application application) {
        super(application);

        recipeDB = RecipeDatabase.getInstance(application);
        recipeDAO = recipeDB.getRecipeDAO();
        allRecipes = recipeDAO.getAllRecipes();
        allURIs = recipeDAO.getAllURIs();
    }

    LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    void deleteRecipe(int id) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //background task
                recipeDAO.deleteRecipe(recipeDAO.getRecipe(id));
            }
        });
    }

    void refreshRecipes() {
        Log.d("refreshRecipes", "refreshed");
        allRecipes = recipeDAO.getAllRecipes();
    }
}
