package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class fire_to_pdf extends AppCompatActivity {
    DatabaseReference Db;
    Button oldPrintBtn;
    EditText oldPrintEditText;
    DataTable dataTable;
    DataTableHeader header;

    String Geo_Name, Description, alert_message, Purok, Radius;
   Double latitude;
   Double longitude;

    ArrayList<DataTableRow> rows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_to_pdf);


        dataTable = findViewById(R.id.data_table);


           header = new DataTableHeader.Builder()
                        .item("Disease Name", 7)
                .item("Radius", 6)
                .item("Purok", 4)
                   .item("latitude", 6)
                .item("longitude", 7)
                .item("alert_message", 10)
                .item("Description", 13)
                .build();
        loadtable();






    }



    private void printPdf(String toString) {
        Db = FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone");
        Db.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Geo_Name = (String) snapshot.child("Geo_Name").getValue();
               // Radius = (String) snapshot.child("Geo_Name").getValue();
               // Description = (String) snapshot.child("Geo_Name").getValue();
               // latitude = (Double) snapshot.child("GeoGeo_Name").getValue();
                //longitude = (Double) snapshot.child("Geo_Name").getValue();
                ///alert_message = (String) snapshot.child("Geo_Name").getValue();


                PdfDocument myPdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint forLinePaint = new Paint();
                forLinePaint.setColor(Color.rgb(0, 50, 250));
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 100).create();
                PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
                Canvas canvas = myPage.getCanvas();

                paint.setTextSize(15.5f);
                paint.setColor(Color.rgb(0, 50, 250));

                canvas.drawText("Name" + Geo_Name, 20, 30, paint);

                myPdfDocument.finishPage(myPage);

                File file = new File(fire_to_pdf.this.getExternalFilesDir("/Documents"), toString + "sd");


                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myPdfDocument.close();



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    private void loadtable() {
        Db = FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone");

        Db.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    DataTableRow row = new DataTableRow.Builder()
                            .value(String.valueOf(myDataSnapshot.child("Geo_Name").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("Radius").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("Purok").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("latitude").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("longitude").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("alert_message").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("Description").getValue()))
                            .build();
                    rows.add(row);
                }
                dataTable.setHeader(header);
                dataTable.setRows(rows);
                dataTable.setPadding(5, 5, 5, 0);
                dataTable.inflate(fire_to_pdf.this);

                //Toast.makeText(fire_to_pdf.this, header.toString(), Toast.LENGTH_LONG).show();


                //String ss= rows.getText().toString();

            }

            @Override
            public void onCancelled( @NotNull DatabaseError error) {

            }
        });
    }
}