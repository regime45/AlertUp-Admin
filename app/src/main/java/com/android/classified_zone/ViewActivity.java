package com.android.classified_zone;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.classified_zone.databinding.ActivityPoint1Binding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


/**
 * An activity that displays a Google map with polylines to represent paths or routes,
 * and polygons to represent areas.
 */
// [START maps_poly_activity_on_map_ready]
public class ViewActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {
        private GoogleMap mMap;
        private BottomNavigationView navigationView;
        private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
        private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

        Polygon polygon = null;
        Double lati1 ;
        Double longi1 ;
        Double lati2 ;
        Double longi2 ;
        Double lati3 ;
        Double longi3;
        Double lati4 ;
        Double longi4;
        List<LatLng> latLngs = null;

        List<LatLng> latLngList = new ArrayList<>();
        List<Marker> markerList = new ArrayList<>();
        private ActivityPoint1Binding binding;
        private DatabaseReference Db;
        private Marker mCustomerMarker;




        // [START EXCLUDE]
        // [START maps_poly_activity_get_map_async]
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                // Retrieve the content view that renders the map.
                setContentView(R.layout.activity_view);


                // Get the SupportMapFragment and request notification when the map is ready to be used.
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

                                        case R.id.view: Intent amphibiansActivityIntent = new Intent(ViewActivity.this, ViewActivity.class);
                                                startActivity(amphibiansActivityIntent);
                                             /*   if (polygon != null) polygon.remove();
                                                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList)
                                                        .clickable(true);
                                                polygon = mMap.addPolygon(polygonOptions);
                                                int color = polygon.getStrokeColor() ^ 0x33FF0000;
                                                polygon.setFillColor(Color.TRANSPARENT);

                                              */

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

