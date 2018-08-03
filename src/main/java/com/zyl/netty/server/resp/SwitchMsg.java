package com.zyl.netty.server.resp;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.utils.NumberUtils;
/**
 * 盒子上传开关状态 
 * (1)0x61 : 服务器控制设备开关状态,盒子上传开关状态,此时服务器不需要回复
 * (2)0x81 : 服务器查询设备开关状态,盒子上传开关状态,此时服务器不需要回复
 * @author Administrator
 *
 */
public class SwitchMsg extends ProtocolMsg {
	/*灯箱开关 0表示关闭,1表示开启*/
	private int openLight;			
	/*字体开关 0表示关闭,1表示开启*/
	private int openWord;
	
	public SwitchMsg(byte[] deviceNum) {
	    setCommand(ProtocolMsg.MSG_TYPE_CONTROL_RESPONSE);
		setDeviceNumber(deviceNum);
	}
	public SwitchMsg(String deviceNum) {
	    setCommand(ProtocolMsg.MSG_TYPE_CONTROL_RESPONSE);
		setDeviceNumber(NumberUtils.hexStringToBytes(deviceNum));
	}
	
	public int isOpenLight() {
		return openLight;
	}
	public void setOpenLight(int openLight) {
		this.openLight = openLight;
	}
	public int isOpenWord() {
		return openWord;
	}
	public void setOpenWord(int openWord) {
		this.openWord = openWord;
	}
	
	// 返回数据记录时间
	public long getRecordDate() {
		return getCurrentDate().getTime();
	}
}
