package com.mobileapp.cookie_jar;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Insert
    public void addRecipe(Recipe recipe);
    @Update
    public void updateRecipe(Recipe recipe);
    @Delete
    public void deleteRecipe(Recipe recipe);

    @Query("select * from recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("select * from recipe where recipe_id==:Recipe_id")
    public Recipe getRecipe(int Recipe_id);
}

@Dao
interface IngredientDAO {
    @Insert
    public void addIngredient(Ingredient ingredient);
    @Update
    public void updateIngredient(Ingredient ingredient);
    @Delete
    public void deleteIngredient(Ingredient ingredient);

    @Query("select * from ingredient")
    public List<Ingredient> getAllIngredients();

    @Query("select * from ingredient where ingredient_name==:Ingredient_name")
    public Ingredient getIngredient(String Ingredient_name);
}

@Dao
interface StepsDAO {
    @Insert
    public void addSteps(Steps steps);
    @Update
    public void updateSteps(Steps steps);
    @Delete
    public void deleteSteps(Steps step);

    @Query("select * from steps")
    public List<Steps> getAllSteps();

    @Query("select * from steps where recipe_id==:Recipe_id")
    public Steps getSteps(int Recipe_id);
}