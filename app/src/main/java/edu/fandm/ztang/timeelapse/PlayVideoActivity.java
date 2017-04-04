package edu.fandm.ztang.timeelapse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import edu.fandm.ztang.timeelapse.ImageAdapter;


public class PlayVideoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private int playPosition = 1;
    private int chooseDelay = 200;
    private int imageNum = 0;

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
            imageNum += 1;
        }

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Set FPS");
        categories.add("33");
        categories.add("66");
        categories.add("100");
        categories.add("200");
        categories.add("500");
        categories.add("1000");

        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        if(!item.equals("Set FPS")){
            chooseDelay = Integer.valueOf(item);
        }else{
            item = "200";
        }


        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected FPS: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView arg0) {


    }



    public void playVideo(View v){
        playPosition = 1;
        final Handler h = new Handler();
        final int delay = chooseDelay;

        h.postDelayed(new Runnable(){
            public void run(){


                ImageView iv = (ImageView)findViewById(R.id.imageView);
                if(playPosition <= imageNum){
                    String fileName = String.valueOf(playPosition) + ".jpg";
                    Bitmap myBitmap = BitmapFactory.decodeFile(fileName);
                    iv.setImageBitmap(myBitmap);
                    playPosition += 1;
                }

                h.postDelayed(this, delay);
            }
        }, delay);
    }
}
