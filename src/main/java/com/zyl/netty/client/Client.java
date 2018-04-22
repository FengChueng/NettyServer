package com.zyl.netty.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * author:zyl
 * date:2018年4月22日 下午10:41:02
 */
public class Client {
    
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("127.0.0.1", 9000);
        
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
               try {
                   while(true){
                       InputStream inputStream = socket.getInputStream();
                       byte [] buffer = new byte [1024];
                       inputStream.read(buffer);
                       System.out.println(new String(buffer));
                       OutputStream outputStream = socket.getOutputStream();
                       outputStream.write("心跳包\n".getBytes());
                       outputStream.flush();
                   }
            } catch (IOException e) {
                e.printStackTrace();
            }
                
            }
        }).start();
        
        
        while(true){
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("hello serverasssssssssssssssssssssssssssssssssss\n".getBytes());
            outputStream.flush();
            System.in.read();
        }
        

    }
    
    
    
    
}
