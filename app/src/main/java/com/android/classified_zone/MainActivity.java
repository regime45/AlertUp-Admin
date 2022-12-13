package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.view:
                        Intent amphibiansActivityIntent = new Intent(MainActivity.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntent);

                        break;

                    case R.id.edits:
                        Intent amphibiansActivityIntentss = new Intent(MainActivity.this,editActivity.class);
                        startActivity(amphibiansActivityIntentss);


                        break;
/*
                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(amphibiansActivityIntentssss);


                        break;
*/
                    case R.id.info:
                        Intent amphibiansActivityIntents = new Intent(MainActivity.this, Report_Activity.class);
                        startActivity(amphibiansActivityIntents);


                        break;

                    case R.id.creates:
                        Intent amphibiansActivityIntentsss = new Intent(MainActivity.this, list_of_disease.class);
                        startActivity(amphibiansActivityIntentsss);


                        break;





                }

                return true;
            }
        });
    }

}