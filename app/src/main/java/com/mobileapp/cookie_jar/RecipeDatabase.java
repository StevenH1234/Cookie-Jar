package com.mobileapp.cookie_jar;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, Ingredient.class, Steps.class},version = 7)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDAO getRecipeDAO();
    public abstract IngredientDAO getIngredientDAO();
    public abstract StepsDAO getStepsDAO();
    private static volatile RecipeDatabase INSTANCE;

    static RecipeDatabase getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, "recipeDB").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}