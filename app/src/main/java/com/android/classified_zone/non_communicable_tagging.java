package com.android.classified_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class non_communicable_tagging extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public  static final int RequestPermissionCode  = 1 ;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Uri mImageUri;
    String sImage;
    String currentPhotoPath;
    private CircleImageView mImageView;
    private StorageReference mStorageRef;
    private ProgressBar mProgressBar;

    private EditText editText, purok, number, barangay;
    private Button register, saveit, profile;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RadioGroup rgGender;
    TextView status;
    // Declare RadioButton object references for Male and Female
    RadioButton rbMale, rbFemale;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geotagging);
        EnableRuntimePermission();

        sharedPreferences = getSharedPreferences("pref", 0);
        // Here pref is the name of the file and 0 for private mode.
        // To read values from SharedPreferences, you can use getInt() method on
        // sharedPreferences object.
        int genderSP = sharedPreferences.getInt("genderSP", 3);
        // on sharedPreferences object.
        editor = sharedPreferences.edit();

        mImageView = (CircleImageView) findViewById(R.id.imageView);
        profile = findViewById(R.id.profile);
        status = findViewById(R.id.status);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("covid_tool");


        BottomSheetDialog dialog = new BottomSheetDialog(non_communicable_tagging.this);
        dialog.setContentView(R.layout.regis);
        dialog.setCanceledOnTouchOutside(false);

        editText = dialog.findViewById(R.id.edittext);
        number = dialog.findViewById(R.id.number);
        barangay = dialog.findViewById(R.id.barangay);
        purok= dialog.findViewById(R.id.purok);
        saveit = dialog.findViewById(R.id.saveit);

        rgGender = dialog.findViewById(R.id.rgGender);
        rbMale = dialog.findViewById(R.id.rbMale);
        rbFemale = dialog.findViewById(R.id.rbFemale);
        FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

        dialog.show();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

            }
        });

        if(genderSP == 1){
            rbMale.setChecked(true);
        }else if(genderSP == 0){
            rbFemale.setChecked(true);
        }

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbMale){
                    // Put the value 1 in the key "genderSP" using editor object
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("geotag_places").child("user").child("non_communicable");
                    //DatabaseReference newChildRef = myRef.push();
                    //String key = newChildRef.getKey();

                    String namess = editText.getText().toString();

                    //String numbersss = number.getText().toString();
                    //  String namess =  "+63"+numbersss ;

                    if (namess  != null) {

                        myRef.child(namess).child("gender").setValue("Male");
                    }
                    editor.putInt("genderSP", 1);
                }else if(checkedId == R.id.rbFemale){
                    // Here, put the value 0 in the key "genderSP" using editor
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("geotag_places").child("user").child("non_communicable");
                    //DatabaseReference newChildRef = myRef.push();
                    //String key = newChildRef.getKey();
                    //Toast.makeText(Create_Account.this, "Female", Toast.LENGTH_SHORT).show();

                    String namess = editText.getText().toString();
                    if (namess != null) {

                        myRef.child(namess).child("gender").setValue("Female");
                    }
                    editor.putInt("genderSP",0);
                }
                // Now, to save the changes call commit() on editor.
                editor.commit();
            }
        });

        saveit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(non_communicable_tagging.this);
                builder.setTitle("Update");
                builder.setMessage("Ok?");
                // Intent intent = new Intent(view.getContext(), Fire_Activity.class);
                // retrieve display dimensions


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = editText.getText().toString();
                        String puroknum =purok.getText().toString();
                        String numbers = number.getText().toString();
                        String local = barangay.getText().toString();



                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("geotag_places").child("user").child("non_communicable");

                        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyyMMdd");

                        //expiration date
                        long cutoff = new Date().getTime()+ TimeUnit.MILLISECONDS.convert(14, TimeUnit.DAYS);
                        String limit_date = dateFormatGmt.format(new Date(cutoff));
                        Long expire = Long.parseLong( limit_date);

                        // Current date
                        // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        String currentDate = dateFormatGmt.format(new Date());
                        Long current = Long.parseLong(currentDate);

                        //date time creation
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy 'at' hh:mm aa");
                        String currentDateandTime = sdf.format(new Date());

                        String onetimekey = editText.getText().toString();

                        if (onetimekey  != null) {

                            myRef.child(onetimekey ).child("name").setValue(name);
                            myRef.child(onetimekey ).child("contact").setValue(numbers);
                            myRef.child(onetimekey ).child("barangay").setValue(local);
                            myRef.child(onetimekey ).child("purok").setValue(puroknum);
                            myRef.child(onetimekey ).child("created_At").setValue(currentDateandTime);
                            //current time
                            //myRef.child(onetimekey ).child("current").setValue(currentDate);
                            //myRef.child(onetimekey ).child("expiry").setValue(limit_date);
                            //expire date
                        }

                        Toast.makeText(getApplicationContext(),"Sucessfully updated",Toast.LENGTH_SHORT).show();
                        // Intent intent = new Intent(Create_Account.this, Create_Account.class);
                        // intent.putExtra("keymessage", sText);
                        //startActivity(intent);

                        dialog.dismiss();
                        askCameraPermissions();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "please fill those required items",Toast.LENGTH_SHORT).show();


                    }
                });

                builder.show();
                // realance pleaase



            }
        });


        requestAppPermissions ();


    }
    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(non_communicable_tagging.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(non_communicable_tagging.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(non_communicable_tagging.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }




    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }





    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //String ss = String.valueOf(data.getData());
            Uri uri = data.getData();
            Toast.makeText(this, "Picture taken!" + uri, Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // initialize byte stream
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                // Initialize byte array
                byte[] bytes = stream.toByteArray();
                // get base64 encoded string
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                // set encoded text on textview
                //textView.setText(sImage);


            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = Base64.decode(sImage, Base64.DEFAULT);
            // Initialize bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // set bitmap on imageView
            mImageView.setImageBitmap(bitmap);
            // Toast.makeText(getApplicationContext(),  sImage,Toast.LENGTH_SHORT).show();
            String ss = sImage;
            //savesdata(ss);
            ///// display image after decoding

            //  Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/geofencing-531cb.appspot.com/o/uploads%2F1645952134032.null?alt=media&token=19459254-2a0a-4003-8c57-fdee1f0f50b6").into(mImageView);
        }

        // Initialize bitmap
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                // set imgae
                //mImageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                String uriss = String.valueOf(f.getName());
                uploadImageToFirebase(uriss, contentUri);
                String supers = String.valueOf(Uri.fromFile(f));
                // Toast.makeText(geotagging.this, supers, Toast.LENGTH_SHORT).show();


                try {
                    Bitmap bitmaps = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    // initialize byte stream
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // compress Bitmap
                    bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // Initialize byte array
                    byte[] bytes = stream.toByteArray();
                    // get base64 encoded string
                    sImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                    // set encoded text on textview
                    //textView.setText(sImage);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = Base64.decode(sImage, Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmaps = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // set bitmap on imageView
                mImageView.setImageBitmap(bitmaps);

                String ss = sImage;
                //savesdata(ss);
                //
            }
        }

    }

    // camera
    private void uploadImageToFirebase(String name, Uri contentUri) {

        if (contentUri != null) {
            final StorageReference image = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExt(contentUri));
            image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());

                            String namess = editText.getText().toString();
                            @SuppressLint("WrongConstant") SharedPreferences sharedPreferencess = getSharedPreferences("MySharedPref", MODE_APPEND);
                            String lat = sharedPreferencess.getString("last_latitude", "");
                            String lon = sharedPreferencess.getString("last_longitude", "");
                            // Toast.makeText(geotagging.this, lon , Toast.LENGTH_SHORT).show();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("geotag_places").child("user").child("non_communicable");
                            // String onetimekey = "eiiw" ;
                            if (namess != null) {
                                //Strng uploadId = mEditTextFileName.getText().toString().trim();
                                myRef.child(namess).child("imageURL").setValue(String.valueOf(uri));
                                myRef.child(namess).child("id").setValue(namess);
                                // myRef.child(name).child("Radius").setValue(contact);
                                myRef.child(namess).child("last_latitude").setValue(lat);
                                myRef.child(namess).child("last_longitude").setValue(lon);
                            }
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("image_camera", String.valueOf(uri));
                            editor.putString("id", namess);
                            editor.commit();

                        }
                    });

                    Toast.makeText(non_communicable_tagging.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(non_communicable_tagging.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    String ss=   df.format(progress);
                    status.setText(ss+" %");
                    mProgressBar.setProgress((int) progress);
                }
            });
        }
    }

    private void requestAppPermissions () {

        Dexter.withActivity(non_communicable_tagging.this)

                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            //interact.downloadImage(array);
                            startService(new Intent(non_communicable_tagging.this, ForegroundService.class));
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            showSettingsDialog();
                            //finish();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(non_communicable_tagging.this);

        builder.setTitle("Need Permissions");

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");

        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void openSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

        Uri uri = Uri.fromParts("package", getPackageName(), null);

        intent.setData(uri);

        startActivityForResult(intent, 101);

    }




}