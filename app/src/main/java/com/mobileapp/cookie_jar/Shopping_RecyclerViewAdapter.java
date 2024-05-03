package com.mobileapp.cookie_jar;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//adapted from GeeksforGeeks code

public class Shopping_RecyclerViewAdapter extends RecyclerView.Adapter<Shopping_RecyclerViewAdapter.ViewHolder> {
    ArrayList<String> ShoppingItemList;

    public Shopping_RecyclerViewAdapter(ArrayList<String> shoppingItemList, Context context){
        this.ShoppingItemList = shoppingItemList;
    }

    @NonNull
    @Override
    public Shopping_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Shopping_RecyclerViewAdapter.ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();
        holder.shoppingItem.setText(ShoppingItemList.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.shoppingItem.setPaintFlags(holder.shoppingItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.shoppingItem.setPaintFlags(holder.shoppingItem.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (ShoppingItemList) {
                    if (currentPosition >= 0 && currentPosition < ShoppingItemList.size()) {
                        ShoppingItemList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, ShoppingItemList.size());
                    }
                }
            }
        });
    }

    public void setList(ArrayList<String> data){
        ShoppingItemList = data;
        notifyDataSetChanged();
    }

    public void clearList(){
        ShoppingItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ShoppingItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shoppingItem;
        CheckBox checkBox;
        ImageButton imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingItem = itemView.findViewById(R.id.shoppingListItem);
            checkBox = itemView.findViewById(R.id.clear_item);
            imageButton = itemView.findViewById(R.id.remove_item);
        }
    }
}
