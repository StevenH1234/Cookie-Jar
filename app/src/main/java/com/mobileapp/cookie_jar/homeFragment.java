package com.mobileapp.cookie_jar;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class homeFragment extends Fragment {
    ArrayList<recipeModel> homeRecipeModels = new ArrayList<recipeModel>();
    Recipe_RecyclerViewAdapater adapter;
    ImageButton add_button;
    Dialog mDialog;

    RecipeDatabase recipeDB;
    recipeModel recipeViewModel;
    Recipe_RecyclerViewAdapater recipeAdapter;

    EditText recipe_edit, description_edit, prep_time_edit, cook_time_edit, total_time_edit;
    Button next_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        add_button = view.findViewById(R.id.add_button);

        // pop out one: making recipe
        View popUpOne = inflater.inflate(R.layout.add_recipe_popup_1, null);
        recipe_edit = popUpOne.findViewById(R.id.recipe_edit);
        description_edit = popUpOne.findViewById(R.id.description_edit);
        prep_time_edit = popUpOne.findViewById(R.id.prep_time_edit);
        cook_time_edit = popUpOne.findViewById(R.id.cook_time_edit);
        total_time_edit = popUpOne.findViewById(R.id.total_time_edit);
        next_button = popUpOne.findViewById(R.id.save);

        mDialog = new Dialog(requireContext());

         //create recipe DB
        RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
        recipeDB = Room.databaseBuilder(requireContext(), RecipeDatabase.class, "recipeDB").addCallback(myCallBack).build();

        // set up recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new Recipe_RecyclerViewAdapater(getContext());
        recyclerView.setAdapter(adapter);

        // set up viewModel
        recipeViewModel = new ViewModelProvider(this).get(recipeModel.class);
        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    adapter.setRecipe(recipes);
                }
            }
        });

        // on click listener to add a recipe
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: using setCOntentView(popUpOne) makes the pop up smaller than using R.id.add_recipe_popup_1
                mDialog.setContentView(popUpOne);
                mDialog.show();
            }
        });

        // on click listener for navigating to the next pop-up
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertRecipeName = recipe_edit.getText().toString();
                String insertDescription = description_edit.getText().toString();
                String insertPrepTimeStr = prep_time_edit.getText().toString();
                int insertPrepTime = Integer.parseInt(insertPrepTimeStr);
                String insertCookTimeStr = cook_time_edit.getText().toString();
                int insertCookTime = Integer.parseInt(insertCookTimeStr);
                String insertTotalTimeStr = total_time_edit.getText().toString();
                int insertTotalTime = Integer.parseInt(insertTotalTimeStr);

                Recipe recipe1 = new Recipe(0,insertRecipeName,insertPrepTime, insertCookTime, insertTotalTime, insertDescription);
                addNewRecipe(recipe1);
                adapter.notifyDataSetChanged();
                mDialog.dismiss();
            }
        });
        return view;
    }

    // abstracted add function to interact with DAO
    public void addNewRecipe(Recipe recipe){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //background task
                recipeDB.getRecipeDAO().addRecipe(recipe);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Recipe> updatedRecipes = new ArrayList<>(adapter.recipes);
                        updatedRecipes.add(recipe);
                        adapter.setRecipe(updatedRecipes);
                        Toast.makeText(requireContext(), "added to DB", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}