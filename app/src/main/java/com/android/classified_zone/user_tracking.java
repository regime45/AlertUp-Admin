package com.android.classified_zone;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class user_tracking extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F13F3F")));
      //  getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFFFF'>Vacuna Scanner</font>"));


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.summary:
               // Intent amphibiansActivityIntents = new Intent(Vaccine_Home.this, vaccine_data.class);
               // startActivity(amphibiansActivityIntents);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

     */





    @Override
    public void handleResult(Result rawResult) {
        String data = rawResult.getText().toString();
       // Toast.makeText(this, data , Toast.LENGTH_SHORT).show();


        //String something = rawResult.getText();
        // String newData = something +"MtSMhQ2n7iWcpnhvSzk";
        Intent intent = new Intent(user_tracking.this, geotagging.class);
       intent.putExtra("SCAN_RESULTS", data);
        // intent.putExtra("date",currentDateandTime );

        //setResult(RESULT_OK, intent);
         startActivityForResult(intent, 100);
        // finish();

    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }
}