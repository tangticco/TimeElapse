package edu.fandm.ztang.timeelapse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import edu.fandm.ztang.timeelapse.ImageAdapter;

import static android.support.v7.appcompat.R.styleable.View;
import static edu.fandm.ztang.timeelapse.R.id.parent;

public class VideoThumbGridAvitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_thumb_grid_avitivity);

        //hook up the grid view
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));


        //TODO change the functionality
        //set the onClickListener of the gridview
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, android.view.View v,
                                    int position, long id) {
                Toast.makeText(VideoThumbGridAvitivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


}
