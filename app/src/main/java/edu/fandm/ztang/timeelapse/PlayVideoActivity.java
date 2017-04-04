package edu.fandm.ztang.timeelapse;

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
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

import edu.fandm.ztang.timeelapse.ImageAdapter;


public class PlayVideoActivity extends AppCompatActivity {

    private MediaController ctlr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_play_video);


        Bundle b = getIntent().getExtras();
        String filepath = ""; // or other values
        if(b != null){
            filepath = b.getString("path");
        }

        VideoView myVideo = (VideoView)findViewById(R.id.videoView);

        myVideo.setVideoPath(filepath);

        ctlr = new MediaController(this);
        ctlr.setMediaPlayer(myVideo);
        myVideo.setMediaController(ctlr);
        myVideo.requestFocus();
        myVideo.start();


    }
}
