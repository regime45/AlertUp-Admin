package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class list_of_disease extends AppCompatActivity {

    RecyclerView recview;
    list_of_disease_adapter disease_adapter;
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_disease);
        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));



        FirebaseRecyclerOptions<geo_model> options =
                new FirebaseRecyclerOptions.Builder<geo_model>()
                        // .setQuery(FirebaseDatabase.getInstance().getReference().child("uploads"), geo_model.class)
                        .setQuery(FirebaseDatabase.getInstance().getReference("alerts_zone").child("list_of_disease").orderByChild("disease_name"), geo_model.class)
                        .build();

        disease_adapter = new list_of_disease_adapter(options);
        recview.setAdapter(disease_adapter);

        navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.view:
                        Intent amphibiansActivityIntent = new Intent(list_of_disease.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntent);

                        break;

                    case R.id.edits:
                        Intent amphibiansActivityIntentss = new Intent(list_of_disease.this,editActivity.class);
                        startActivity(amphibiansActivityIntentss);


                        break;

                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(list_of_disease.this, MainActivity.class);
                        startActivity(amphibiansActivityIntentssss);


                        break;

                    case R.id.info:
                        Intent amphibiansActivityIntents = new Intent(list_of_disease.this, Report_Activity.class);
                        startActivity(amphibiansActivityIntents);


                        break;

                    case R.id.creates:
                        Intent amphibiansActivityIntentsss = new Intent(list_of_disease.this, list_of_disease.class);
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
        inflater.inflate(R.menu.add_disease, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.add:
                Intent amphibiansActivityIntents = new Intent(list_of_disease.this, select_disease.class);
                startActivity(amphibiansActivityIntents);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        disease_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        disease_adapter.stopListening();
    }

}