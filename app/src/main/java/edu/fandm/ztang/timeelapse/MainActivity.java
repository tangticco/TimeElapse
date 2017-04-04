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
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback{
    private String TAG = "TimeElapse";

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;

    private final int PERMS_REQUEST_CODE = 1;

    private double fpsRate = 0.1;

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

        //Set up recorder
        recorder = new MediaRecorder();
        initRecorder();

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);


        final SeekBar fpsController = (SeekBar)findViewById(R.id.seekBar);
        fpsController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(fpsController.getProgress() != 0){
                    fpsRate = (fpsController.getProgress()/fpsController.getMax()) * 6 + 2 ;
                }
                Toast.makeText(MainActivity.this, "FPS set to " + String.valueOf(fpsRate), Toast.LENGTH_SHORT).show();
            }
        });

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
        recorder.setOrientationHint(90);

        // Create the File where the photo should go
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Get the directory for storage
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath()+"/TimeElapse");
        if(!Dir.exists()){ // Create one if there is not
            Dir.mkdirs();
        }
        File videoFile = new File(Dir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");


        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
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
            initRecorder();
            prepareRecorder();
        } else {
            recording = true;
            recorder.start();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
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
}



