package edu.fandm.ztang.timeelapse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by zhuofantang on 3/27/17
 * A Image Adapter for the grid view
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> thumbList = null;
    final int THUMBSIZE = 128;

    public ImageAdapter(Context c, ArrayList<Bitmap> folders) {
        thumbList = folders;
        mContext = c;
    }

    /**
     * A get method to get the number of image thumbnails on screen
     *
     * @return the number of image thumbnails on screen
     */
    public int getCount() {
        return thumbList.size();
    }

    /**
     * A get method to get the image thumbnail at that position
     *
     * @param position
     * @return the image thumbnail at that position
     */
    public Object getItem(int position) {

        //TODO implementation
        return null;
    }

    /**
     * A get method to get the image thumbnail's position
     *
     * @param position
     * @return the image thumbnail's id
     */
    public long getItemId(int position) {
        //TODO implementation
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(285, 285));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        imageView.setImageBitmap(thumbList.get(position));

        return imageView;
    }
}