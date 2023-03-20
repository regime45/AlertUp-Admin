package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.classified_zone.databinding.ActivityPoint1Binding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class point_1 extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityPoint1Binding binding;
    Button next;
    Button save, refresh;

    Polygon polygon = null;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private Polyline mPolyline;
    TextView area;

    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    private final static int ALPHA_ADJUSTMENT = 0x70000000;


    private float GEOFENCE_RADIUS = 50;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    BottomNavigationView navigationView, mapview;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_point1);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        area = findViewById(R.id.area);
        save= findViewById(R.id.save);
        refresh= findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                String names = getIntent().getStringExtra("key" );
                DatabaseReference dbRef = database.getReference("poly").child("tline").child(names);
                dbRef .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        String lon4 = snapshot.child("plon4").getValue().toString();
                        if (lon4  != null){
                            reload(lon4);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent amphibiansActivityIntentsss = new Intent(point_1.this, list_of_disease.class);
                startActivity(amphibiansActivityIntentsss);
                Toast.makeText(point_1.this, "Succesfully save", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView = findViewById(R.id.bottom_navigation);
        mapview = findViewById(R.id.maps);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

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
                        Intent amphibiansActivityIntentssss = new Intent(point_1.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntentssss);


                        return true;
                    case R.id.edits:

                        Intent amphibiansActivityIntents = new Intent(point_1.this, editActivity.class);
                        startActivity(amphibiansActivityIntents);

                        return true;
                    case R.id.homes:
                         Intent amphibiansActivityIntentsssss = new Intent(point_1.this, MainActivity.class);
                         startActivity(amphibiansActivityIntentsssss);
                        return true;
                    case R.id.info:
                         Intent ints = new Intent(point_1.this, Report_Activity.class);
                          startActivity(ints);
                        return true;
                    case R.id.creates:
                         Intent amphibiansActivityIntentsss = new Intent(point_1.this, list_of_disease.class);
                        startActivity(amphibiansActivityIntentsss);
                        return true;
                }
                return true;
            }
        });

        String nam= getIntent().getStringExtra("key" );


        // Toast.makeText(this, marker.getId(), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("keys", nam);
        editor.commit();





    }

    private void reload(String lon4) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String nam= getIntent().getStringExtra("key" );
        DatabaseReference dbRef = database.getReference("poly").child("tline").child(nam);
        dbRef .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue().toString();
                String lat1 = snapshot.child("plat1").getValue().toString();
                String lon1 = snapshot.child("plon1").getValue().toString();
                String lat2 = snapshot.child("plat2").getValue().toString();
                String lon2 = snapshot.child("plon2").getValue().toString();
                String lat3 = snapshot.child("plat3").getValue().toString();
                String lon3 = snapshot.child("plon3").getValue().toString();
                String lat4 = snapshot.child("plat4").getValue().toString();
                String lon4 = snapshot.child("plon4").getValue().toString();


                Double lati1 = Double.valueOf(lat1);
                Double longi1 = Double.valueOf(lon1);
                Double lati2 = Double.valueOf(lat2);
                Double longi2 = Double.valueOf(lon2);
                Double lati3 = Double.valueOf(lat3);
                Double longi3 = Double.valueOf(lon3);
                Double lati4 = Double.valueOf(lat4);
                Double longi4 = Double.valueOf(lon4);

                LatLng poly1 = new LatLng(lati1, longi1);
                LatLng poly2 = new LatLng(lati2, longi2);
                LatLng poly3 = new LatLng(lati3, longi3);
                LatLng poly4 = new LatLng(lati4, longi4);

                mMarkerA= mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lati1, longi1))
                     );


                mMarkerC  = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lati3, longi3))

                );


                mMarkerB= mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lati2, longi2))
                );


                mMarkerD  = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lati4, longi4))

                );
                mMarkerA.remove();
                mMarkerB.remove();
                mMarkerC.remove();
                mMarkerD.remove();
                showDistance();
                updatePolyline();
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poly4, 14));

                //mMap.addMarker(new MarkerOptions().position(poly1).title(name));
                // mMap.addMarker(new MarkerOptions().position(poly2).title(name));
                // mMap.addMarker(new MarkerOptions().position(poly3).title(name));
                //mMap.addMarker(new MarkerOptions().position(poly4).title(name));

                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poly1, 10));
                if (polygon != null) polygon.remove();


                PolygonOptions polygonOptions = new PolygonOptions().add(poly1, poly2, poly3, poly4, poly1)
                        .clickable(true);
                polygon = mMap.addPolygon(polygonOptions);
                int color = polygon.getStrokeColor() ^ 0x33FF0000;

                polygon.setFillColor(Color.RED - ALPHA_ADJUSTMENT);
            }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng casisang= new LatLng(8.133303764999694, 125.13788323894);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casisang, 16));


        // Add a marker in Sydney and move the camera

        enableUserLocation();
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

       // mMarkerA = mMap.addMarker(new MarkerOptions().position(new LatLng(8.12493040897172, 125.14118939638138)).draggable(true));
        //mMarkerB = mMap.addMarker(new MarkerOptions().position(new LatLng(8.129915344970389, 125.14194980263709)).draggable(true));
        //mMarkerC = mMap.addMarker(new MarkerOptions().position(new LatLng(8.129899413453915, 125.13623636215925)).draggable(true));
        //mMarkerD = mMap.addMarker(new MarkerOptions().position(new LatLng(8.125384462567121, 125.13576395809649)).draggable(true));
        mPolyline = mMap.addPolyline(new PolylineOptions().geodesic(true));
        /*

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.up);
        Bitmap a=bitmapdraw.getBitmap();
        Bitmap aMarker = Bitmap.createScaledBitmap(a, 100, 130, false);

        BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.down);
        Bitmap b=bitmapdraws.getBitmap();
        Bitmap bMarker = Bitmap.createScaledBitmap(b, 120, 120, false);


        mMarkerA= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(8.12493040897172, 125.14118939638138))
                .title("1")
                .icon(BitmapDescriptorFactory.fromBitmap(aMarker)).draggable(true));
        mMarkerA.showInfoWindow();

        mMarkerB = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(8.129915344970389, 125.14194980263709))
                .title("2")
                .icon(BitmapDescriptorFactory.fromBitmap(aMarker)).draggable(true));
        mMarkerB .showInfoWindow();


        mMarkerC  = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(8.129899413453915, 125.13623636215925))
                .title("3")
                .icon(BitmapDescriptorFactory.fromBitmap(aMarker)).draggable(true));
        mMarkerC .showInfoWindow();

        mMarkerD  = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(8.125384462567121, 125.13576395809649))
                .title("4")
                .icon(BitmapDescriptorFactory.fromBitmap(aMarker)).draggable(true));
        mMarkerD .showInfoWindow();

         */

       // showDistance();


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(50, 50, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Bitmap.createScaledBitmap(bitmap, 1000, 84, false);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showDistance() {
        double distance = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerC.getPosition());

        double A_B = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerB.getPosition());
        double B_C = SphericalUtil.computeDistanceBetween(mMarkerB.getPosition(), mMarkerC.getPosition());
        // set b
        double A_D = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerD.getPosition());
        double D_C = SphericalUtil.computeDistanceBetween(mMarkerD.getPosition(), mMarkerC.getPosition());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("poly").child("tline");

        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_APPEND);
        String sharedkey = sharedPreferences.getString("keys", "");
        double distancesss = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerC.getPosition());
        // Toast.makeText(this,"The markers are " + formatNumber(distance) + " apart", Toast.LENGTH_SHORT).show();
        myRef.child(sharedkey ).child("radius").setValue(formatNumber(distancesss));

        //A-C diagonal distance
       // double A_C =distance * 1000;
        //Toast.makeText(this,"The markers are " + formatNumber(distance) + " apart", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,"A_C: " + A_C , Toast.LENGTH_SHORT).show();

        //A-B
       // double A_B_m =  formatNumber(A_B) * 1000;
        //Toast.makeText(this,"The markers are " + formatNumber(A_B) + " apart", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this,"A_B: " + A_B_m , Toast.LENGTH_SHORT).show();

        //B-C
        //double B_C_m =  Double.valueOf(formatNumber(B_C)) * 1000;
        //Toast.makeText(this,"The markers are " + formatNumber(B_C) + " apart", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,"B_C: " + B_C_m, Toast.LENGTH_SHORT).show();

        //CALCULATIOM SET A
        double Set_a = distance + A_B+ B_C /2;
        //calcualte set a
        double Set_b = distance + A_D+ D_C /2;

        //Toast.makeText(this,"Set A result : " + Set_a, Toast.LENGTH_SHORT).show();
        // measure meter m2
        double  set_a_m1 = Set_a -distance ;
        double  set_a_m2 = Set_a - A_B;
        double  set_a_m3 = Set_a - B_C ;
        double  set_a_m4 = Set_a *  set_a_m1 * set_a_m2 * set_a_m3 ;

        // set b
        double  set_b_m1 = Set_b -distance ;
        double  set_b_m2 = Set_b - A_D;
        double  set_b_m3 = Set_b-  D_C ;
        double  set_b_m4 = Set_b *  set_b_m1 * set_b_m2 * set_b_m3 ;


        double set_a_m5 = Math.sqrt(set_a_m4) ;
        double set_b_m5 = Math.sqrt(set_b_m4) ;
        double finalcal = set_b_m5 +  set_a_m5;
        double finalcals = Math.sqrt(finalcal);


        DecimalFormat decfor = new DecimalFormat("0.000");
      //  area.setText(formatNumbers(finalcal ));
        Toast.makeText(this,"Set A  "+formatNumbers(set_a_m5 ), Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Result: "+formatNumber(distance), Toast.LENGTH_SHORT).show();






    }

    private String formatNumber(double distance) {
        String unit = "";
        if (distance < 1) {
            distance *= 1000;
            unit = "";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "";
        }

        return String.format("%.3f%s", distance, unit);
    }



    private String formatNumbers(double distance) {

            String unit = " m sq";
            if (distance < 1) {
                distance *= 10000000;
                unit = " mm sq";
            } else if (distance > 1000000) {
                distance /= 1000000;
                unit = " km sq";
            }

            return String.format("%1.1f%s", distance, unit);

    }

    private void updatePolyline() {
        mPolyline.setPoints(Arrays.asList(mMarkerA.getPosition(), mMarkerB.getPosition(),mMarkerC.getPosition(), mMarkerD.getPosition(), mMarkerA.getPosition()));
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
    public void onMarkerDrag(@NonNull @NotNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
        double lats=   marker.getPosition().latitude;
        double longs=   marker.getPosition().longitude;
        //showDistance();
        //updatePolyline();
        area.setText("latitude : "+lats+ "\nlongitude : "+longs);
        //Toast.makeText(this, String.valueOf(lats), Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, ""+lats+""+longs, Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("poly").child("tline");
        DatabaseReference newChildRef = myRef.push();

        FirebaseDatabase databases = FirebaseDatabase.getInstance();
        DatabaseReference myRefs = database.getReference("alerts_zone").child("classified_zone");


        String poly_name= getIntent().getStringExtra("name_disease" );
        String keysafe= getIntent().getStringExtra("keysafe" );


        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_APPEND);
        String sharedkey = sharedPreferences.getString("keys", "");

        //Toast.makeText(this, sharedkey, Toast.LENGTH_SHORT).show();

        String m0 = "m0";

        switch (marker.getId()){

            case "m0":
                myRef.child(sharedkey ).child("plat1").setValue( lats);
                myRef.child(sharedkey ).child("plon1").setValue( longs);
                myRef.child(sharedkey ).child("name").setValue(poly_name);
                myRef.child(sharedkey ).child("alert_message").setValue(keysafe);
                //Toast.makeText(this, marker.getId(), Toast.LENGTH_SHORT).show();
               // double distancesss = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerC.getPosition());
                // Toast.makeText(this,"The markers are " + formatNumber(distance) + " apart", Toast.LENGTH_SHORT).show();
               // myRef.child(sharedkey ).child("radius").setValue(formatNumber(distancesss));

                break;

            case "m1":
                myRef.child(sharedkey ).child("plat2").setValue( lats);
                myRef.child(sharedkey ).child("plon2").setValue( longs);
                //Toast.makeText(this, marker.getId(), Toast.LENGTH_SHORT).show();
               // double distancess = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerC.getPosition());
                // Toast.makeText(this,"The markers are " + formatNumber(distance) + " apart", Toast.LENGTH_SHORT).show();
                //myRef.child(sharedkey ).child("radius").setValue(formatNumber(distancess));
//
                break;

            case "m2":
                myRef.child(sharedkey ).child("plat3").setValue( lats);
                myRef.child(sharedkey ).child("plon3").setValue( longs);
                //double distance = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerC.getPosition());
               // Toast.makeText(this,"The markers are " + formatNumber(distance) + " apart", Toast.LENGTH_SHORT).show();
                //myRef.child(sharedkey ).child("radius").setValue(formatNumber(distance));
                //Toast.makeText(this, marker.getId(), Toast.LENGTH_SHORT).show();


                break;

            case "m3":
                myRef.child(sharedkey ).child("plat4").setValue( lats);
                myRef.child(sharedkey ).child("plon4").setValue( longs);
               // double distances = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerC.getPosition());
                // Toast.makeText(this,"The markers are " + formatNumber(distance) + " apart", Toast.LENGTH_SHORT).show();
                //myRef.child(sharedkey ).child("radius").setValue(formatNumber(distances));

                //Toast.makeText(this, marker.getId(), Toast.LENGTH_SHORT).show();
                // load autopmode
                /*
                String names = getIntent().getStringExtra("key" );
                DatabaseReference dbRef = database.getReference("poly").child("tline").child(sharedkey);
                dbRef .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        String lon4 = snapshot.child("plon4").getValue().toString();
                        String lon3 = snapshot.child("plon3").getValue().toString();
                        String lon2= snapshot.child("plon2").getValue().toString();
                        String lon1 = snapshot.child("plon1").getValue().toString();
                        if ((lon4 != null) && (lon3 != null) && (lon2 != null) && (lon1 != null)){
                            reload(lon4);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                 */



                break;


        }

        String strlat =String.valueOf(lats);
        String strlons=String.valueOf(longs);
        if (sharedkey!= null) {
            myRefs.child(sharedkey).child("latitude").setValue(strlat);
            myRefs.child(sharedkey).child("longitude").setValue(strlons);

        }





    }

    @Override
    public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

    }

    @Override
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
        //addCircle(latLng, 20);
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

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.up);
        Bitmap a=bitmapdraw.getBitmap();
        Bitmap aMarker = Bitmap.createScaledBitmap(a, 100, 140, false);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("1")
                .alpha(0.9f)

                .snippet("Population: 4,137,400")

                .rotation(180.0f)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(aMarker))
                .draggable(true);


        mMap.addMarker(markerOptions);
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

}