package com.mobileapp.cookie_jar;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ingredients_RecyclerViewAdapter extends RecyclerView.Adapter<ingredients_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    int ingredient_count;
    List<Ingredient> ingredient_list;
    OnIngredientAddedListener listener;

    /*
     * Precondition: a valid context for the constructor is passed, as well as an
     * initial list for the ingredient list member
     * PostCondition: an ingredient_recyclerView object is created
     * */
    public ingredients_RecyclerViewAdapter(Context context, List<Ingredient> ingredient_list){
        this.context = context;
        this.ingredient_count = 0;
        this.ingredient_list = ingredient_list;
    }

    /*
     * Precondition: valid listener is passed, often a fragment itself
     * PostCondition: listener member is set
     * */
    public void setOnIngredientAddedListener(OnIngredientAddedListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ingredients_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_ingredient_recycler_view_row, parent, false);
        return new ingredients_RecyclerViewAdapter.MyViewHolder(view, listener);
    }

    /*
     * Precondition: a valid holder, and position in the recycler view is provided
     * PostCondition: for each holder, information is bound to the view and populated
     * */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("Current position of holder is", Integer.toString(position));
        Log.d("Current ingred size", Integer.toString(ingredient_list.size()));
        if (position < ingredient_list.size()) {
            Ingredient current_ingredient = ingredient_list.get(position);
            holder.ingredients_label.setText("Ingredient " + Integer.toString(position + 1));
            holder.ingredient_title.setText(current_ingredient.getIngredientName());

            holder.ingredient_quantity.setText(String.valueOf(current_ingredient.getIngredientQuantity()));
            if ("cups".equals(current_ingredient.getIngredientMeasure())) {
                holder.ingredient_measure.setSelection(1);
            } else if ("ounces".equals(current_ingredient.getIngredientMeasure())) {
                holder.ingredient_measure.setSelection(2);
            } else if ("grams".equals(current_ingredient.getIngredientMeasure())) {
                holder.ingredient_measure.setSelection(3);
            } else if ("tsp".equals(current_ingredient.getIngredientMeasure())) {
                holder.ingredient_measure.setSelection(4);
            } else if ("tbsp".equals(current_ingredient.getIngredientMeasure())) {
                holder.ingredient_measure.setSelection(5);
            } else if ("count".equals(current_ingredient.getIngredientMeasure())) {
                holder.ingredient_measure.setSelection(6);
            } else {
                holder.ingredient_measure.setSelection(0);
            }
        } else {
            Log.d("creating blank", "blank");
            holder.ingredients_label.setText("Ingredient " + Integer.toString(position + 1));
            holder.ingredient_title.setText("");
            holder.ingredient_quantity.setText("");
            holder.ingredient_measure.setSelection(0);
        }

    }

    /*
     * Precondition: None
     * PostCondition: active ingredient list is returned
     * */
    public List<Ingredient> getIngredientsList(){
        return ingredient_list;
    }

    /*
     * Precondition: None
     * PostCondition: size of the recycler view is incremented by one
     * to create a new holder
     * */
    public void update_amount(){
        ingredient_count += 1;
        notifyDataSetChanged();
    }

    /*
     * Precondition: None
     * PostCondition: item count of the ingredient recycler view is returned
     * */
    @Override
    public int getItemCount() {
        return ingredient_count;
    }

    /*
     * Precondition: valid list of ingredient objects is passed
     * PostCondition: the current list of ingredients in the recycler view is
     * replaced with the new list that gets passed in.
     * */
    public void replaceList(List<Ingredient> newIngredients){
        this.ingredient_list = newIngredients;
        ingredient_count = newIngredients.size();
        notifyDataSetChanged();
    }

    /*
     * Precondition: Valid Ingredient object is passed as a parameter
     * PostCondition: the new ingredient is added to the existing list
     * of ingredients held by the recycler view
     * */
    public void updateIngredientsList(Ingredient newIngredient) {
        //check for existence before adding
        boolean exists = false;
        for (Ingredient ingredients : ingredient_list){
            if(ingredients.getIngredientName().equals(newIngredient.getIngredientName())){
                Log.d("Existence", "Ingredient already exists");
                exists = true;
                break;
            }
        }
        if (!exists){
            ingredient_list.add(newIngredient);
        }
        for (Ingredient ingredients : ingredient_list){
            Log.d("current ingredient List", ingredients.getIngredientName());
        }
        notifyDataSetChanged();
    }

    public interface OnIngredientAddedListener {
        void onIngredientAdded(Ingredient ingredient);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ingredients_label;
        EditText ingredient_title;
        EditText ingredient_quantity;
        Spinner ingredient_measure;
        public MyViewHolder(@NonNull View itemView, OnIngredientAddedListener listener) {
            super(itemView);
            ingredients_label = itemView.findViewById(R.id.ingredient_label);
            ingredient_title = itemView.findViewById(R.id.edit_ingredient);
            ingredient_quantity = itemView.findViewById(R.id.edit_quantity);
            ingredient_measure = itemView.findViewById(R.id.edit_measurement);

            ingredient_measure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(id != 0){
                        String title = ingredient_title.getText().toString();
                        int quantity = 0;
                        String measure = ingredient_measure.getSelectedItem().toString();
                        Log.d("Selected Item", measure);

                        if (!(ingredient_quantity.getText().toString().isEmpty())){
                            quantity = Integer.valueOf(ingredient_quantity.getText().toString());
                        }

                        Ingredient insertIngredient = new Ingredient(0,0, title, quantity, measure);
                        listener.onIngredientAdded(insertIngredient);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });


        }
    }
}
