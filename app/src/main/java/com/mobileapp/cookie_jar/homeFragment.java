package com.mobileapp.cookie_jar;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.Manifest;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class homeFragment extends Fragment implements Recipe_RecyclerViewAdapater.OnDeleteClickListener,
        Recipe_RecyclerViewAdapater.OnEditRecipeClickedListener, Recipe_RecyclerViewAdapater.OnItemClickListener,
        ingredients_RecyclerViewAdapter.OnIngredientAddedListener, steps_RecyclerViewAdapter.OnStepsAddedListener {

    // global variables
    Recipe_RecyclerViewAdapater adapter;
    ingredients_RecyclerViewAdapter iAdapter;
    steps_RecyclerViewAdapter sAdapter;
    RecyclerView recyclerView, ingredients_RecyclerView, steps_RecyclerView;

    RecipeDatabase recipeDB;
    recipeModel recipeViewModel;
    Recipe toBeAddedRecipe, toBeEdittedRecipe;
    List<Ingredient> iAdapter_ingredient_list;
    List<Steps> sAdapter_ingredient_list;

    Button next_button, ingredient_next_button, steps_save_recipe;
    ImageButton add_button, add_additional_ingredient, add_additional_step,
            edit_image_add, delete_recipe, edit_recipe;
    EditText recipe_edit, description_edit, prep_time_edit, cook_time_edit, total_time_edit;
    View popUpOne, popUpTwo, popUpThree, popupOverview;
    ImageView edit_image;
    SearchView recipeSearchView;
    Dialog mDialog;

    Long inserted_recipe_id;
    ActivityResultLauncher<Intent> resultLauncher;
    String insertURI;
    Boolean edit_in_progress = false;
    int editing_Id;

    String[] permissions = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        add_button = view.findViewById(R.id.add_button);
        recipeSearchView = view.findViewById(R.id.recipeSearchView);

        // add variables
        List<Ingredient> ingredientsList = new ArrayList<>();
        List<Ingredient> editIngredientList = new ArrayList<>();
        List<Steps> stepsList = new ArrayList<>();
        List<Steps> editStepsList = new ArrayList<>();

        // set up dialogs
        popUpOne = inflater.inflate(R.layout.add_recipe_popup_1, null);
        popUpTwo = inflater.inflate(R.layout.add_recipe_popup_2, null);
        popUpThree = inflater.inflate(R.layout.add_recipe_popup_3, null);
        popupOverview = inflater.inflate(R.layout.overview_popup, null);

        // set up elements in dialogs
        edit_image_add = popUpOne.findViewById(R.id.edit_image_add);
        edit_image = popUpOne.findViewById(R.id.edit_image);
        recipe_edit = popUpOne.findViewById(R.id.recipe_edit);
        description_edit = popUpOne.findViewById(R.id.description_edit);
        prep_time_edit = popUpOne.findViewById(R.id.prep_time_edit);
        cook_time_edit = popUpOne.findViewById(R.id.cook_time_edit);
        total_time_edit = popUpOne.findViewById(R.id.total_time_edit);
        next_button = popUpOne.findViewById(R.id.save);
        ingredient_next_button = popUpTwo.findViewById(R.id.ingredient_next);
        steps_save_recipe = popUpThree.findViewById(R.id.save_recipe);
        mDialog = new Dialog(requireContext());

        // recipe cardviews
        View recipeCardView = inflater.inflate(R.layout.overview_recycler_view_row, null);
        delete_recipe = recipeCardView.findViewById(R.id.delete_recipe);
        edit_recipe = recipeCardView.findViewById(R.id.edit_recipe);
        edit_recipe.bringToFront();

         //create instance of recipe DB
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

        // set up recyclerviews
        recipeViewModel = new ViewModelProvider(this).get(recipeModel.class);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ingredients_RecyclerView = popUpTwo.findViewById(R.id.ingredientRecyclerView);
        steps_RecyclerView = popUpThree.findViewById(R.id.stepsRecyclerView);
        LinearLayoutManager ingredientsManager = new LinearLayoutManager(getContext());
        ingredients_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        steps_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // set up adapters for recycler views
        adapter = new Recipe_RecyclerViewAdapater(requireContext(), recipeViewModel);
        iAdapter_ingredient_list = new ArrayList<>();
        iAdapter = new ingredients_RecyclerViewAdapter(getContext(), iAdapter_ingredient_list);
        sAdapter_ingredient_list = new ArrayList<>();
        sAdapter = new steps_RecyclerViewAdapter(getContext(), sAdapter_ingredient_list);
        adapter.setOnItemClickListener(this);
        adapter.setOnDeleteClickListener(this);
        adapter.setOnEditRecipeClickListener(this);
        iAdapter.setOnIngredientAddedListener(this);
        sAdapter.setOnStepsAddedListener(this);
        ingredients_RecyclerView.setAdapter(iAdapter);
        steps_RecyclerView.setAdapter(sAdapter);

        //image adding {Request permission to access gallery}
        registerResult();
        if(ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(READ_MEDIA_IMAGES);
        } else {
            Log.d("Success", "permission Granted");
            getImagesInGallery();
            recyclerView.setAdapter(adapter);
        }

        // on click listener to add a recipe
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_in_progress = false;
                mDialog.setContentView(popUpOne);
                mDialog.show();
            }
        });

        // add an image from the phone
        edit_image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
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

                if(!edit_in_progress) {
                    Recipe recipe1 = new Recipe(0, insertRecipeName, insertPrepTime, insertCookTime, insertTotalTime, insertDescription, insertURI);
                    toBeAddedRecipe = recipe1;
                } else {
                    Recipe recipe2 = new Recipe(editing_Id, insertRecipeName, insertPrepTime, insertCookTime, insertTotalTime, insertDescription, insertURI);
                    toBeEdittedRecipe = recipe2;
                }

                Log.d("current recipe id is", String.valueOf(inserted_recipe_id));
                mDialog.dismiss();
                mDialog.setContentView(popUpTwo);
                mDialog.show();
                add_additional_ingredient = popUpTwo.findViewById(R.id.add_additional_ingredient);
                if(edit_in_progress){
                    List<Ingredient> currentIngredientList = getCurrentRecipeIngredients(editing_Id);
                    iAdapter.replaceList(currentIngredientList);
                }
                add_additional_ingredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iAdapter.update_amount();
                    }
                });

            }
        });

        // navigate to the next stage in adding recipes (move to add ingredients)
        ingredient_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eIngredient, eMeasurement;
                int eQuantity;
                if(iAdapter != null){
                    if(!edit_in_progress) {
                        for (Ingredient ingredients : iAdapter.getIngredientsList()) {
                            ingredientsList.add(ingredients);
                        }
                    } else {
                        for (Ingredient ingredients : iAdapter.getIngredientsList()) {
                            editIngredientList.add(ingredients);
                        }
                    }
                }
                mDialog.dismiss();
                mDialog.setContentView(popUpThree);
                mDialog.show();
                add_additional_step = popUpThree.findViewById(R.id.add_additional_step);
                if(edit_in_progress){
                    List<Steps> currentStepsList = getCurrentRecipeSteps(editing_Id);
                    sAdapter.replaceList(currentStepsList);
                }
                add_additional_step.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { sAdapter.update_amount(); }
                });
            }
        });

        // save recipe into the database
        steps_save_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sAdapter != null) {
                    if (!edit_in_progress) {
                        for (Steps step : sAdapter.getStepsList()) {
                            stepsList.add(step);
                        }
                        addNewDBEntry(toBeAddedRecipe, ingredientsList, stepsList);
                    } else {
                        for (Steps step : sAdapter.getStepsList()) {
                            editStepsList.add(step);
                        }
                        updateDBEntry(toBeEdittedRecipe, editIngredientList, editStepsList);
                    }
                }
                mDialog.dismiss();
            }
        });

        // search view implementation
        recipeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    adapter.setRecipe(adapter.getOGrecipes());
                } else {
                    filterList(newText);
                }
                return true;
            }

            private void filterList(String newText) {
                List<Recipe> filteredList = new ArrayList<>();
                for(Recipe recipe: adapter.recipes){
                    if (recipe.getRecipeName().toLowerCase().contains(newText.toLowerCase())){
                        filteredList.add(recipe);
                    }
                }

                if (filteredList.isEmpty()){
                    Log.d("Failed", "No Data Found");
                } else {
                    adapter.setFilteredRecipes(filteredList);
                }
            }
        });

        return view;
    }

    /* *
    * PreCondition: a valid recipe object, and list of Ingredient and Steps objects are
    * passed to the function
    * PostCondition: No return values. On the Background thread the program updates the
    * Recipe database to have the added parameters properly. Once complete a record of the
    * given recipe will exist in the database
    * */
    public void addNewDBEntry(Recipe recipe, List<Ingredient> ingredients, List<Steps> steps){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        AtomicLong recipe_id = new AtomicLong();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipe_id.set(recipeDB.getRecipeDAO().addRecipe(recipe));
                Log.d("added recipe is called", recipeDB.getRecipeDAO().getRecipe(recipe_id.intValue()).getRecipeName());
                for(Ingredient ingredient: ingredients){
                    ingredient.setRecipe_id((int)recipe_id.get());
                    recipeDB.getIngredientDAO().addIngredient(ingredient);
                }
                for(Steps step: steps){
                    step.setRecipe_id((int)recipe_id.get());
                    recipeDB.getStepsDAO().addSteps(step);
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Recipe> updatedRecipes = new ArrayList<>(adapter.recipes);
                        updatedRecipes.add(recipe);
                        adapter.setRecipe(updatedRecipes);
                        recipeViewModel.refreshRecipes();
                        for(Recipe recipes: adapter.getOGrecipes()){
                            Log.d("updated recipes list", recipes.getRecipeName());
                        }
                        Log.d("list updated", "here");
                        Toast.makeText(requireContext(), "added to DB", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /*
    * Precondition: A valid Recipe object and valid List of ingredient and step objects are passed
    * PostCondition: No return value. Using the provided parameters, the background thread will locate
    * the appropriate recipe with the matching id, and replace it with the values in the parameters and
    * update the database
    * */
    public void updateDBEntry(Recipe recipe, List<Ingredient> ingredients, List<Steps> steps) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipeDB.getRecipeDAO().updateRecipe(recipe);
                for(Ingredient ingredient: ingredients){
                    ingredient.setRecipe_id(editing_Id);
                    recipeDB.getIngredientDAO().updateIngredient(ingredient);
                }
                for(Steps step: steps){
                    step.setRecipe_id(editing_Id);
                    recipeDB.getStepsDAO().updateSteps(step);
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int edit_index = 0;
                        List<Recipe> tempRecipes = new ArrayList<>(adapter.recipes);
                        for(int i = 0; i < tempRecipes.size(); i++){
                            if(tempRecipes.get(i).getId() == editing_Id){
                                edit_index = i;
                                break;
                            }
                        }

                        tempRecipes.set(edit_index, recipe);
                        adapter.setRecipe(tempRecipes);
                        Toast.makeText(requireContext(), "updated to DB", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    /*
    * Precondition: A valid recipe id and position within a recycler view is passed to
    * the function to retrieve the appropriate information
    * PostCondition: A dialog box with the appropriate information (regarding recipe details,
    * ingredients, and steps is created and presented to the user. information is retrieved on
    * the background thread, while UI is updated on the main thread.
    * */
    @Override
    public void onItemClick(int position, int recipe_id){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //background task
                Recipe current_recipe = recipeDB.getRecipeDAO().getRecipe(recipe_id);
                ImageView overview_recipe_image = popupOverview.findViewById(R.id.overview_image);
                TextView overview_recipe_name = popupOverview.findViewById(R.id.overview_recipe_name);
                TextView overview_cook_time = popupOverview.findViewById(R.id.overview_cook_time);
                TextView overview_prep_time = popupOverview.findViewById(R.id.overview_prep_time);
                TextView overview_total_time = popupOverview.findViewById(R.id.overview_total_time);
                TextView overview_description = popupOverview.findViewById(R.id.overview_description_text);
                LinearLayout ingredients_layout = popupOverview.findViewById(R.id.overview_ingredients_layout);
                LinearLayout steps_layout = popupOverview.findViewById(R.id.overview_steps_layout);

                //overview_recipe_image.setImageURI(Uri.parse(current_recipe.getImageURI()));
                overview_recipe_name.setText(current_recipe.getRecipeName());
                overview_cook_time.setText(Html.fromHtml("<b>Cook Time: </b>" + current_recipe.getCookTime() + " Minutes"));
                overview_prep_time.setText(Html.fromHtml("<b>Prep Time: </b>" + current_recipe.getPrepTime() + " Minutes"));
                overview_total_time.setText(Html.fromHtml("<b>Total Time Spent: </b>" + current_recipe.getTotalTime() + " Minutes"));
                overview_description.setText(current_recipe.getDescription());

                ingredients_layout.removeAllViews();
                List<Ingredient> currentRecipeIngredients = recipeDB.getIngredientDAO().getIngredientsOfRecipe(recipe_id);
                for(Ingredient ingredient: currentRecipeIngredients){
                    TextView ingredientSlot = new TextView(getContext());
                    String quantity = Integer.toString(ingredient.getIngredientQuantity());
                    String measure = ingredient.getIngredientMeasure();
                    String name = ingredient.getIngredientName();
                    ingredientSlot.setText("    " + quantity + " " + measure + " " + name);
                    ingredientSlot.setLayoutParams(new ViewGroup.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    ingredients_layout.addView(ingredientSlot);
                }

                steps_layout.removeAllViews();
                List<Steps> currentRecipeSteps = recipeDB.getStepsDAO().getStepsOfRecipe(recipe_id);
                for(Steps step: currentRecipeSteps){
                    TextView stepSlot = new TextView(getContext());
                    stepSlot.setText("  " + step.getStepNo() + ". " + step.getStepDescription());
                    stepSlot.setLayoutParams(new ViewGroup.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    steps_layout.addView(stepSlot);
                }


                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });

        mDialog.setContentView(popupOverview);
        mDialog.show();
    }


    /*
    * Precondition: a valid position on the recipe recycler view is provided
    * PostCondition: the background UI thread will find the recipe associated with the
    * position in the recycler view, and remove it from the database before updating the UI
    * on the Main UI Thread.
    * */
    @Override
    public void onDeleteClick(int position){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //background task
                recipeDB.getRecipeDAO().deleteRecipe(adapter.recipes.get(position));
                for(int i = 0; i < adapter.recipes.size(); i++){
                    Log.d("recipe", adapter.recipes.get(i).recipeName);
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Recipe> updatedRecipes = new ArrayList<>(adapter.recipes);
                        updatedRecipes.remove(position);
                        adapter.setRecipe(updatedRecipes);
                        Toast.makeText(requireContext(), "removed from DB", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*
     * Precondition: a valid position of a recipe card in the recycler view is provided and, the
     * associated recipe with that position is provided
     * PostCondition: opens the same dialog box that "create recipe" opens, but it is prepopulated
     * with information from the chosen recipe. Initiates the same recipe creation process but with
     * the boolean editable set to true, meaning everything saved will later change the values in the
     * database.
     * */
    @Override
    public void onEditRecipeClick(int position, Recipe recipe){
        mDialog.setContentView(popUpOne);
        recipe_edit.setText(recipe.getRecipeName());
        description_edit.setText(recipe.getDescription());
        prep_time_edit.setText(String.valueOf(recipe.getPrepTime()));
        cook_time_edit.setText(String.valueOf(recipe.getCookTime()));
        total_time_edit.setText(String.valueOf(recipe.getTotalTime()));
        editing_Id = recipe.getId();
        edit_in_progress = true;
        mDialog.show();
    }

    /*
     * Precondition: a valid ingredient objecct is passed as a parameter
     * PostCondition: ingredient recycler view adapter adds the ingredient
     * to its inner List of ingredients.
     * */
    @Override
    public void onIngredientAdded(Ingredient ingredient) {
        iAdapter.updateIngredientsList(ingredient);
    }

    /*
     * Precondition: a valid step objecct is passed as a parameter
     * PostCondition: steps recycler view adapter adds the step
     * to its inner List of ingredients.
     * */
    @Override
    public void onStepsAdded(Steps step) {
        sAdapter.updateStepsList(step);
    }

    /*
     * Precondition: given a valid recipeID corresponding to a recipe in the database
     * PostCondition: returns the list of ingredients associated with the correspinding
     * recipeID, this occurs on a background thread to avoid freezing the main thread
     * */
    public List<Ingredient> getCurrentRecipeIngredients(int recipeId){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureString = executorService.submit(new Callable<List<Ingredient>>() {
            @Override
            public List<Ingredient> call() throws Exception {
                List<Ingredient> retrievedList = recipeDB.getIngredientDAO().getIngredientsOfRecipe(recipeId);
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

    /*
     * Precondition: given a valid recipeID corresponding to a recipe in the database
     * PostCondition: returns the list of Steps associated with the correspinding
     * recipeID, this occurs on a background thread to avoid freezing the main thread
     * */
    public List<Steps> getCurrentRecipeSteps(int recipeId){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Steps>> futureSteps = executorService.submit(new Callable<List<Steps>>() {
            @Override
            public List<Steps> call() throws Exception {
                List<Steps> retrievedList = recipeDB.getStepsDAO().getStepsOfRecipe(recipeId);
                return retrievedList;
            }
        });

        try {
            return futureSteps.get();
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipess) {
                adapter.setRecipe(recipess);
                for(int i = 0; i < adapter.getItemCount(); i++){
                    Log.d("in onchanged the recipes are", adapter.getOGrecipes().get(i).recipeName);
                }
                adapter.notifyDataSetChanged();
                Log.d("on changed", "things changed");
            }
        });
    }

    /*
     * Precondition: None
     * PostCondition: creates a new intent to pick an image from the imagestore on the phone
     * */
    void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    /*
     * Precondition: Permission from the phone to access external storage is granted
     * PostCondition: the URI of a given image in the phones external image storage
     * is saved to the global variable
     * */
    void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            ArrayList<String> imageList = new ArrayList<>();
                            Uri imageUri = result.getData().getData();
                            edit_image.setImageURI(imageUri);
                            insertURI = imageUri.toString();
                        } catch (Exception e){
                            Log.d("error", "error");
                        }
                    }
                }
        );
    }

    /*
     * Precondition: permission to access the phones external image storage is provided
     * PostCondition: URIs from all images in the phones external image storage are retrieved
     * */
    public void getImagesInGallery() {

        ArrayList<String> images = new ArrayList<>();
        String imagePath;
        int Index;
        String[] columns = {MediaStore.Images.Media.DATA};

        Cursor Cursor = requireContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null
        );

        if (Cursor != null) {
            while (Cursor.moveToNext()) {
                Index = Cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imagePath = Cursor.getString(Index);
                images.add(imagePath);
            }
            Cursor.close();
        }
        adapter.setURIpaths(images);
    }

    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted){
                        recyclerView.setAdapter(adapter);
                        Log.d("Success", "permission Granted");
                    } else {
                        Log.d("Failed", "permission denied");
                    }
                });


}