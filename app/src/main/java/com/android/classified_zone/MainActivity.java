package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    Button geo_view, confidential, user_tracking;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.bottom_navigation);
        geo_view = findViewById(R.id.scan2);
        confidential = findViewById(R.id.scan4);
        user_tracking = findViewById(R.id.user_tracking);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);





        geo_view .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, geo_tag_loc_non_communicable.class);
                startActivity(i);
            }
        });
        user_tracking .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, user_tracking.class);
                startActivity(i);
            }
        });

        confidential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, geotag_location.class);
                startActivity(i);
            }
        });


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

                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(MainActivity.this, user_tracking.class);
                        startActivity(amphibiansActivityIntentssss);


                        break;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.summary:
                Intent amphibiansActivityIntents = new Intent(MainActivity.this, fire_to_pdf.class);
                startActivity(amphibiansActivityIntents);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}