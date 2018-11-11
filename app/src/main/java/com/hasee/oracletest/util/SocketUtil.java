package com.hasee.oracletest.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketUtil {
    private static final String TAG = "SocketUtil";
    public static int PORT = 1234;
    public static String IP = "111.180.201.85";
    private boolean flag = true;
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    ByteArrayOutputStream content = null;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void connected(){
        try {
            socket = new Socket(IP, PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "连接超时");
            setFlag(false);
            closeAll();
        }
    }

    public String send_receive(String message){
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            byte[] send_message = message.getBytes("UTF-8");
            out.write(send_message);
            out.flush();
            String receive_message = null;
            content = new ByteArrayOutputStream();
            int len = -1;
            byte[] bytes = new byte[1024];
//            do{
//                len = in.read(bytes);
//                content.write(bytes,0,len);
//            }while (in.available()!=0);
            Log.d(TAG, "send_receive: "+1);
            while ((len = in.read(bytes))!= -1){
                content.write(bytes,0,len);
            }
            Log.d(TAG, "send_receive: "+2);
            receive_message = new String(content.toByteArray(),"UTF-8");
            return receive_message;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void closeAll(){
        try {
            if(in!=null){
                content.close();
                in.close();
            }
            if(out!=null){
                out.close();
            }
            if(socket!=null){
                socket.close();
            }
            content = null;
            in = null;
            out = null;
            socket =null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
