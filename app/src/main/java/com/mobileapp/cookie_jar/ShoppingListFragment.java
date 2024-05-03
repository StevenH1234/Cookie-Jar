package com.mobileapp.cookie_jar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class ShoppingListFragment extends Fragment {

    RecyclerView Shopping_recyclerView;
    EditText addShoppingItemText;
    Button addShoppingItemButton, clearListButton;
    CheckBox checkBox;
    ArrayList<String> items;
    Shopping_RecyclerViewAdapter shoppingAdapter;
    RecipeDatabase recipeDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        Shopping_recyclerView = view.findViewById(R.id.shoppingListRecyclerView);
        addShoppingItemText = view.findViewById(R.id.edit_list_item);
        addShoppingItemButton = view.findViewById(R.id.add_list_item);
        clearListButton = view.findViewById(R.id.clear_all);
        items = new ArrayList<>();

        Shopping_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shoppingAdapter = new Shopping_RecyclerViewAdapter(items, getContext());
        Shopping_recyclerView.setAdapter(shoppingAdapter);

        recipeDB = Room.databaseBuilder(requireContext(), RecipeDatabase.class, "recipeDB").build();

        addShoppingItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertedItem = addShoppingItemText.getText().toString();

                addShoppingItem(insertedItem);
                addShoppingItemText.setText("");
            }
        });

        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingAdapter.clearList();
            }
        });


        return view;
    }

    public List<Ingredient> getRecipe(String name){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureString = executorService.submit(new Callable<List<Ingredient>>() {
            @Override
            public List<Ingredient> call() throws Exception {
                Recipe retrieved = recipeDB.getRecipeDAO().getRecipeByName(name);
                int retrievedID = retrieved.getId();
                List<Ingredient> retrievedList = recipeDB.getIngredientDAO().getIngredientsOfRecipe(retrievedID);
                if(retrieved == null){
                    return null;
                }
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
    void addShoppingItem(String item){
        List<Ingredient> recipeIngredients = getRecipe(item);
        if (!item.isEmpty() && recipeIngredients == null){
            items.add(item);
            shoppingAdapter.notifyItemInserted(items.size());
        } else {
            for(Ingredient ingredients : recipeIngredients){
                items.add(ingredients.getIngredientName());
                shoppingAdapter.notifyItemInserted(items.size());
            }
        }
    }
}