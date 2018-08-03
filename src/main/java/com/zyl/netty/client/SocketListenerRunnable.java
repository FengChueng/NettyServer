package com.zyl.netty.client;

import java.io.IOException;
import java.io.InputStream;

import com.zyl.netty.client.recv.Command;

/**
 * Created by zhangyinglong on 2018/8/2.
 */

public class SocketListenerRunnable implements Runnable{
    //读取设备返回数据
    private final InputStream inputStream;
    //用于通知主线程
    private final SimulatorSocket simulatorSocket;

    public SocketListenerRunnable(SimulatorSocket simulatorSocket,InputStream inputStream) {
        this.simulatorSocket = simulatorSocket;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        byte [] buffer = new byte[22];//缓存区,是否半包
        try {
            while (true){
                byte msg_start = readByte();

                if(msg_start != MsgConstants.MSG_START){
                    //首字节非0xaa,抛弃不处理后继续读取
                    continue;
                }
                //数据总长度-2,即此字节后的所有字节
                int msg_length = readByte();
                //类型(1) + 命令(2) +设备编号(6) + content(0) + end(2)
                byte[] bytes = readByte(msg_length);
                if(bytes == null){
                    continue;
                }
                byte msg_type = bytes[0];//设备类型

                byte [] msg_cmd = new byte[2];//命令
                System.arraycopy(bytes,1,msg_cmd,0,2);

                int msg_content_len = bytes[3];

                byte [] msg_device_num = new byte[6];//设备编号
                System.arraycopy(bytes,4,msg_device_num,0,6);

                int param_len = msg_content_len - 6;//内容参数长度

                byte [] msg_param = null;//内容参数
                if(param_len > 0){
                    msg_param = new byte[param_len];
                    System.arraycopy(bytes,10,msg_device_num,0,param_len);
                }
                byte [] msg_end = new byte[2];//2个结束字节
                System.arraycopy(bytes,10,msg_device_num,0,param_len);

                Message protocolMsg = new Message(msg_device_num,msg_cmd,msg_param);

                Command command = simulatorSocket.getCommand(msg_cmd[1]);
                if(command!=null){
                    command.execute(simulatorSocket,protocolMsg);//处理相应逻辑
                }


            }
        } catch (IOException e) {
            try {
                inputStream.close();
            } catch (IOException e1) {

            }
        }
    }

    /**
     * 读取指定长度的字节至数组
     * @param i
     * @return
     * @throws IOException
     */
    private byte [] readByte(int i) throws IOException {
        byte [] bytes = new byte[i];
        int length = inputStream.read(bytes);
        if(length < i) return null;
        return bytes;
    }

    /**
     * 读取一个字节
     * @return
     * @throws IOException
     */
    private byte readByte() throws IOException {
        int read = inputStream.read();
        return (byte)read;
    }
}
