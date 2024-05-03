package com.mobileapp.cookie_jar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MysteryBoxFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mystery_box, container, false);

        List<String> hackedList = Arrays.asList("bread","Cherries", "Mango", "Potatoes", "Salmon","asparagus", "white Chocolate", "filet mignon", "tomatoes",
                "coconut", "flour", "water", "Cherries");

        FloatingActionButton randomizer = view.findViewById(R.id.MysteryBox);
        EditText randomNum = view.findViewById(R.id.Random_Gen);
        LinearLayout randomIngredients = view.findViewById(R.id.randomIngredients);

        /*
         * Precondition: None
         * PostCondition: A List of n given Ingredients will be displayed to the screen
         * */
        randomizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randNumAsString = Integer.parseInt(randomNum.getText().toString());
                for(int i = 0; i < randNumAsString; i++){
                    TextView newTextViews = new TextView(getContext());
                    newTextViews.setText(hackedList.get(i));
                    newTextViews.setGravity(Gravity.CENTER);
                    randomIngredients.addView(newTextViews);
                }

            }
        });


        return view;
    }
}