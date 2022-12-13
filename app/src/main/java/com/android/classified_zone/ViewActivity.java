package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.android.classified_zone.databinding.ActivityViewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityViewBinding binding;

    private float GEOFENCE_RADIUS = 50;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    BottomNavigationView navigationView;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.view:
                        Intent amphibiansActivityIntent = new Intent(ViewActivity.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntent);
                        break;
                    case R.id.edits:
                        Intent amphibiansActivityIntentss = new Intent(ViewActivity.this,editActivity.class);
                        startActivity(amphibiansActivityIntentss);
                        break;
                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(ViewActivity.this, MainActivity.class);
                        startActivity(amphibiansActivityIntentssss);
                        break;
                    case R.id.info:
                        Intent amphibiansActivityIntents = new Intent(ViewActivity.this, Report_Activity.class);
                        startActivity(amphibiansActivityIntents);
                        break;

                    case R.id.creates:
                        Intent amphibiansActivityIntentsss = new Intent(ViewActivity.this, list_of_disease.class);
                        startActivity(amphibiansActivityIntentsss);
                        break;

                }

                return true;
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


        LatLng sydney = new LatLng(8.133303764999694, 125.13788323894);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Casisang"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        enableUserLocation();
        hotspot();
    }

    private void hotspot() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference dbRef = database.getReference("alerts_zone").child("classified_zone");

        dbRef .addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // user.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    String Name = childSnapshot.child("Geo_Name").getValue().toString();
                    String description = childSnapshot.child("Description").getValue().toString();
                    String purok = childSnapshot.child("Purok").getValue().toString();
                    //String radius = childSnapshot.child("Radius").getValue().toString();
                    Integer radius = Integer.valueOf(childSnapshot.child("Radius").getValue().toString());


                    String lat = childSnapshot.child("latitude").getValue().toString();
                    String lon = childSnapshot.child("longitude").getValue().toString();

                    Double lati = Double .valueOf(lat);
                    Double  longi = Double .valueOf(lon);
                    String radi = String.valueOf(radius);

                    String title = "Name:\t" +Name +"\t\tRadius:\t" +radi ;
                    String dtitle= title + "\t"+description+ "\t"+ purok;


                    LatLng LatLng = new LatLng(lati, longi);

                    Marker marker =  mMap.addMarker(new MarkerOptions().position(LatLng).title(dtitle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    mMap.addCircle(new CircleOptions()
                            .center(LatLng)
                            .radius(radius)
                            .strokeColor(Color.argb(255, 52, 234,53))
                            .fillColor(Color.argb(64, 75, 136,162))
                            .strokeWidth(4));
                    enableUserLocation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                //We do not have the permission..

            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                //We do not have the permission..
                Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}