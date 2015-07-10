package com.aryon.myserver;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by Aryon on 2015/6/25.
 */
public class LoopSocket extends Thread implements Runnable {
    MainActivity myactivity;
    public LoopSocket(MainActivity activity){
        myactivity = activity;
    }
    public void run(){
        try{
            while(true){
                Socket socket = new Socket(myactivity.ip_addr,myactivity.port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("normal");
                Log.d("LoopSocket","normal sent");
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String s = input.readUTF();
                myactivity.rec = s;
                myactivity.mhandler.sendEmptyMessage(myactivity.gotmessage);
                input.close();
                out.close();
                socket.close();
                try{
                    LoopSocket.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
