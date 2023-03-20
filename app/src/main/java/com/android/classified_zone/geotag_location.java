package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.classified_zone.databinding.ActivityPoint1Binding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class geotag_location extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityPoint1Binding binding;
   Button next,refresh;
   Marker aMarker;
   TextView nae, snipss;
   ImageView tag_photo;
   BottomNavigationView navigationView, mapview;
   Button new_geo;



    private float GEOFENCE_RADIUS = 50;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";


    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //binding = ActivityPoint1Binding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        setContentView(R.layout.activity_point2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        navigationView = findViewById(R.id.bottom_navigation);
        mapview = findViewById(R.id.maps);
        new_geo= findViewById(R.id.geo);

        new_geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(geotag_location.this, user_tracking.class);
                startActivity(i);
            }
        });


        mapview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){


                    case R.id.hybrid_map:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        return true;
                    case R.id.normal_map:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        return true;
                    case R.id.satellite_map:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        return true;
                    case R.id.terrain_map:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        return true;

                }
                return true;
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){


                    case R.id.view:


                        return true;
                    case R.id.edits:


                        return true;
                    case R.id.homes:
                        // Intent amphibiansActivityIntentssss = new Intent(editActivity.this, MainActivity.class);
                        // startActivity(amphibiansActivityIntentssss);
                        return true;
                    case R.id.info:
                        // Intent amphibiansActivityIntents = new Intent(editActivity.this, Report_Activity.class);
                        //  startActivity(amphibiansActivityIntents);
                        return true;
                    case R.id.creates:
                        // Intent amphibiansActivityIntentsss = new Intent(editActivity.this, list_of_disease.class);
                        //startActivity(amphibiansActivityIntentsss);
                        return true;
                }
                return true;
            }
        });

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }


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
        LatLng casisang= new LatLng(8.133303764999694, 125.13788323894);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casisang, 16));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(casisang));
        enableUserLocation();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //String nam= getIntent().getStringExtra("key" );
        DatabaseReference dbRef = database.getReference("track").child("user");
        dbRef .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot: snapshot.getChildren()) {

//                    String name = snapshot.child("name").getValue().toString();
                    String urls = childSnapshot.child("imageURL").getValue().toString();
                    String name = childSnapshot.child("name").getValue().toString();
                    String lat1 = childSnapshot.child("last_latitude").getValue().toString();
                    String lon1 = childSnapshot.child("last_longitude").getValue().toString();

                    String latlong = "lat:"+lat1+" lon: "+lon1;

                   double lati1 = Double.valueOf(lat1);
                    double longi1 = Double.valueOf(lon1);

                    URL url = null;
                    try {
                        url = new URL(urls);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        LatLng center = new LatLng(lati1, longi1);



                        Bitmap aMarker = Bitmap.createScaledBitmap(bmp, 200, 260, false);

                        // set image to circle
                        int sice = Math.min(200, 260);

                        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bmp, sice, sice);

                        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(output);

                        final int color = 0xffff0000;
                        final Paint paint = new Paint();
                        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        final RectF rectF = new RectF(rect);

                        paint.setAntiAlias(true);
                        paint.setDither(true);
                        paint.setFilterBitmap(true);
                        canvas.drawARGB(0, 0, 0, 0);
                        paint.setColor(color);
                        canvas.drawOval(rectF, paint);

                        paint.setColor(Color.BLUE);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth((float) 4);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(bitmap, rect, rect, paint);



                        Marker marker =  mMap.addMarker(new MarkerOptions().position(center).title(name).snippet(latlong).icon(BitmapDescriptorFactory.fromBitmap(output)));

                        marker.showInfoWindow();
                        marker.setTag(center);
                        marker.setTag(urls);
                        mMap.setOnMarkerClickListener(geotag_location.this);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poly2, 16));

                // mMap.addMarker(new MarkerOptions().position(poly1).title(name));
                // mMap.addMarker(new MarkerOptions().position(poly2).title(name));
                // mMap.addMarker(new MarkerOptions().position(poly3).title(name));
                //mMap.addMarker(new MarkerOptions().position(poly4).title(name));

                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poly1, 10));










                    //Toast.makeText(ViewActivity.this, "" +lat1+"____"+lon2 , Toast.LENGTH_LONG).show();



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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


    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        // Retrieve the data from the marker.
//        Integer clickCount = (Integer) marker.getTag();

        String ss = marker.getId();
        String title= String.valueOf(marker.getTitle());
        String snips= String.valueOf(marker.getSnippet());
        String lats = String.valueOf(marker.getPosition().latitude);
        String lons= String.valueOf(marker.getPosition().longitude);
        String dd = String.valueOf(marker.getTag());
      //  Toast.makeText(this,dd ,Toast.LENGTH_LONG).show();

         Dialog dialog = new Dialog(geotag_location.this);
        dialog.setContentView(R.layout.tag_photo);
        dialog.setCanceledOnTouchOutside(false);

        tag_photo= dialog.findViewById(R.id.imageView2);
       // nae= dialog.findViewById(R.id.name);
     //   snipss= dialog.findViewById(R.id.snippet);
        FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);


        AlertDialog.Builder builder=new AlertDialog.Builder(geotag_location.this);
        builder.setTitle("Update");
        builder.setMessage("Ok?");
        dialog.show();

        Glide.with(geotag_location.this).load(dd).into(tag_photo);
        //nae.setText(dd);
      //  snipss.setText(snips);


        return false;
    }
}