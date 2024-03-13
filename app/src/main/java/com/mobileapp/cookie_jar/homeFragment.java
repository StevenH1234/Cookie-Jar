package com.mobileapp.cookie_jar;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class homeFragment extends Fragment {
    ArrayList<recipeModel> homeRecipeModels = new ArrayList<recipeModel>();
    ImageButton add_button;
    Dialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        add_button = view.findViewById(R.id.add_button);
        mDialog = new Dialog(requireContext());

        // on click listener to add a recipe
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.setContentView(R.layout.add_recipe_popup_1);
                mDialog.show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        //TODO: setup models temporarily hardcoded
        String[] recipeNames = getResources().getStringArray(R.array.Recipe_name);
        String[] recipeTimes = getResources().getStringArray(R.array.Recipe_time_mins);
        String[] recipeDescription = getResources().getStringArray(R.array.recipe_description);
        int[] recipeImages = {R.drawable.cake_placeholder};
        homeRecipeModels.add(new recipeModel(recipeNames[0], recipeTimes[0], recipeDescription[0], recipeImages[0]));
        //TODO: check if this is adding to the recipeModels

        Recipe_RecyclerViewAdapater adapter = new Recipe_RecyclerViewAdapater(getContext(), homeRecipeModels);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}