                if (Build.VERSION.SDK_INT >= 30){
                        if (!Environment.isExternalStorageManager()){
                                Intent getpermission = new Intent();
                                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(getpermission);
                        }
                }
                /*

                String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+"a.json");

                JSONObject json = new JSONObject();
                try {
                        json.put("name", "Google");
                        json.put("employees", 140000);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                json.put("offices", List.of("Mountain View", "Los Angeles", "New York"));
                        }
                } catch (JSONException e) {
                        e.printStackTrace();
                }

                try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                        out.write(json.toString());
                } catch (Exception e) {
                        e.printStackTrace();
                }

                 */




        }
        // [END maps_poly_activity_get_map_async]

        /**
         * Manipulates the map when it's available.
         * The API invokes this callback when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
         */
        // [END EXCLUDE]
        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng casisang= new LatLng(8.133303764999694, 125.13788323894);


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casisang, 16));



                // Add polylines to the map.
                // Polylines are useful to show a route or some other connection between points.
                // [START maps_poly_activity_add_polyline_set_tag]
                // [START maps_poly_activity_add_polyline]
                Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(-35.016, 143.321),
                                new LatLng(-34.747, 145.592),
                                new LatLng(-34.364, 147.891),
                                new LatLng(-33.501, 150.217),
                                new LatLng(-32.306, 149.248),
                                new LatLng(-32.491, 147.309)));
                // [END maps_poly_activity_add_polyline]
                // [START_EXCLUDE silent]
                // Store a data object with the polyline, used here to indicate an arbitrary type.
                polyline1.setTag("A");
                // [END maps_poly_activity_add_polyline_set_tag]
                // Style the polyline.
                stylePolyline(polyline1);

                Polyline polyline2 = googleMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(-29.501, 119.700),
                                new LatLng(-27.456, 119.672),
                                new LatLng(-25.971, 124.187),
                                new LatLng(-28.081, 126.555),
                                new LatLng(-28.848, 124.229),
                                new LatLng(-28.215, 123.938)));
                polyline2.setTag("B");
                stylePolyline(polyline2);

                // [START maps_poly_activity_add_polygon]
                // Add polygons to indicate areas on the map.
                Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                        .clickable(true)
                        .add(
                                new LatLng(-27.457, 153.040),
                                new LatLng(-33.852, 151.211),
                                new LatLng(-37.813, 144.962),
                                new LatLng(-34.928, 138.599)));
                // Store a data object with the polygon, used here to indicate an arbitrary type.
                polygon1.setTag("alpha");
                // [END maps_poly_activity_add_polygon]
                // Style the polygon.
                stylePolygon(polygon1);

                Polygon polygon2 = googleMap.addPolygon(new PolygonOptions()
                        .clickable(true)
                        .add(
                                new LatLng(-31.673, 128.892),
                                new LatLng(-31.952, 115.857),
                                new LatLng(-17.785, 122.258),
                                new LatLng(-12.4258, 130.7932)));
                polygon2.setTag("beta");
                stylePolygon(polygon2);
                // [END_EXCLUDE]

                // Position the map's camera near Alice Springs in the center of Australia,
                // and set the zoom factor so most of Australia shows on the screen.
                LatLng casisangs= new LatLng(8.133303764999694, 125.13788323894);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casisangs, 16));


                // Set listeners for click events.
                googleMap.setOnPolylineClickListener(this);
                googleMap.setOnPolygonClickListener(this);
                mMap.setOnMapLongClickListener(this);
                mMap.setOnMarkerDragListener(this);
                enableUserLocation();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference("poly").child("tline");
                dbRef .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                        geo_model dsds = childSnapshot.getValue(geo_model.class);

                                        //String key = childSnapshot.getKey();

                                       // heatmaps(key);

                                        String name = String.valueOf(dsds.getname());
                                        String radius =childSnapshot.child("radius").getValue().toString();
                                        String lat1 = childSnapshot.child("plat1").getValue().toString();
                                        String lon1 = childSnapshot.child("plon1").getValue().toString();
                                        String lat2 = childSnapshot.child("plat2").getValue().toString();
                                        String lon2 = childSnapshot.child("plon2").getValue().toString();
                                        String lat3 = childSnapshot.child("plat3").getValue().toString();
                                        String lon3 = childSnapshot.child("plon3").getValue().toString();
                                        String lat4 = childSnapshot.child("plat4").getValue().toString();
                                        String lon4 = childSnapshot.child("plon4").getValue().toString();

                                      //  int size = (int) childSnapshot.getChildrenCount();\
                                        lati1 = Double.valueOf(lat1);
                                        longi1 = Double.valueOf(lon1);
                                        lati2 = Double.valueOf(lat2);
                                        longi2 = Double.valueOf(lon2);
                                        lati3 = Double.valueOf(lat3);
                                        longi3 = Double.valueOf(lon3);
                                        lati4 = Double.valueOf(lat4);
                                        longi4 = Double.valueOf(lon4);



                                        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+"c.json");
                                        JSONObject json = new JSONObject();
                                        try {

                                                        json.put("lat", lati1);
                                                        json.put("lon",longi1);
                                                        //locations.add(new LatLng(lati1, longi1));

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                        //    json.put("offices", List.of("Mountain View", "Los Angeles", "New York"));
                                                }
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }

                                        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                                                out.write(json.toString());
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                        }
                                        double calrad = Double.parseDouble(radius);
                                         double calrads = calrad /3;
                                              // float rad = Float.valueOf(radius);


                                                LatLng poly1 = new LatLng(lati1, longi1);
                                                LatLng poly2 = new LatLng(lati2, longi2);
                                                LatLng poly3 = new LatLng(lati3, longi3);
                                                LatLng poly4 = new LatLng(lati4, longi4);
                                        Gson gson = new Gson();

                                        String jsonString = gson.toJson(childSnapshot.child("plat1").getValue().toString());
                                       // Toast.makeText(ViewActivity.this, ""+jsonString, Toast.LENGTH_LONG).show();

                                        double CenterLat = (lati1 + lati2 + lati3 + lati4 ) / 4;
                                        double CenterLon = (longi1 + longi2 + longi3 + longi4 ) / 4;
                                        LatLng Center = new LatLng(CenterLat, CenterLon);
                                        mMap.addMarker(new MarkerOptions().position(Center).title(name));

                                        ArrayList<LatLng> result = new ArrayList<>();

                                        ArrayList<LatLng> latLngs = new ArrayList<>();
                                        latLngs.add(new LatLng(lati1, longi1));
                                        latLngs.add(new LatLng(lati2, longi2));
                                        latLngs.add(new LatLng(lati3, longi3));
                                        latLngs.add(new LatLng(lati4, longi4));
                                        Log.i("TAG", "computeArea " + SphericalUtil.computeArea(latLngs));

                                        int[] colors = {
                                                Color.GREEN,    // green
                                                Color.YELLOW,    // yellow
                                                Color.rgb(255,165,0), //Orange
                                                Color.RED,              //red
                                                Color.rgb(153,50,204), //dark orchid
                                                Color.rgb(165,42,42) //brown
                                        };

                                        float[] startpoints = {
                                                0.05f,    //0-50
                                                0.1f,   //51-100
                                                0.2f,   //101-150
                                                0.3f,   //151-200
                                                0.4f,    //201-300
                                                0.6f      //301-500
                                        };
                                        /*
                                        result.add(new LatLng(lati1, longi1));
                                        for(LatLng results : result){






                                        Gradient gradient = new Gradient(colors,startpoints);
                                        HeatmapTileProvider heatmapTileProvider;
                                        heatmapTileProvider = new HeatmapTileProvider.Builder()
                                                .data(Collections.singleton(results))
                                                .radius(50)
                                                .gradient(gradient)
                                                .build();
                                        TileOverlayOptions tileoverlayoptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
                                        mMap.addTileOverlay(tileoverlayoptions);

                                        }

                                         */

                                        //Toast.makeText(ViewActivity.this, ""+name+ ": "+ SphericalUtil.computeArea(latLngs), Toast.LENGTH_LONG).show();
                                        /*
                                        CircleOptions circleOptions = new CircleOptions();
                                        circleOptions.center(Center);
                                        circleOptions.radius(calrads);
                                        circleOptions.strokeColor(Color.argb(255, 255, 0,0));
                                        circleOptions.fillColor(Color.argb(64, 255, 0,0));
                                        circleOptions.strokeWidth(4);
                                        mMap.addCircle(circleOptions);

                                         */
                                        //List<LatLng> result = new ArrayList<>();
                                        //result.add(poly1);
                                        //result.add(poly2);
                                       // result.add(poly3);
                                        //result.add(poly4);
                                      // computeCentroid(result);
                                              //  mMap.addMarker(new MarkerOptions().position(poly1).title(name));
                                          // addheatmap();
                                          //  buildheatmap(result);
                                         ///addHeatMap();

                                        // mMap.addMarker(new MarkerOptions().position(poly2).title(name));
                                               // mMap.addMarker(new MarkerOptions().position(poly3).title(name));
                                                //mMap.addMarker(new MarkerOptions().position(poly4).title(name));

                                                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poly1, 10));

                                        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                                                        .clickable(true)
                                                        .add(poly1, poly2, poly3, poly4, poly1));
                                                // Store a data object with the polygon, used here to indicate an arbitrary type.
                                                polygon1.setTag("beta");
                                                // [END maps_poly_activity_add_polygon]
                                                // Style the polygon.
                                                stylePolygon(polygon1);

                                }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                });
                user_location();

                          }

        private void heatmaps(String key) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference("poly").child("tline");
                dbRef .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                        geo_model dsds = childSnapshot.getValue(geo_model.class);

                                        String key = childSnapshot.getKey();

                                        heatmaps(key);

                                        String name = String.valueOf(dsds.getname());
                                        String radius =childSnapshot.child("radius").getValue().toString();
                                        String lat1 = childSnapshot.child("plat1").getValue().toString();
                                        String lon1 = childSnapshot.child("plon1").getValue().toString();
                                        String lat2 = childSnapshot.child("plat2").getValue().toString();
                                        String lon2 = childSnapshot.child("plon2").getValue().toString();
                                        String lat3 = childSnapshot.child("plat3").getValue().toString();
                                        String lon3 = childSnapshot.child("plon3").getValue().toString();
                                        String lat4 = childSnapshot.child("plat4").getValue().toString();
                                        String lon4 = childSnapshot.child("plon4").getValue().toString();


                                        double calrad = Double.parseDouble(radius);
                                        double calrads = calrad /3;
                                        // float rad = Float.valueOf(radius);
                                        lati1 = Double.valueOf(lat1);
                                        longi1 = Double.valueOf(lon1);
                                        lati2 = Double.valueOf(lat2);
                                        longi2 = Double.valueOf(lon2);
                                        lati3 = Double.valueOf(lat3);
                                        longi3 = Double.valueOf(lon3);
                                        lati4 = Double.valueOf(lat4);
                                        longi4 = Double.valueOf(lon4);

                                        LatLng poly1 = new LatLng(lati1, longi1);
                                        LatLng poly2 = new LatLng(lati2, longi2);
                                        LatLng poly3 = new LatLng(lati3, longi3);
                                        LatLng poly4 = new LatLng(lati4, longi4);
                                        Gson gson = new Gson();

                                        String jsonString = gson.toJson(childSnapshot.child("plat1").getValue().toString());
                                        // Toast.makeText(ViewActivity.this, ""+jsonString, Toast.LENGTH_LONG).show();

                                        double CenterLat = (lati1 + lati2 + lati3 + lati4 ) / 4;
                                        double CenterLon = (longi1 + longi2 + longi3 + longi4 ) / 4;
                                        LatLng Center = new LatLng(CenterLat, CenterLon);
                                       // mMap.addMarker(new MarkerOptions().position(Center).title(name));

                                        ArrayList<LatLng> result = new ArrayList<>();

                                        ArrayList<LatLng> latLngs = new ArrayList<>();
                                        latLngs.add(new LatLng(lati1, longi1));
                                        latLngs.add(new LatLng(lati2, longi2));
                                        latLngs.add(new LatLng(lati3, longi3));
                                        latLngs.add(new LatLng(lati4, longi4));
                                        Log.i("TAG", "computeArea " + SphericalUtil.computeArea(latLngs));

                                        int[] colors = {
                                                Color.GREEN,    // green
                                                Color.YELLOW,    // yellow
                                                Color.rgb(255,165,0), //Orange
                                                Color.RED,              //red
                                                Color.rgb(153,50,204), //dark orchid
                                                Color.rgb(165,42,42) //brown
                                        };

                                        float[] startpoints = {
                                                0.05f,    //0-50
                                                0.1f,   //51-100
                                                0.2f,   //101-150
                                                0.3f,   //151-200
                                                0.4f,    //201-300
                                                0.6f      //301-500
                                        };
                                        result.add(new LatLng(lati1, longi1));
                                        /*
                                        for(LatLng results : result){

                                                Gradient gradient = new Gradient(colors,startpoints);
                                                HeatmapTileProvider heatmapTileProvider;
                                                heatmapTileProvider = new HeatmapTileProvider.Builder()
                                                        .data(Collections.singleton(results))
                                                        .radius(50)
                                                        .gradient(gradient)
                                                        .build();
                                                TileOverlayOptions tileoverlayoptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
                                                mMap.addTileOverlay(tileoverlayoptions);

                                        }

                                         */

                                        //Toast.makeText(ViewActivity.this, ""+name+ ": "+ SphericalUtil.computeArea(latLngs), Toast.LENGTH_LONG).show();
                                        /*
                                        CircleOptions circleOptions = new CircleOptions();
                                        circleOptions.center(Center);
                                        circleOptions.radius(calrads);
                                        circleOptions.strokeColor(Color.argb(255, 255, 0,0));
                                        circleOptions.fillColor(Color.argb(64, 255, 0,0));
                                        circleOptions.strokeWidth(4);
                                        mMap.addCircle(circleOptions);

                                         */
                                        //List<LatLng> result = new ArrayList<>();
                                        //result.add(poly1);
                                        //result.add(poly2);
                                        // result.add(poly3);
                                        //result.add(poly4);
                                        // computeCentroid(result);
                                        //  mMap.addMarker(new MarkerOptions().position(poly1).title(name));
                                        // addheatmap();
                                        //  buildheatmap(result);
                                        ///addHeatMap();

                                        // mMap.addMarker(new MarkerOptions().position(poly2).title(name));
                                        // mMap.addMarker(new MarkerOptions().position(poly3).title(name));
                                        //mMap.addMarker(new MarkerOptions().position(poly4).title(name));

                                        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poly1, 10));

                                      //  Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                                       //         .clickable(true)
                                        //        .add(poly1, poly2, poly3, poly4, poly1));
                                        // Store a data object with the polygon, used here to indicate an arbitrary type.
                                       // polygon1.setTag("beta");
                                        // [END maps_poly_activity_add_polygon]
                                        // Style the polygon.
                                      //  stylePolygon(polygon1);

                                }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                });
        }

        private String formatNumber(double distance) {
                String unit = "";
                if (distance < 1) {
                        distance /= 1000;
                        unit = "";
                } else if (distance > 1000) {
                        distance /= 1000;
                        unit = "";
                }

                return String.format("%1.3f%s", distance, unit);
        }






        //   private computeCentroid() {



     //   }
                        // [END maps_poly_activity_on_map_ready]

        // [START maps_poly_activity_style_polyline]
        private static final int COLOR_BLACK_ARGB = 0xff000000;
        private static final int POLYLINE_STROKE_WIDTH_PX = 12;

        /**
         * Styles the polyline, based on type.
         * @param polyline The polyline object that needs styling.
         */
        private void stylePolyline(Polyline polyline) {
                String type = "";
                // Get the data object stored with the polyline.
                if (polyline.getTag() != null) {
                        type = polyline.getTag().toString();
                }

                switch (type) {
                        // If no type is given, allow the API to use the default.
                        case "A":
                                // Use a custom bitmap as the cap at the start of the line.
                               // polyline.setStartCap(
                                        //new CustomCap(
                                               // BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_arrow_upward_24), 10));
                                break;
                        case "B":
                                // Use a round cap at the start of the line.
                                polyline.setStartCap(new RoundCap());
                                break;
                }

                polyline.setEndCap(new RoundCap());
                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                polyline.setColor(COLOR_BLACK_ARGB);
                polyline.setJointType(JointType.ROUND);
        }
        // [END maps_poly_activity_style_polyline]

        // [START maps_poly_activity_on_polyline_click]
        private static final int PATTERN_GAP_LENGTH_PX = 20;
        private static final PatternItem DOT = new Dot();
        private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

        // Create a stroke pattern of a gap followed by a dot.
        private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

        /**
         * Listens for clicks on a polyline.
         * @param polyline The polyline object that the user has clicked.
         */
        @Override
        public void onPolylineClick(Polyline polyline) {
                // Flip from solid stroke to dotted stroke pattern.
                if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
                        polyline.setPattern(PATTERN_POLYLINE_DOTTED);
                } else {
                        // The default pattern is a solid stroke.
                        polyline.setPattern(null);
                }

                Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                        Toast.LENGTH_SHORT).show();
        }
        // [END maps_poly_activity_on_polyline_click]

        /**
         * Listens for clicks on a polygon.
         * @param polygon The polygon object that the user has clicked.
         */
        @Override
        public void onPolygonClick(Polygon polygon) {
                // Flip the values of the red, green, and blue components of the polygon's color.
                int color = polygon.getStrokeColor() ^ 0x33FF0000;
                polygon.setStrokeColor(color);
                color = polygon.getFillColor() ^ 0xffF9A825;
                polygon.setFillColor(color);


                Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
        }

        // [START maps_poly_activity_style_polygon]
        private static final int COLOR_WHITE_ARGB = 0xffffffff;
        private static final int COLOR_DARK_GREEN_ARGB = 0x33FF0000;
        private static final int COLOR_LIGHT_GREEN_ARGB = 0x33FF0000;
        private static final int COLOR_DARK_ORANGE_ARGB = 0x33FF0000;
        private static final int COLOR_LIGHT_ORANGE_ARGB = 0x33FF0000;

        private static final int POLYGON_STROKE_WIDTH_PX = 8;
        private static final int PATTERN_DASH_LENGTH_PX = 20;
        private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

        // Create a stroke pattern of a gap followed by a dash.
        private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

        // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
        private static final List<PatternItem> PATTERN_POLYGON_BETA =
                Arrays.asList(DOT, GAP, DASH, GAP);

        /**
         * Styles the polygon, based on type.
         * @param polygon The polygon object that needs styling.
         */
        private void stylePolygon(Polygon polygon) {
                String type = "";
                // Get the data object stored with the polygon.
                if (polygon.getTag() != null) {
                        type = polygon.getTag().toString();
                }

                List<PatternItem> pattern = null;
                int strokeColor = COLOR_BLACK_ARGB;
                int fillColor = COLOR_WHITE_ARGB;

                switch (type) {
                        // If no type is given, allow the API to use the default.
                        case "alpha":
                                // Apply a stroke pattern to render a dashed line, and define colors.
                                pattern = PATTERN_POLYGON_ALPHA;
                                strokeColor = COLOR_DARK_GREEN_ARGB;
                                fillColor = COLOR_LIGHT_GREEN_ARGB;
                                break;
                        case "beta":
                                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                                pattern = PATTERN_POLYGON_BETA;
                                strokeColor = COLOR_DARK_ORANGE_ARGB;
                                fillColor = COLOR_LIGHT_ORANGE_ARGB;
                                break;
                }

                polygon.setStrokePattern(pattern);
                polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
                polygon.setStrokeColor(strokeColor);
                polygon.setFillColor(fillColor);

        }

        public void onMapLongClick(LatLng latLng) {
                if (Build.VERSION.SDK_INT >= 29) {
                        //We need background permission
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                handleMapLongClick(latLng);




                               // Toast.makeText(this, ""+lats+""+longs, Toast.LENGTH_SHORT).show();

                        } else {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                                        //We show a dialog and ask for permission
                                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                                } else {
                                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                                }
                        }

                } else {
                        handleMapLongClick(latLng);
                }

        }

        private void handleMapLongClick(LatLng latLng) {
                //mMap.clear();
                addMarker(latLng);
                addCircle(latLng, 20);
                //addpolyline(latLng);
                // mMap.addPolyline((new PolylineOptions()).add(latLng).width(5)
                // below line is use to add color to our poly line.
                //     .color(Color.RED)
                // below line is to make our poly line geodesic.
                // .geodesic(true));

                // addGeofence(latLng, GEOFENCE_RADIUS);
        }

        private void addpolyline(LatLng latLng) {
                Polyline site = mMap.addPolyline(new PolylineOptions()
                        .add(latLng)
                        .width(5)
                        .color(Color.MAGENTA));
               // mMap.addPolygon(site);
        }


        private void addMarker(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true);

                Marker marker = mMap.addMarker(markerOptions);
                latLngList.add(latLng);
                markerList.add(marker);


                /*

                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList)
                        .clickable(true);
                polygon = mMap.addPolygon(polygonOptions);
                int color = polygon.getStrokeColor() ^ 0x33FF0000;
                polygon.setStrokeColor(color);

                 */










                /*


                double longitude = latLng.longitude;
                double latitude = latLng.latitude;


                String lats = String.valueOf(latitude);
                String longs= String.valueOf(longitude);
               // Toast.makeText(this, ""+lats+""+longs, Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("poly").child("tline");
                DatabaseReference newChildRef = myRef.push();
                String key = "df";
                if (key != null) {
                        myRef.child(key).child("task").setValue( lats);
                        myRef.child(key).child("date").setValue( longs);

                }

                 */
        }

        private void addCircle(LatLng latLng, float radius) {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(latLng);
                circleOptions.radius(radius);
                circleOptions.strokeColor(Color.argb(255, 255, 0,0));
                circleOptions.fillColor(Color.argb(64, 255, 0,0));
                circleOptions.strokeWidth(4);
                mMap.addCircle(circleOptions);
        }

        @Override
        public void onMarkerDrag(@NonNull @NotNull Marker marker) {


        }

        @Override
        public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
                double lats=   marker.getPosition().latitude;
                double longs=   marker.getPosition().longitude;

                marker.getId();
                //Toast.makeText(this, String.valueOf(lats), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, marker.getId(), Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("poly").child("tline");
                DatabaseReference newChildRef = myRef.push();
                String key = "a";
            //if (marker.getId() != null) {
             //          myRef.child(key).child("plat1").setValue( lats);
              //         myRef.child(key).child("plon1").setValue( longs);

             //  }

        }

        @Override
        public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

        }
        private void addHeatMap() {

                // Get the data: latitude/longitude positions of police stations.
                try {
                        latLngs = readItems(R.raw.k);
                        
                } catch (JSONException e) {
                        Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
                }

                // Create a heat map tile provider, passing it the latlngs of the police stations.
                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                        .data(latLngs)
                        .build();

                // Add a tile overlay to the map, using the heat map tile provider.
                mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
                color();


        }

        private List<LatLng> readItems(@RawRes int resource) throws JSONException {
                List<LatLng> result = new ArrayList<>();
                InputStream inputStream = this.getResources().openRawResource(resource);
                String json = new Scanner(inputStream).useDelimiter("\\A").next();
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        double lat = object.getDouble("plat1");
                        double lng = object.getDouble("plon1");
                        result.add(new LatLng(lat, lng));
                }
                return result;

        }
        private void color(){

                // Create the gradient.
                /*
                int[] colors = {
                        Color.rgb(102, 225, 0), // green
                        Color.rgb(255, 0, 0)    // red
                };

                float[] startPoints = {
                        0.2f, 1f
                };


                Gradient gradient = new Gradient(colors, startPoints);

                 */
                int[] colors = {
                        Color.GREEN,    // green
                        Color.YELLOW,    // yellow
                        Color.rgb(255,165,0), //Orange
                        Color.RED,              //red
                        Color.rgb(153,50,204), //dark orchid
                        Color.rgb(165,42,42) //brown
                };

                float[] startpoints = {
                        0.05f,    //0-50
                        0.1f,   //51-100
                        0.2f,   //101-150
                        0.3f,   //151-200
                        0.4f,    //201-300
                        0.6f      //301-500
                };
                Gradient gradient = new Gradient(colors, startpoints);

// Create the tile provider.
                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                        .data(latLngs)
                        .radius(50)
                        .gradient(gradient)
                        .build();

// Add the tile overlay to the map.
                TileOverlay tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
        }


        private ArrayList addheatmap() {
                ArrayList<WeightedLatLng> arr = new ArrayList<>();


                arr.add(new WeightedLatLng(new LatLng(lati1, longi1),2000)); //madurai
                 //  arr.add(new WeightedLatLng(new LatLng(-14.94470356730159, 128.4884286671877),5000));
              //  arr.add(new WeightedLatLng(new LatLng(lati2, longi2),2000));
               // arr.add(new WeightedLatLng(new LatLng(lati3, longi3),2000));
               // arr.add(new WeightedLatLng(new LatLng(lati4, longi4),2000));
               // arr.add(new WeightedLatLng(new LatLng(-19.25577005158557, 129.6846355497837),2000));


                //Log.e("adding heatmap","yes");

                return arr;
        }


        private void buildheatmap(List<LatLng> result){
                int[] colors = {
                        Color.GREEN,    // green
                        Color.YELLOW,    // yellow
                        Color.rgb(255,165,0), //Orange
                        Color.RED,              //red
                        Color.rgb(153,50,204), //dark orchid
                        Color.rgb(165,42,42) //brown
                };

                float[] startpoints = {
                        0.05f,    //0-50
                        0.1f,   //51-100
                        0.2f,   //101-150
                        0.3f,   //151-200
                        0.4f,    //201-300
                        0.6f      //301-500
                };

                Gradient gradient = new Gradient(colors,startpoints);
                HeatmapTileProvider heatmapTileProvider;
                heatmapTileProvider = new HeatmapTileProvider.Builder()
                        .weightedData(addheatmap())
                        .radius(50)
                        .gradient(gradient)
                        .build();
                TileOverlayOptions tileoverlayoptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
               mMap.addTileOverlay(tileoverlayoptions);
               // mMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapTileProvider));

               //tileoverlay.clearTileCache();
                //Toast.makeText(this,"added heatmap",Toast.LENGTH_SHORT).show();
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

        private void  user_location(){
                Db = FirebaseDatabase.getInstance().getReference("Alert_up").child("user_tracking").child("user_location");
                Db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        try {
                                                dataSnapshot.getChildren();
                                                String nameDataDefault = "default";
                                                final String Name = dataSnapshot1.child("name").getValue().toString();
                                                final String latitude = (dataSnapshot1.child("last_latitude").getValue().toString());
                                                final String longitude = (dataSnapshot1.child("last_longitude").getValue().toString());

                                                double lati = Double.parseDouble(latitude);
                                                double longi = Double.parseDouble(longitude);
                                                LatLng LatLng = new LatLng(lati, longi);


                                                //  geo_ID = Name;
                                                // latlng1 = LatLng;
                                                String title = "Name:\t" + Name;
                                                //float[] colours = { BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED /* etc */ };
                                                // BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)]);
                                                // Marker marker =  mMap.addMarker(new MarkerOptions().position(LatLng).title(title).icon( BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));


                                                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.s);
                                                Bitmap a=bitmapdraw.getBitmap();
                                                Bitmap aMarker = Bitmap.createScaledBitmap(a, 100, 140, false);
                                               // mMarkerD  = mMap.addMarker(new MarkerOptions()
                                                 //               .position(new LatLng(lati4, longi4))

                                                MarkerOptions markerOptions = new MarkerOptions().position(LatLng)
                                                        .title("user")
                                                        .alpha(0.9f)
                                                        .snippet(latitude+" "+longitude)
                                                         .rotation(180.0f)
                                                         .flat(true)

                                                        .icon(BitmapDescriptorFactory.fromBitmap(aMarker))
                                                        .draggable(true);

                                               // mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng));


                                                if (mCustomerMarker != null) {

                                                        mCustomerMarker.remove();
                                                }

                                                mCustomerMarker = mMap.addMarker(markerOptions);



                                               //mMap.addMarker(markerOptions);
                                              //  markerOptions.remove();
                                             //   mMap.clear();

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


        // [END maps_poly_activity_style_polygon]
}
