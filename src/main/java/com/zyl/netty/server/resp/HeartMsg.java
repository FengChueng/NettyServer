package com.zyl.netty.server.resp;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.utils.NumberUtils;

public class HeartMsg extends ProtocolMsg {
	public HeartMsg(byte[] deviceNumn) {
		setDeviceNumber(deviceNumn);
		setCommand(ProtocolMsg.MSG_TYPE_HEART);
	}

	public HeartMsg(String deviceNumn) {
		setDeviceNumber(NumberUtils.hexStringToBytes(deviceNumn));
		setCommand(ProtocolMsg.MSG_TYPE_HEART);
	}

	// 返回数据记录时间
	public long getRecordDate() {
		return getCurrentDate().getTime();
	}

}
