package com.aryon.myserver;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Aryon on 2015/6/24.
 */
public class MyServer extends Thread implements Runnable {
    MainActivity myactivity;
    String ip_addr;
    int port;
    Socket socket;

    public MyServer(MainActivity activity){
        //
        myactivity = activity;
        ip_addr = myactivity.ip_addr;
        port = myactivity.port;
        Log.d("MyServer","MyServer created");
    }
    public void run(){
        //
        try{
            socket = new Socket(ip_addr,port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            out.writeUTF("register");
            myactivity.rec = input.readUTF();
            myactivity.mhandler.sendEmptyMessage(myactivity.gotmessage);
            input.close();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
