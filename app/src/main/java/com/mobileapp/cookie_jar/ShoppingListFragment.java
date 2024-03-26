package com.mobileapp.cookie_jar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

    RecyclerView Shopping_recyclerView;
    EditText addShoppingItemText;
    Button addShoppingItemButton;
    ArrayList<String> items;
    Shopping_RecyclerViewAdapter shoppingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        Shopping_recyclerView = view.findViewById(R.id.shoppingListRecyclerView);
        addShoppingItemText = view.findViewById(R.id.edit_list_item);
        addShoppingItemButton = view.findViewById(R.id.add_list_item);
        items = new ArrayList<>();

        Shopping_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shoppingAdapter = new Shopping_RecyclerViewAdapter(items, getContext());
        Shopping_recyclerView.setAdapter(shoppingAdapter);



        addShoppingItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addShoppingItem(addShoppingItemText.getText().toString());
            }
        });

        return view;
    }
    void addShoppingItem(String item){
        if (!item.isEmpty()){
            items.add(item);
            shoppingAdapter.notifyItemInserted(items.size()-1);
        }
    }
}