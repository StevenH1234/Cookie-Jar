package com.mobileapp.cookie_jar;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecipeCookWithMeFragment extends Fragment {
    SearchView search;
    Button prevButton, nextButton;
    RecipeDatabase recipeDB;
    List<Steps> stepsToShow;
    int index = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_cook_with_me, container, false);
        // Inflate the layout for this fragment

        TextView current_step = view.findViewById(R.id.current_step);
        search = view.findViewById(R.id.search_cook_with_me);
        prevButton = view.findViewById(R.id.prev_step_button);
        nextButton = view.findViewById(R.id.next_step_button);

        recipeDB = Room.databaseBuilder(requireContext(), RecipeDatabase.class, "recipeDB").build();
        Log.d("in the cook", "Cook with me");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("onQueryTextSubmit", "query");
                Recipe found = getRecipe(query);
                int foundID = found.getId();
                stepsToShow = getRecipeSteps(foundID);
                current_step.setText(stepsToShow.get(index).getStepDescription());
                nextButton.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index += 1;
                prevButton.setVisibility(View.VISIBLE);
                if(index < stepsToShow.size()) {
                    current_step.setText(stepsToShow.get(index).getStepDescription());
                } else {
                    current_step.setText("Recipe Complete!");
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index -= 1;
                if(index > 0){
                    current_step.setText(stepsToShow.get(index).getStepDescription());
                } else {
                    current_step.setText(stepsToShow.get(0).getStepDescription());
                    prevButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
    }

    public Recipe getRecipe(String name){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Recipe> futureString = executorService.submit(new Callable<Recipe>() {
            @Override
            public Recipe call() throws Exception {
                Recipe retrieved = recipeDB.getRecipeDAO().getRecipeByName(name);
                if(retrieved == null){
                    return null;
                }
                return retrieved;
            }
        });

        try {
            return futureString.get();
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }

    public List<Steps> getRecipeSteps(int recipeID){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Steps>> futureString = executorService.submit(new Callable<List<Steps>>() {
            @Override
            public List<Steps> call() throws Exception {
                List<Steps> retrievedList = recipeDB.getStepsDAO().getStepsOfRecipe(recipeID);
                return retrievedList;
            }
        });

        try {
            return futureString.get();
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }
}