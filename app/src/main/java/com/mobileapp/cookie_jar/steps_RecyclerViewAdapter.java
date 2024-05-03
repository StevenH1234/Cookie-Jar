package com.mobileapp.cookie_jar;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//TODO: fix the edittexts for the steps so that they dont disappear after adding a new one

public class steps_RecyclerViewAdapter extends RecyclerView.Adapter<steps_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    int step_count;
    List<Steps> listOfSteps;
    OnStepsAddedListener listener;
    Boolean isEditing;

    public steps_RecyclerViewAdapter(Context context, List<Steps> listOfSteps){
        this.context = context;
        this.step_count = 0;
        this.listOfSteps = listOfSteps;
    }

    public void setOnStepsAddedListener(steps_RecyclerViewAdapter.OnStepsAddedListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public steps_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_steps_recycler_view_row, parent, false);
        return new steps_RecyclerViewAdapter.MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull steps_RecyclerViewAdapter.MyViewHolder holder, int position) {
        if(position < listOfSteps.size()){
            Steps current_step = listOfSteps.get(position);
            holder.step_no.setText(Integer.toString(current_step.getStepNo()));
            holder.description.setText(current_step.getStepDescription());
            Log.d("if", "in the bind view if block");
        } else {
            holder.step_no.setText(Integer.toString(position+1));
            holder.description.setText("");
            Log.d("else", "in the bind view else block");
        }
    }

    public void update_amount(){
        step_count += 1;
        notifyDataSetChanged();
    }

    public List<Steps> getStepsList(){
        return listOfSteps;
    }
    public void updateStepsList(Steps newStep){
        boolean exists = false;
        for (Steps step : listOfSteps){
            if(step.getStepDescription().equals(newStep.getStepDescription())){
                Log.d("Existence", "Step already exists");
                exists = true;
                break;
            }
        }
        if (!exists){
            listOfSteps.add(newStep);
            notifyDataSetChanged();
        }
        for (Steps step: listOfSteps){
            Log.d("current steps List", step.getStepDescription());
        }
    }

    public interface OnStepsAddedListener {
        void onStepsAdded(Steps step);
    }

    @Override
    public int getItemCount() { return step_count; }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView step_no;
        EditText description;
        TextWatcher textWatcher;
        public MyViewHolder(@NonNull View itemView, steps_RecyclerViewAdapter.OnStepsAddedListener listener) {
            super(itemView);

            step_no = itemView.findViewById(R.id.step_no);
            description = itemView.findViewById(R.id.edit_step_description);

            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    int step_number = position + 1;
                    String step_description = s.toString();
                    Log.d("Step no", Integer.toString(step_number));
                    Log.d("description", step_description);
                    if(!(step_description.isEmpty())) {
                        Steps insertStep = new Steps(0, 0, step_number, step_description);
                        listener.onStepsAdded(insertStep);
                        Log.d("afterTextChanged", "addded");
                        description.removeTextChangedListener((TextWatcher) description.getTag());
                        description.setTag(null);
                    }
                }
            };

            description.addTextChangedListener(textWatcher);
            description.setTag(textWatcher);
        }
    }

    public void replaceList(List<Steps> newSteps){
        this.listOfSteps = newSteps;
        step_count = newSteps.size();
        notifyDataSetChanged();
    }

}
