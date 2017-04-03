package edu.fandm.ztang.timeelapse;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import edu.fandm.ztang.timeelapse.ImageAdapter;

import static android.support.v7.appcompat.R.styleable.View;
import static edu.fandm.ztang.timeelapse.R.id.parent;

public class VideoThumbGridAvitivity extends AppCompatActivity {


    private Context mContext = this;
    private ArrayList<File> folderGrid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_thumb_grid_avitivity);

        folderGrid = new ArrayList<File>();
        getFolderList();

        //hook up the grid view
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        Thread gridThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gridview.setAdapter(new ImageAdapter(mContext, folderGrid));
            }
        }, "Grid Display Thread");
        gridThread.start();




        //set the onClickListener of the gridview
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, android.view.View v,
                                    int position, long id) {
                Toast.makeText(VideoThumbGridAvitivity.this, ""  + position,
                        Toast.LENGTH_SHORT).show();

                String filePath = folderGrid.get(position).getAbsolutePath();

                Intent intent = new Intent(mContext, PlayVideoActivity.class);
                Bundle b = new Bundle();
                b.putString("path", filePath);

                intent.putExtras(b);
                startActivity(intent);


            //minor change
            }
        });

    }


    /**
     * Fill the arrayList with all the folders in the directory
     */
    private void getFolderList(){

        //TODO check permission

        File root = Environment.getExternalStorageDirectory();
        File storageDir = new File(root.getAbsolutePath() + "/TimeElapse");
        Log.d("Storage Path: ", storageDir.getAbsolutePath());
        for (final File fileEntry : storageDir.listFiles()) {
            Log.d("Loop", "anything?");
            if (fileEntry.isDirectory()) {
                folderGrid.add(fileEntry);
                Log.d("File Path: ", fileEntry.getAbsolutePath());
            }else{
                Log.d("Not File Path: ", fileEntry.getAbsolutePath());
            }
        }

    }




}
