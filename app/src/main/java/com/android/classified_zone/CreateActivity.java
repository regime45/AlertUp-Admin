package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button  send, add;
    private EditText  lat, lon, risk_name;
    private EditText editText,   editTextRadius, description, purok, diseases, safety;
    boolean isAllFieldsChecked = false;
    String store_risk, temp4, lati, longi, radiusnumber,  diameter,name , des, safey;

    boolean Checked = false;

    String[] pors= {"Purok 1","Purok 2","Purok 3","Purok 4","Purok 5","Purok 6",
            "Purok 7","Purok 8","Purok 9","Purok 10","Purok 11","Purok 12",
            "Purok 13","Purok 14","Purok 15","Purok 16","Purok 17","Purok 18",
            "Purok 19","Purok 20","Purok 21", "Purok 22",
            "Purok 23","Purok 24","Purok 25","Purok 26","Purok 27","Purok 28",
            "Purok 29","Purok 30","Purok 31", "Purok 32", "Purok 33",
            "Purok 34", "Purok 35"};
    Spinner poruk;



    private View viewMerk;
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        send = findViewById(R.id.send);
        add= findViewById(R.id.select_nav);


        saveZone();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateActivity.this, list_of_disease.class);


                startActivity(intent);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CreateActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to submit this data...?");
                //   isAllFieldsChecked = CheckAllFields();
                //Checked = Checked();
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                //if ( isAllFieldsChecked) {
                Intent intent = new Intent(CreateActivity.this, list_of_disease.class);
                startActivity(intent);

                startActivity(intent);
                        Toast.makeText(CreateActivity.this, "Yey! You have successfully created a classified zone"  , Toast.LENGTH_SHORT).show();
                fine();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });


        navigationView = findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        //navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.view:
                        Intent amphibiansActivityIntent = new Intent(CreateActivity.this, ViewActivity.class);
                        startActivity(amphibiansActivityIntent);

                        break;

                    case R.id.edits:
                        Intent amphibiansActivityIntentss = new Intent(CreateActivity.this, editActivity.class);
                        startActivity(amphibiansActivityIntentss);


                        break;

                    case R.id.homes:
                        Intent amphibiansActivityIntentssss = new Intent(CreateActivity.this, MainActivity.class);
                        startActivity(amphibiansActivityIntentssss);


                        break;

                    case R.id.info:
                        Intent amphibiansActivityIntents = new Intent(CreateActivity.this, Report_Activity.class);
                        startActivity(amphibiansActivityIntents);


                        break;

                    case R.id.creates:
                        Intent amphibiansActivityIntentsss = new Intent(CreateActivity.this, list_of_disease.class);
                        startActivity(amphibiansActivityIntentsss);

                        break;

                }

                return true;
            }
        });


        final Spinner purok
                = (Spinner)findViewById(R.id.purok);


        ArrayAdapter<String> ad_purok
                = new ArrayAdapter<String>(
                this,R.layout.spinner_dropdown_layout,pors);

        ad_purok.setDropDownViewResource(
                R.layout.spinner_dropdown_layout);

        // Apply the adapter to the spinner
        purok.setAdapter(ad_purok);

        purok.setOnItemSelectedListener(this);


    }


    private void saveZone() {


         lati = getIntent().getStringExtra("keylat" );
         longi = getIntent().getStringExtra("keylon" );



        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);
        lat.setText(lati);
        lon.setText(longi);

        name  = getIntent().getStringExtra("keyname" );
        des  = getIntent().getStringExtra("keydes" );
        String disease = getIntent().getStringExtra("disease" );
        diameter = getIntent().getStringExtra("keyradius" );
        safey = getIntent().getStringExtra("keysafet" );


        editText =  findViewById(R.id.message);
        diseases =   findViewById(R.id.disease_name);
        editTextRadius =  findViewById(R.id.radius);
        description = findViewById(R.id.description);
        safety = findViewById(R.id.safety);
        //purok= findViewById(R.id.purok);

        String name_disease = getIntent().getStringExtra("name_disease" );


        //Toast.makeText(this, name_disease, Toast.LENGTH_SHORT).show();

        editText.setText(name_disease);
        description .setText( des);
        diseases.setText(name);
        safety.setText(safey);
        editTextRadius.setText(diameter);


    }

    private void fine() {
        //isAllFieldsChecked = CheckAllFields();
        //Intent intent = new Intent();

        String message = editText.getText().toString();
         radiusnumber = editTextRadius.getText().toString();
        String descript = description.getText().toString();
        //String purk = purok.getText().toString();
        String lati = getIntent().getStringExtra("keylat" );
        String longi = getIntent().getStringExtra("keylon" );

        lat.setText(lati);
        lon.setText(longi);
        editText.setText(name);
        description .setText(des);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("alerts_zone").child("classified_zone");

        DatabaseReference newChildRef = myRef.push();
        String key = newChildRef.getKey();
       // if (isAllFieldsChecked) {

            if (key!= null) {

                myRef.child(key).child("Geo_Name").setValue(name);
                myRef.child(key).child("Radius").setValue(radiusnumber);
                myRef.child(key).child("Description").setValue(descript);
                myRef.child(key).child("Purok").setValue(temp4 );
                myRef.child(key).child("latitude").setValue(lati);
                myRef.child(key).child("alert_message").setValue(safey);
                myRef.child(key).child("longitude").setValue(longi);

          //  }
            Toast.makeText(this, " save successfully...", Toast.LENGTH_SHORT).show();
        }



        myRef.orderByChild("Geo_Name").equalTo(message).addListenerForSingleValueEvent (new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    //   Toast.makeText(Fire_Activity.this, "naa na  "  , Toast.LENGTH_LONG).show();
                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private boolean CheckAllFields() {


        if (editText.length() == 0) {
            editText.setError("This field is required");
            findViewById(R.id.message).setBackgroundColor(Color.TRANSPARENT);
            findViewById(R.id.message).requestFocus();
            // startActivity(getIntent());
            return false;
        }




        if (editText.length() == 0) {
            editText.setError("This field is required");
            findViewById(R.id.message).setBackgroundColor(Color.TRANSPARENT);
            findViewById(R.id.message).requestFocus();
            // startActivity(getIntent());
            return false;
        }

        if (editTextRadius .length() == 0) {
            editTextRadius .setError("This field is required");
            findViewById(R.id.radius).setBackgroundColor(Color.TRANSPARENT);
            findViewById(R.id.radius).requestFocus();
            return false;
        }

        if (lat.length() == 0) {
            lat.setError("required");
            findViewById(R.id.message).setBackgroundColor(Color.TRANSPARENT);
            findViewById(R.id.message).requestFocus();
            return false;
        }

        if (lon.length() == 0) {
            lon.setError("required");

            return false;
        } else if (lon.length() < 8) {
            lon.setError("longitude must be  minimum 8 of characters");
            return false;
        }

        // after all validation return true.
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ImageURLs", pors[i] );
        temp4= pors[i];
        editor.commit();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}