package com.zyl.netty.client.recv;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zyl.netty.client.Message;
import com.zyl.netty.client.MessageUtils;
import com.zyl.netty.client.SimulatorSocket;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * 服务器响应心跳包0x21
 * 
 * @author laiiihan
 *
 */
public class HeartResponse extends Command implements Runnable {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    boolean firstHeartResponse = true;

    Message heart, clock, switchMsg, dateMsg;

    private SimulatorSocket simulatorSocket;

    @Override
    public void execute(SimulatorSocket simulatorSocket, Message message) throws IOException {
        LOGGER.info("收到设备心跳回复");

        this.simulatorSocket = simulatorSocket;
        String deviceNumber = NumberUtils.bytesToHexString(message.getDeviceNumber());

        
        heart = MessageUtils.newHeartMessage(deviceNumber);

        clock = MessageUtils.newClockMessage(deviceNumber);
        
        
        switchMsg = MessageUtils.newSwitchMessage(deviceNumber, true, true);
        
        
        dateMsg = MessageUtils.newSetTimeMessage(deviceNumber, "12:00", "12:00", "12:00", "12:00");
        

        
        if(firstHeartResponse){
            // 定时上传
            new Thread(this).start();
            firstHeartResponse = false;
        }
    }

    @Override
    public void run() {
        while (!simulatorSocket.isClose()) {
            try {
                Thread.sleep(30000);
            } catch (Exception e) {
            }
            try {
                simulatorSocket.sendMessage(heart);
            } catch (IOException e1) {
                LOGGER.error("发送心跳包失败:{}",e1.getMessage());
            }
            
            try {
                simulatorSocket.sendMessage(clock);
            } catch (IOException e1) {
                LOGGER.error("发送时钟参数:{}",e1.getMessage());
            }
            
            try {
                simulatorSocket.sendMessage(switchMsg);
            } catch (IOException e1) {
                LOGGER.error("发送开关参数:{}",e1.getMessage());
            }
            
            try {
                simulatorSocket.sendMessage(dateMsg);
            } catch (IOException e1) {
                LOGGER.error("发送时间参数失败:{}",e1.getMessage());
            }
            
            
        }
    }
}
