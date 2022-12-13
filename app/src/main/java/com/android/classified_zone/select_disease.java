package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class select_disease extends AppCompatActivity  {

    Button next;
    EditText disease, discreption, safety;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_disease);
        next = findViewById(R.id.select_nav);
        disease = findViewById(R.id.disease_name);
        discreption = findViewById(R.id.discreption);
        safety = findViewById(R.id.safety);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("alerts_zone").child("list_of_disease");

                DatabaseReference newChildRef = myRef.push();
                String key = newChildRef.getKey();
                // if (isAllFieldsChecked) {

                String add_disease = disease.getText().toString();
                String add_description= discreption.getText().toString();
                String add_safety= safety.getText().toString();

                if (add_disease!= null) {

                    myRef.child(add_disease ).child("disease_name").setValue(add_disease);
                    myRef.child(add_disease ).child("disease_description").setValue(add_description);
                    myRef.child(add_disease ).child("alert_message").setValue(add_safety);

                    //  }
                    Toast.makeText(select_disease.this, " save successfully...", Toast.LENGTH_SHORT).show();
                }

                myRef.orderByChild("Geo_Name").equalTo(add_disease).addListenerForSingleValueEvent (new ValueEventListener()  {
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



                Intent intent = new Intent(select_disease.this, list_of_disease.class);

                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.casisang, menu);

        return true;
    }




}