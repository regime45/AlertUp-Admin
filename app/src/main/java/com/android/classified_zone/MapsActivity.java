package com.android.classified_zone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.GeofencingClient;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private float GEOFENCE_RADIUS = 50;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private LatLng LatLng;
    private DatabaseReference Db;
    private String lats, longs, rads, nam, des, safe;


    //  private static int TIME_OUT = 9000; //Time to launch the another activity

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

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);

        FirebaseDatabase firebaseDatabase;

        // creating a variable for our Database
        // Reference for Firebase.
        DatabaseReference databaseReference;




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

        enableUserLocation();

        mMap.setOnMapLongClickListener(this);

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
                    //String radius = childSnapshot.child("Radius").getValue().toString();
                    Integer radius = Integer.valueOf(childSnapshot.child("Radius").getValue().toString());


                    String lat = childSnapshot.child("latitude").getValue().toString();
                    String lon = childSnapshot.child("longitude").getValue().toString();

                    Double lati = Double .valueOf(lat);
                    Double  longi = Double .valueOf(lon);
                    String radi = String.valueOf(radius);

                    String title = "Name:\t" +Name +"\tRadius:\t" +radi ;


                    LatLng LatLng = new LatLng(lati, longi);

                    Marker marker =  mMap.addMarker(new MarkerOptions().position(LatLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

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


    @Override
    public void onMapLongClick(LatLng latLng ) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //mMap.clear();
                addMarker(latLng);
                addCircle(latLng);
                //addGeofence(latLng, GEOFENCE_RADIUS);
                //Toast.makeText(MapsActivity.this, "latitude  " + latLng.latitude+"   longitude  "+latLng.longitude, Toast.LENGTH_SHORT).show();

                double longitude = latLng.longitude;
                double latitude = latLng.latitude;

                lats = String.valueOf(latitude);
                longs = String.valueOf(longitude);
                nam= getIntent().getStringExtra("name_disease" );
                rads = getIntent().getStringExtra("keyradius" );
                des= getIntent().getStringExtra("description_disease" );
                safe= getIntent().getStringExtra("keysafe" );

                        // Toast.makeText(MapsActivity.this, "name  " + mes+"   radius  "+rad, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MapsActivity.this, CreateActivity.class);

                // intent.putExtra("keyname",nam);
                //intent.putExtra("keyradius",rads);
                //intent.putExtra("keylat",lats);
                // intent.putExtra("keylon",longs);
                // startActivity(intent);
                //setResult(RESULT_OK,intent);
                //startActivityForResult(intent, 100);
                // finish();

                Handler h = new Handler();
                // ADD delayed time
                h.postDelayed(r, 5000); // will be delayed for 1.5 seconds


                /*

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference("data");
                DatabaseReference newChildRef = dbRef.push();
                String key = newChildRef.getKey();
                //hotspot();
               // startActivity(getIntent());

                if (key != null) {


                    dbRef.child(key).child("latitude").setValue(latLng.latitude);
                    dbRef.child(key).child("longitude").setValue(latLng.longitude);

                }

                Intent amphibiansActivityIntent = new Intent(MapsActivity.this, Fire_Activity.class);
                startActivity(amphibiansActivityIntent);



                DatabaseReference dbRefd = database.getReference("datas");

                DatabaseReference newChildRefd = dbRefd.push();
                String keys = newChildRefd.getKey();
                //String key="1234567sdfsf8";

                if (keys != null) {
                    String key="1234567sdfsf8";


                    dbRefd.child(keys).child("latitude").setValue(latLng.latitude);
                    dbRefd.child(keys).child("longitude").setValue(latLng.longitude);

                }
                //finish();
                //startActivity(getIntent());

                */
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }

        } else {



        }

    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            // if you are redirecting from a fragment then use getActivity() as the context.
            Intent intent = new Intent (MapsActivity.this,  CreateActivity.class);
            // intent.putExtra("SCAN_RESULT", something);
            // intent.putExtra("date",currentDateandTime );
            intent.putExtra("keyname",nam);
            intent.putExtra("keyradius",rads);
            intent.putExtra("keylat",lats);
            intent.putExtra("keylon",longs);
            intent.putExtra("keydes",des);
            intent.putExtra("keysafet",safe);
            setResult(RESULT_OK,intent);
            startActivityForResult(intent, 1);
            finish();
        }
    };


/*

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
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
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
*/
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

    private void addMarker(LatLng latLng) {
        //MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        MarkerOptions MarkerOptions =  new MarkerOptions().position(latLng).title("New marker").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


        mMap.addMarker(MarkerOptions);
    }

    private void addCircle(LatLng latLng) {

        String rad = getIntent().getStringExtra("keyradius" );
        String rads =String.valueOf(rad);
        Integer radss  = Integer.parseInt(rads);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radss);
        circleOptions.strokeColor(Color.argb(255, 52, 234,53));
        circleOptions.fillColor(Color.argb(64, 75, 136,162));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }



}
