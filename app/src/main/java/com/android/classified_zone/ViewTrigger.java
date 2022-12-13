package com.android.classified_zone;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;

import android.os.Bundle;


import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;



import com.firebase.geofire.GeoFire;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ViewTrigger extends AppCompatActivity
        implements OnMapReadyCallback {
    public GeofenceHelper geofenceHelper;

    /* access modifiers changed from: private */
    public GeofencingClient geofencingClient;

    // private GeofencingClient geofencingClient;
    // private GeofenceHelper geofenceHelper;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mLastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseReference database; // database
    GeoFire geoFire;            // database
    Marker locationMarker;

    private static final int PermissionCode = 1994;
    private static final int ServiceRequest = 1994;
    private MediaPlayer mp;
    private DatabaseReference Db;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private Location mCurrentLocation;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    public LatLng latlng1;
    private java.util.List List;
    public List<Geofence> geofencelist = new ArrayList();
    public String geo_ID;
    public Integer radius1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4b88a2")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>AlertUp</font>"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        this.geofencingClient = LocationServices.getGeofencingClient(this);
        this.geofenceHelper = new GeofenceHelper(this);


        // Database reference
        database = FirebaseDatabase.getInstance().getReference("Location");
        geoFire = new GeoFire(database);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (Build.VERSION.SDK_INT < 29) {
            // hotspot();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_BACKGROUND_LOCATION") == 0) {
            //hotspot();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_BACKGROUND_LOCATION")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_BACKGROUND_LOCATION"}, this.BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_BACKGROUND_LOCATION"}, this.BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
        }
        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(this.kolkata));
//        this.mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
        mMap = googleMap;
        LatLng casisang= new LatLng(8.133303764999694, 125.13788323894);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casisang, 15));


        hotspot();
        enableUserLocation();
        // user_location();
    }
    private void  user_location(){
        Db = FirebaseDatabase.getInstance().getReference("covid_tool").child("geofencing").child("user_location");
        Db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        // String Name = dataSnapshot1.child("name").getValue().toString();
                        //   Double latitude = Double.valueOf(dataSnapshot1.child("last_latitude").getValue().toString());
                        //   Double longitude = Double.valueOf(dataSnapshot1.child("last_longitude").getValue().toString());
                        //   LatLng LatLng = new LatLng(latitude, longitude);

                        // addMarker(LatLng);
                        // geo_ID = Name;
                        // latlng1 = LatLng;
                        // String title = "Name:\t" +Name ;
                        // float[] colours = { BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_ORANGE,
                        //     /* etc */ };
                        // BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)]);
                        // Marker marker =  mMap.addMarker(new MarkerOptions().position(LatLng).title(title).icon( BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
                        //marker.showInfoWindow();

                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void hotspot() {
        Db = FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone");
        Db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // user.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        String Name = dataSnapshot1.child("Geo_Name").getValue().toString();
                        Integer radius = Integer.valueOf(dataSnapshot1.child("Radius").getValue().toString());
                        Double latitude = Double.valueOf(dataSnapshot1.child("latitude").getValue().toString());
                        Double longitude = Double.valueOf(dataSnapshot1.child("longitude").getValue().toString());
                        LatLng LatLng = new LatLng(latitude, longitude);

                        String a1 = String.valueOf(radius);

                        //Toast.makeText(MapsActivity2.this, "name  " + Name+"   radius "+a1 + "latitude" +latitude+ "longitude"  +longitude, Toast.LENGTH_LONG).show();
                        addMarker(LatLng);
                        addCircle(LatLng, radius);
                        geo_ID = Name;
                        latlng1 = LatLng;
                        radius1 = radius;
                        String title = "Name:\t" +Name + "\t\tradius:\t" +radius;
                        Marker marker =  mMap.addMarker(new MarkerOptions().position(LatLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        marker.showInfoWindow();
                        //showInfoWindow();
                        // addgeofence(latitude, longitude , perimeter);

                        addGeofence(geo_ID, latlng1, radius1);
                    }
                    catch (NumberFormatException e)
                    {
// log e if you want...
                    }

                }



                GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofencelist);
                PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

                if (ActivityCompat.checkSelfPermission(ViewTrigger.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d(TAG, "onSuccess: Geofence Added...");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String errorMessage = geofenceHelper.getErrorString(e);
                                //Log.d(TAG, "onFailure: " + errorMessage);
                            }
                        });




            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });




    }

    private void addGeofence(String geo_ID2, LatLng latLng1, Integer perimeter) {
        //double longitude = LatLng.longitude;
        //double latitude = LatLng.latitude;
        // Float perimeters = Float.valueOf(perimeter);
        //LatLng = new LatLng(latitude, longitude);
        // Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, LatLng, perimeter, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        geofencelist.add(geofenceHelper.getGeofence(geo_ID2, latLng1, perimeter,  Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT));
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofencelist);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        //Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });

    }





/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


*/

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



    private void playSound(int resId) {
        mp = MediaPlayer.create(ViewTrigger.this, resId);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
        mp.start();
    }


    private void vibrateDevice() {

        Vibrator vibratorService = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (vibratorService != null && vibratorService.hasVibrator()) {
            vibratorService.vibrate(500);
        }
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, Integer radius) {

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 52, 234,53));
        circleOptions.fillColor(Color.argb(64, 75, 136,162));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }



}