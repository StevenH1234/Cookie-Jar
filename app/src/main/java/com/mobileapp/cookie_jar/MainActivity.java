package com.mobileapp.cookie_jar;

/*
Mobile App Development II -- COMP.4631 Honor Statement
The practice of good ethical behavior is essential for maintaining good order in the classroom, providing an enriching
learning experience for students, and training as a practicing computing professional upon graduation. This practice
is manifested in the University's Academic Integrity policy. Students are expected to strictly avoid academic dishonesty
and adhere to the Academic Integrity policy as outlined in the course catalog. Violations will be dealt with as outlined
therein. All programming assignments in this class are to be done by the student alone unless otherwise specified. No
outside help is permitted except the instructor and approved tutors.

I certify that the work submitted with this assignment is mine and was generated in a manner consistent with this document,
the course academic policy on the course website on Blackboard, and the UMass Lowell academic code.

Coding Resources Utilized:
- RoomDatabase Android Studio - https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase
- General Questions StackOverflow - https://stackoverflow.com/
- How to Update RecyclerView Adapter Data in Android? - https://www.geeksforgeeks.org/how-to-update-recyclerview-adapter-data-in-android/
- SearchView with RecyclerView - Android Studio Tutorial (2022) - codingSTUFF - https://www.youtube.com/watch?v=tQ7V7iBg5zE&t=303s
- Android Room Database Tutorial: Fetch & Display Data from ROOM in RecyclerView #4.5 - Smartherd - https://www.youtube.com/watch?v=VrP_GrgWzyo
- Permissions and Threading help - Stack Overflow, Medium.com, and GeeksForGeeks

Date: 03/26/24
Name: Steven Huynh
*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Button add_button;
    EditText recipe_edit, description_edit, prep_time_edit, cook_time_edit, total_time_edit;
    Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation Host
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        //set the toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create an "appBarConfiguration" object
        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(navController.getGraph());
        AppBarConfiguration appBarConfiguration = builder.build();

        //link to toolbar
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        // link bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // navigate to the cook with me fragment
        ImageButton imageButton = findViewById(R.id.cook_with_me_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.recipeCookWithMeFragment);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}