package com.zyl.netty.client;

import java.util.Calendar;

/**
 * Created by zhangyinglong on 2018/8/2.
 */

public class MessageUtils {

    /**
     * 心跳包0x21
     * @param deviceNumber
     * @return
     */
    public static Message newHeartMessage(String deviceNumber){
        Message message = new Message(deviceNumber,MsgConstants.MSG_TYPE_HEART_SEND,null);
        return  message;
    }

    /**
     * 时钟
     * @param deviceNumber
     * @return
     */
    public static Message newClockMessage(String deviceNumber){
        byte[] content = null;
        content = new byte[7];
        Calendar dar = Calendar.getInstance();
        String year = Integer.toString(dar.get(Calendar.YEAR));
        content[0] = (byte) Integer.parseInt(year.substring(0, 2), 16);
        content[1] = (byte) Integer.parseInt(year.substring(2), 16);
        content[2] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.MONTH) + 1), 16);
        content[3] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.DAY_OF_MONTH)), 16);

        content[4] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.HOUR_OF_DAY)), 16);
        content[5] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.MINUTE)), 16);
        content[6] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.SECOND)), 16);
        Message message = new Message(deviceNumber,MsgConstants.MSG_TYPE_CHECK_SEND,content);
        return  message;
    }

    /**
     * 时钟
     * @param deviceNumber
     * @param currentDateMils   服务器返回的时钟
     * @return
     */
    public static Message newClockMessage(String deviceNumber,long currentDateMils){
        byte[] content = null;
        content = new byte[7];
        Calendar dar = Calendar.getInstance();
        dar.setTimeInMillis(currentDateMils);
        String year = Integer.toString(dar.get(Calendar.YEAR));
        content[0] = (byte) Integer.parseInt(year.substring(0, 2), 16);
        content[1] = (byte) Integer.parseInt(year.substring(2), 16);
        content[2] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.MONTH) + 1), 16);
        content[3] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.DAY_OF_MONTH)), 16);

        content[4] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.HOUR_OF_DAY)), 16);
        content[5] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.MINUTE)), 16);
        content[6] = (byte) Integer.parseInt(Integer.toString(dar.get(Calendar.SECOND)), 16);
        Message message = new Message(deviceNumber,MsgConstants.MSG_TYPE_CHECK_SEND,content);
        return  message;
    }

    /**
     * 设置时间0x42
     * @param deviceNumber
     * @param time
     * @return
     */
    public static Message newSetTimeMessage(String deviceNumber,String [] time){
        assert time !=null && time.length ==4;
        String[] startArray = time[0].split(":");
        String[] endArray = time[1].split(":");
        String[] startWordArray = time[2].split(":");
        String[] endWordArray = time[3].split(":");
        byte[] content = null;
        if (startArray != null && endArray != null && startArray.length == 2 && endArray.length == 2
                && startWordArray != null && endWordArray != null && startWordArray.length == 2
                && endWordArray.length == 2) {
            content = new byte[8];
            content[0] = (byte) Integer.parseInt(startArray[0], 16);
            content[1] = (byte) Integer.parseInt(startArray[1], 16);
            content[2] = (byte) Integer.parseInt(endArray[0], 16);
            content[3] = (byte) Integer.parseInt(endArray[1], 16);

            content[4] = (byte) Integer.parseInt(startWordArray[0], 16);
            content[5] = (byte) Integer.parseInt(startWordArray[1], 16);
            content[6] = (byte) Integer.parseInt(endWordArray[0], 16);
            content[7] = (byte) Integer.parseInt(endWordArray[1], 16);
        }
        Message message = new Message(deviceNumber,MsgConstants.MSG_TYPE_SET_TIME_SEND,content);
        return  message;
    }

    public static Message newSetTimeMessage(String deviceNumber,String startTimeLight, String endTimeLight,
                                            String startTimeWord,String endTimeWord){
        String[] startArray = startTimeLight.split(":");
        String[] endArray = endTimeLight.split(":");
        String[] startWordArray = startTimeWord.split(":");
        String[] endWordArray = endTimeWord.split(":");
        byte[] content = null;
        if (startArray != null && endArray != null && startArray.length == 2 && endArray.length == 2
                && startWordArray != null && endWordArray != null && startWordArray.length == 2
                && endWordArray.length == 2) {
            content = new byte[8];
            content[0] = (byte) Integer.parseInt(startArray[0], 16);
            content[1] = (byte) Integer.parseInt(startArray[1], 16);
            content[2] = (byte) Integer.parseInt(endArray[0], 16);
            content[3] = (byte) Integer.parseInt(endArray[1], 16);

            content[4] = (byte) Integer.parseInt(startWordArray[0], 16);
            content[5] = (byte) Integer.parseInt(startWordArray[1], 16);
            content[6] = (byte) Integer.parseInt(endWordArray[0], 16);
            content[7] = (byte) Integer.parseInt(endWordArray[1], 16);
        }
        Message message = new Message(deviceNumber,MsgConstants.MSG_TYPE_SET_TIME_SEND,content);
        return  message;
    }

    /**
     * 设置开关0x60
     * @param deviceNumber
     * @param content
     * @return
     */
    public static Message newSwitchMessage(String deviceNumber,byte[] content){
        Message message = new Message(deviceNumber,MsgConstants.MSG_TYPE_CONTROL_SEND,content);
        return  message;
    }

    /**
     * 设置开关0x60
     * @param deviceNumber
     * @return
     */
    public static Message newSwitchMessage(String deviceNumber,boolean lightSwitch,boolean wordSwitch){
        return newSwitchMessage(deviceNumber,new byte[]{(byte) (lightSwitch?0x01:0x00), (byte) (wordSwitch?0x01:0x00)});
    }


}
