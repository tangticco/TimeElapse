package edu.fandm.ztang.timeelapse;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback{
    private String TAG = "TimeElapse";

    private Camera camera;
    private MediaRecorder recorder;
    private SurfaceHolder holder;
    private boolean recording = false;
    private File videoFile;

    private final int PERMS_REQUEST_CODE = 1;

    private double fpsRate = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check whether got the permissions
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permissions denied");
            //Request runtime permissions
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
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

        //set the spinner for adjusting fps rate
        Spinner fpsController = (Spinner)findViewById(R.id.spinner);
        final List<Double> fpsRateMenu = new ArrayList<>();

        fpsRateMenu.add(0.5);
        fpsRateMenu.add(1.0);
        fpsRateMenu.add(5.0);
        fpsRateMenu.add(10.0);


        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this, android.R.layout.simple_spinner_item, fpsRateMenu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fpsController.setAdapter(adapter);

        fpsController.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fpsRate = fpsRateMenu.get(position);
                Toast.makeText(MainActivity.this, "Recording FPS to " + String.valueOf(fpsRate), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               fpsRate = 5;
            }
        });

        camera = getCameraInstance();
        camera.setDisplayOrientation(270);
        //Set up recorder
        recorder = new MediaRecorder();
        camera.unlock();
        recorder.setCamera(camera);


//        initRecorder();

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);


        Toast.makeText(this, "Please click to start recording", Toast.LENGTH_SHORT).show();

    }
    /**
     * A controller to open the thumbnail grid view of videos
     * @param v
     */
    public void showAlbum(View v){

        Intent intent = new Intent(this, VideoThumbGridAvitivity.class);
        startActivity(intent);

    }


    //Initialize the recorder
    private void initRecorder() {

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        recorder.setOrientationHint(180);

        // Create the File where the photo should go
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Get the directory for storage
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath()+"/TimeElapse");
        if(!Dir.exists()){ // Create one if there is not
            Dir.mkdirs();
        }
        videoFile = new File(Dir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");


        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        recorder.setOutputFile(videoFile.toString());
        recorder.setMaxDuration(1000000); // 50 seconds
        recorder.setMaxFileSize(500000000); // Approximately 500 megabytes

        recorder.setCaptureRate(fpsRate);
    }

    // Prepare the recorder
    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    // When click on the surface view
    public void onClick(View v) {
        if (recording) {
            recorder.stop();
            recording = false;

            // Let's initRecorder so we can record again
        } else {
            initRecorder();
            prepareRecorder();

            recording = true;
            recorder.start();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
        finish();
    }



    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}



