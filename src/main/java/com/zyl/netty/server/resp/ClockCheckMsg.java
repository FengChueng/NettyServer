package com.zyl.netty.server.resp;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * 服务器响应时间校验
 * @author Administrator
 *
 */
public class ClockCheckMsg extends ProtocolMsg {
    
    String year;
    String month;
    String day;
    String time;

	public ClockCheckMsg(byte[] deviceNumn) {
		setDeviceNumber(deviceNumn);
		setCommand(ProtocolMsg.MSG_TYPE_CHECK_TIME);
	}

	public ClockCheckMsg(String deviceNumn) {
		setDeviceNumber(NumberUtils.hexStringToBytes(deviceNumn));
		setCommand(ProtocolMsg.MSG_TYPE_CHECK_TIME);
	}

	public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
