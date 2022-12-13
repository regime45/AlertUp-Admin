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

public class editActivity extends AppCompatActivity {
    RecyclerView recview;
    geo_adapter adapter;
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<geo_model> options =
                new FirebaseRecyclerOptions.Builder<geo_model>()
                        // .setQuery(FirebaseDatabase.getInstance().getReference().child("uploads"), geo_model.class)
                        .setQuery(FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone"), geo_model.class)
                        .build();

        adapter = new geo_adapter(options);
        recview.setAdapter(adapter);

        navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.view:
                        Intent amphibiansActivityIntent = new Intent(editActivity.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntent);

                        break;

                    case R.id.edits:
                        Intent amphibiansActivityIntentss = new Intent(editActivity.this,editActivity.class);
                        startActivity(amphibiansActivityIntentss);


                        break;

                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(editActivity.this, MainActivity.class);
                        startActivity(amphibiansActivityIntentssss);


                        break;

                    case R.id.info:
                        Intent amphibiansActivityIntents = new Intent(editActivity.this, Report_Activity.class);
                        startActivity(amphibiansActivityIntents);


                        break;

                    case R.id.creates:
                        Intent amphibiansActivityIntentsss = new Intent(editActivity.this, list_of_disease.class);
                        startActivity(amphibiansActivityIntentsss);


                        break;





                }

                return true;
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.casisang, menu);

        return true;
    }

}