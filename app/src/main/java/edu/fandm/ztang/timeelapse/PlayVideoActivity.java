package edu.fandm.ztang.timeelapse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

import edu.fandm.ztang.timeelapse.ImageAdapter;


public class PlayVideoActivity extends AppCompatActivity {

    //set up some class wide variables
    private MediaController ctlr;
    private boolean isVideoPlaying = false;
    private String filepath = "";
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_play_video);


        //retrieve the file path
        Bundle b = getIntent().getExtras();
        if(b != null){
            filepath = b.getString("path");
        }
    }


    /**
     * A method to control the video playback
     * @param v
     */
    public void videoControl(View v){

        //connect
        ToggleButton controlVideo = (ToggleButton)findViewById(R.id.toggleButton);
        VideoView myVideo = (VideoView) findViewById(R.id.videoView);


        //configure a video controller to play the video
        myVideo.setVideoPath(filepath);
        ctlr = new MediaController(mContext);
        ctlr.setMediaPlayer(myVideo);
        myVideo.setMediaController(ctlr);
        myVideo.requestFocus();
        myVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                isVideoPlaying = false;
            }
        });


        //check if the video is playing or not
        if(!isVideoPlaying){
            myVideo.start();
            isVideoPlaying = true;
            controlVideo.setTextOff("Play");
        }else{
            myVideo.stopPlayback();
            isVideoPlaying = false;
            controlVideo.setTextOff("Stop");
        }




    }
}
