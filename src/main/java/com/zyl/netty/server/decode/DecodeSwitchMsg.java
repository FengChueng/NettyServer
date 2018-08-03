package com.zyl.netty.server.decode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.resp.SwitchMsg;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * APP下发开关参数0x60,设备响应61
 * 
 * @author Administrator
 *
 */
public class DecodeSwitchMsg implements ProtocolMsgBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(DecodeSwitchMsg.class);

	@Override
	public ProtocolMsg builder(ProtocolMsg msg, int contentlength, byte[] content) {
		String deviceNumber = NumberUtils.bytesToHexString(msg.getDeviceNumber());
		SwitchMsg switchMsg = new SwitchMsg(msg.getDeviceNumber());

		if(content.length == 2){
			switchMsg.setOpenLight(content[0]);
			switchMsg.setOpenWord(content[1]);
			switchMsg.setCurrentDate(msg.getCurrentDate());
//			LOGGER.info("----{} 设备{}当前灯箱开关状态:{}",Integer.toHexString(msg.getCommand()&0xFF),deviceNumber,switchMsg.isOpenLight()+"----设备当前字体开关状态:"+switchMsg.isOpenWord());
			LOGGER.info("app设置开关,code={},deviceNumber={},switch1={},switch2={}",Integer.toHexString(msg.getCommand()&0xFF),deviceNumber,switchMsg.isOpenLight(),switchMsg.isOpenWord());
		}
		return switchMsg;
	}

}
