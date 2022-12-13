package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.classified_zone.databinding.ActivityReportBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import static java.lang.String.valueOf;


public class Report_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityReportBinding binding;
    BottomNavigationView navigationView;
    TextView num_class_zone, num_violations, list_of_disease, num_purok, num_Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        num_class_zone = findViewById(R.id.geofences);
        num_violations = findViewById(R.id.violations);
        list_of_disease = findViewById(R.id.risk);
        num_purok = findViewById(R.id.purok);
        num_Area =  findViewById(R.id.total_area);


        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int num = (int) dataSnapshot.getChildrenCount();

                String count_geo = valueOf(num);
                // set text
                num_class_zone.setText(count_geo);
                num_purok.setText(count_geo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference userviolation = FirebaseDatabase.getInstance().getReference("covid_tool").child("trigger").child("user_enter");
        userviolation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int num = (int) dataSnapshot.getChildrenCount();

                String count_vio = valueOf(num);
                // set text
                num_violations.setText(count_vio);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference risky = FirebaseDatabase.getInstance().getReference("alerts_zone").child("list_of_disease");
        risky .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int nums = (int) dataSnapshot.getChildrenCount();

                String count_risk= valueOf(nums);
                // set text
                list_of_disease.setText(count_risk);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference Db = FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone");
        Db.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                int total_total = 0;
                int total_recover = 0;
                int total_deat= 0;
                int total_actives = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    try {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();

                        Object value_total = map.get("Radius");
                        Double avalue = Double.parseDouble(valueOf(value_total));
                        total_total += avalue;
                        double d =  0.001;

                      Double s = Double.parseDouble(String.valueOf(total_total));
                      double sum = s*  0.001;
                      double kmsq2 = sum *sum;

                      DecimalFormat decfor = new DecimalFormat("0.000");

                      num_Area.setText(decfor.format(kmsq2)+" Km sq");
                       // Toast.makeText(Report_Activity.this, "" + formatter.format(strI)+" km sq" , Toast.LENGTH_SHORT).show();

                    }
                    catch (NumberFormatException e)
                    {
// log e if you want...
                    }
                }



            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(8.133303764999694, 125.13788323894);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.view:
                        Intent amphibiansActivityIntent = new Intent(Report_Activity.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntent);

                        break;

                    case R.id.edits:
                        Intent amphibiansActivityIntentss = new Intent(Report_Activity.this,editActivity.class);
                        startActivity(amphibiansActivityIntentss);


                        break;

                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(Report_Activity.this, MainActivity.class);
                        startActivity(amphibiansActivityIntentssss);


                        break;

                    case R.id.info:
                        Intent amphibiansActivityIntents = new Intent(Report_Activity.this, Report_Activity.class);
                        startActivity(amphibiansActivityIntents);


                        break;

                    case R.id.creates:
                        Intent amphibiansActivityIntentsss = new Intent(Report_Activity.this,list_of_disease.class);
                        startActivity(amphibiansActivityIntentsss);


                        break;





                }

                return true;
            }
        });


    }
}