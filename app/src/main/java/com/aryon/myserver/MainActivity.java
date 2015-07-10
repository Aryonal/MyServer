package com.aryon.myserver;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;


public class MainActivity extends Activity {
    Handler mhandler;
    final int gotmessage = 1;
    final int gotpic = 2;
    String rec = "surprise!";
    TextView tv;
    Button reg;
    Button work;
    Button clear;
    Button gallery;
    String ip_addr = "192.168.137.16";
    int port = 8890;
    MyServer ms;
    LoopSocket ls;

    Bitmap gotbmp = null;

    int onDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textview);
        reg = (Button)findViewById(R.id.register);
        work = (Button)findViewById(R.id.work);
        clear = (Button)findViewById(R.id.clear);
        gallery = (Button)findViewById(R.id.gallery);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(0xff,0x3F,0x51,0xB5)));



        //MyServer ms = new MyServer(this);
        //ms.start();

        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.d("MainActivity","got message.what: "+msg.what);
                switch(msg.what){
                    case gotmessage:{
                        if(rec.equals("An Action Has Been Caught!")){
                            //warnning!
                            SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("hh:mm:ss MM-dd");
                            String   date   =   sDateFormat.format(new   java.util.Date());
                            tv.setText(tv.getText()+"\n"+"("+date+") " + rec);
                            Log.d("MainActivity", "An Action Has Been Caught!");

                            /*
                            try {
                                ls.join();
                            }catch (Exception e){
                                e.printStackTrace();
                            }*/
                            //dialog

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PicSocket ps = new PicSocket(MainActivity.this);
                                    ps.start();

                                }
                            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ls = new LoopSocket(MainActivity.this);
                                    //ls.start();
                                    onDialog = 0;
                                }
                            }).setMessage("An Action Has Been Caught!\nDo you want to receive the Picture");
                            if(onDialog == 0) {
                                builder.create().show();
                                onDialog = 1;
                            }
                            //ls = new LoopSocket(MainActivity.this);
                            //ls.start();
                        }else if(rec.equals("normal")){
                            if(tv.getText().toString().endsWith("normal")) {
                                //do nothing
                            }else{
                                SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("hh:mm:ss MM-dd");
                                String   date   =   sDateFormat.format(new   java.util.Date());
                                tv.setText(tv.getText()+"\n"+"("+date+") " + rec);
                            }
                            Log.d("MainActivity","normal");
                        }else if(rec.equals(">")){
                            tv.setText(tv.getText()+rec);
                        }else{
                            SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("hh:mm:ss MM-dd");
                            String   date   =   sDateFormat.format(new   java.util.Date());
                            tv.setText(tv.getText()+"\n"+"("+date+") " + rec);
                            Log.d("MainActivity", rec);
                        }
                        break;

                    }
                    case gotpic:{
                        //show gotbmp
                        Toast.makeText(MainActivity.this, "got bmp, ready to show", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:{
                        //Log.d("MainActivity","error message: "+rec);
                        break;
                    }
                }
            }
        };
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ms = new MyServer(MainActivity.this);
                ms.start();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                ls = new LoopSocket(MainActivity.this);
                ls.start();
                work.setActivated(false);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(">_");
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(MainActivity.this,Gallery.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //
        ls = new LoopSocket(MainActivity.this);
        ls.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:{
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.savetotxt:{
                BufferedWriter output = null;
                try {
                    SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyyMMddhhmmss");
                    String date = sDateFormat.format(new java.util.Date());
                    File file = new File(Environment.getExternalStorageDirectory()+"/media/pic", date+".log");
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(tv.getText().toString());
                    output.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Log saved", Toast.LENGTH_SHORT).show();
                break;
            }
            default:{
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
