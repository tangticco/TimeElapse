package edu.fandm.ztang.timeelapse;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String TAG = "TimeElapse";


    static final int REQUEST_TAKE_PHOTO = 1;
    private final int PERMS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check whether got the permissions
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            //Request runtime permissions
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, PERMS_REQUEST_CODE);

        }

        //Create a folder for this app
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            // Get the directory for storage
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/TimeElapse");
            if (!Dir.exists()) { // Create one if there is not
                Dir.mkdirs();
            }
        }
    }


    public void captureImage(View v){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            // Get the directory for storage
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath()+"/TimeElapse");
            if(!Dir.exists()){ // Create one if there is not
                Dir.mkdirs();
            }
            File photoFile = new File(Dir, "TE" + timeStamp + ".jpg");

            Uri photoURI = FileProvider.getUriForFile(this,
                    "edu.fandm.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }



    public void captureVideo(View v){
        Intent i = new Intent(this, CaptureVideo.class);
        startActivity(i);
    }


}



