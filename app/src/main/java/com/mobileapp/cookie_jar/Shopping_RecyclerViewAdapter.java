package com.mobileapp.cookie_jar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.shoppingItem.setText(ShoppingItemList.get(position));
    }

    public void setList(ArrayList<String> data){
        ShoppingItemList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ShoppingItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shoppingItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingItem = itemView.findViewById(R.id.shoppingListItem);
        }
    }
}
