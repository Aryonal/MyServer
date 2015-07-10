package com.aryon.myserver;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Aryon on 2015/6/27.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] imagePath;
    private final String[] imageName;

    public CustomListAdapter(Activity context, String[] imagePath, String[] imageName){
        super(context,R.layout.activity_gallery,imageName);
        this.context = context;
        this.imagePath = imagePath;
        this.imageName = imageName;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowview = mInflater.inflate(R.layout.listitemlayout, null,true);
        ImageView image = (ImageView)rowview.findViewById(R.id.imagesquare);
        TextView tv = (TextView)rowview.findViewById(R.id.imagename);

        tv.setText(imageName[position]);
        File img = new File(imagePath[position]);
        if(img.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(img.getAbsolutePath());
            image.setImageBitmap(bmp);
        }
        return rowview;

    }

}
