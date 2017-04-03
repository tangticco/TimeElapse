package edu.fandm.ztang.timeelapse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import edu.fandm.ztang.timeelapse.ImageAdapter;


public class PlayVideoActivity extends AppCompatActivity {


    private int playPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);


        Bundle b = getIntent().getExtras();
        String filepath = ""; // or other values
        if(b != null){
            filepath = b.getString("path");
        }

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        File targetFolder = new File(filepath);
        for (final File fileEntry : targetFolder.listFiles()) {
            if (fileEntry.getName().equals("1.jpg")){
                Bitmap myBitmap = BitmapFactory.decodeFile(fileEntry.getAbsolutePath());
                iv.setImageBitmap(myBitmap);

            }
        }



    }


    /**
    public void playVideo(View v){
        final Handler h = new Handler();
        final int delay = 33; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                //do something

                ImageView iv = (ImageView)findViewById(R.id.imageView);
                if(playPosition < mThumbIds.length){
                    iv.setImageResource(mThumbIds[playPosition]);
                    playPosition += 1;
                }

                h.postDelayed(this, delay);
            }
        }, delay);
    }



    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };

     */
}
