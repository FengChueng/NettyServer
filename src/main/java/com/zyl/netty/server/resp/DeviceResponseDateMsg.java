package com.zyl.netty.server.resp;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.utils.NumberUtils;
/**
 * (1)设备返回给服务器开关时间参数，服务器不需要再响应
 * (2)设备响应服务设置时间参数,服务器不需要再响应
 * @author Administrator
 *
 */
public class DeviceResponseDateMsg extends ProtocolMsg {
	/*灯箱开灯时间*/
	private String startDateLight;
	/*灯箱关灯时间*/
	private String endDateLight;
	/*字体开灯时间*/
	private String startDateWord;
	/*字体关灯时间*/
	private String endDateWord;
	
	public DeviceResponseDateMsg(byte[] deviceNumn) {
	    setCommand(ProtocolMsg.TIME_CLIENT_RESPONSE);
		setDeviceNumber(deviceNumn);
	}
	public DeviceResponseDateMsg(String deviceNumn) {
	    setCommand(ProtocolMsg.TIME_CLIENT_RESPONSE);
		setDeviceNumber(NumberUtils.hexStringToBytes(deviceNumn));
	}
	public String getStartDateLight() {
		return startDateLight;
	}
	public void setStartDateLight(String startDateLight) {
		this.startDateLight = startDateLight;
	}
	public String getEndDateLight() {
		return endDateLight;
	}
	public void setEndDateLight(String endDateLight) {
		this.endDateLight = endDateLight;
	}
	public String getStartDateWord() {
		return startDateWord;
	}
	public void setStartDateWord(String startDateWord) {
		this.startDateWord = startDateWord;
	}
	public String getEndDateWord() {
		return endDateWord;
	}
	public void setEndDateWord(String endDateWord) {
		this.endDateWord = endDateWord;
	}
	
}
