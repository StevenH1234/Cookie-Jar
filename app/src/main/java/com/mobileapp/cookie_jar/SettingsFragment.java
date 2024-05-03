package com.mobileapp.cookie_jar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsFragment extends Fragment {

    RadioGroup radioHolder;
    RadioButton theme1, theme2;
    LinearLayout helpLinear, aboutLinear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        helpLinear = view.findViewById(R.id.help_linear_Layout);
        aboutLinear = view.findViewById(R.id.about_linear_layout);
        helpLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.helpFragment);
            }
        });

        aboutLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.aboutFragment);
            }
        });
        return view;
    }
}