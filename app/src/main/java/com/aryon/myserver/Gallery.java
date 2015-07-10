package com.aryon.myserver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.Map;

public class Gallery extends Activity {
    String rootpath = Environment.getExternalStorageDirectory()+"/media/pic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ListView listView = (ListView)findViewById(R.id.listview);


        File imageDoc = new File(rootpath);
        int i = 0;
        for(File fp:imageDoc.listFiles()) {
            if (fp.getName().toString().endsWith(".jpg")) {
                i++;
            }
        }


        final String[] imagePath = new String[i];
        String[] imageName = new String[i];

        i = 0;
        for(File fp:imageDoc.listFiles()) {
            if (fp.getName().toString().endsWith(".jpg")) {
                imagePath[i] = fp.getPath();
                imageName[i] = fp.getName();
                i++;
            }
        }

        CustomListAdapter listAdapter = new CustomListAdapter(Gallery.this, imagePath,imageName);
        listView.setAdapter(listAdapter);

        //ImageView imageView = (ImageView)findViewById(R.id.imagesquare);
        //Log.d("Gallery","ImageView width: "+imageView.getWidth());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + imagePath[position]), "image/*");
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
