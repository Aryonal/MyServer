package com.aryon.myserver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * Created by Aryon on 2015/6/26.
 */
public class PicSocket extends Thread implements Runnable {
    MainActivity activity;
    String ip_addr;
    int port;

    public PicSocket(MainActivity ac){
        activity = ac;
        ip_addr = ac.ip_addr;
        port = ac.port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip_addr, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("send me a bmp");
            Log.d("MainActivity", "send me a bmp sent");

            DataInputStream input = new DataInputStream(socket.getInputStream());
            Log.d("MainActivity","got a message -----");
            //out.close();
            while(true) {
                if (input.available() > 0) {
                    Log.d("MainActivity","file arrived---");
                    int size = input.readInt();
                    Log.d("MainActivity","got file size: "+size+"---");
                    byte[] data = new byte[size];
                    Log.d("MainActivity","got file data---");
                    //input.read(data);
                    Log.d("MainActivity","read file data---");
                    int len = 0;
                    while (len < size) {
                        len += input.read(data, len, size - len);
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Log.d("MainActivity","make file into bmp，length: "+data.length+" bmp is"+(bmp==null?" null":" not null")+"---");

                    SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyyMMddhhmmss");
                    String date = sDateFormat.format(new java.util.Date());
                    File file = new File(Environment.getExternalStorageDirectory()+"/media/pic", date+".jpg");
                    Log.d("MainActivity","store file ---");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    Log.d("MainActivity","stream file ---");

                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    String s = "On the air    ";
                    activity.rec = s;
                    activity.mhandler.sendEmptyMessage(activity.gotmessage);

                    for(int i = 0; i < 10; i++) {
                        try {
                            this.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        activity.rec = ">";
                        activity.mhandler.sendEmptyMessage(activity.gotmessage);
                    }
                    activity.rec = "Picture saved";
                    activity.mhandler.sendEmptyMessage(activity.gotmessage);
                    activity.gotbmp = bmp;
                    activity.mhandler.sendEmptyMessage(activity.gotpic);
                    Log.d("MainActivity","file saved");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.onDialog = 0;
    }
}
