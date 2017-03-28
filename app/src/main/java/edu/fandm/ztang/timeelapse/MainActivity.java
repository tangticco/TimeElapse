package edu.fandm.ztang.timeelapse;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity {

    static final int camera_request = 1;
    private static int PERMS_REQUEST_CODE = 123;
    private static String TAG = "TimeElapse Camera";

    private static int numImages = 10;
    private static int intervalImages = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            //Request runtime permissions
            String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMS_REQUEST_CODE);

        }
    }

    public void shot(View v){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,camera_request);
            }
        });

    }

    private File getFile(){
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath(), "camera_app");
        if(!Dir.exists()){
            Dir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss'Z'").format(new Timestamp(System.currentTimeMillis()));
        File image_files = new File(Dir, "timeStamp");
        return  image_files;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/camera_app" + "/timeStamp";
        ImageView image = (ImageView)findViewById(R.id.view);
        image.setImageDrawable(Drawable.createFromPath(path));
    }
}
