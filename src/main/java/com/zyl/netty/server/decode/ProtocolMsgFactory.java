package com.zyl.netty.server.decode;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.zyl.netty.server.ProtocolMsg;

@Component
public class ProtocolMsgFactory{
	private Map<Short,ProtocolMsgBuilder> protocolMsgs = new HashMap<Short,ProtocolMsgBuilder>();
	public ProtocolMsgBuilder getProtocolMsg(short key){
		return protocolMsgs.get(key);
	}
	public boolean exist(short key){
		return protocolMsgs.containsKey(key);
	}
	@PostConstruct
	public void register(){
		//0x42,app下发时间参数
		protocolMsgs.put(ProtocolMsg.TIME_SERVER_REQ, new DecodeDateMsg());//解析服务器查询后,盒子返回的开关时间
		//0x60,app下发开关参数
		protocolMsgs.put(ProtocolMsg.MSG_TYPE_CONTROL, new DecodeSwitchMsg());
		//0x91,APP下发时钟参数
		protocolMsgs.put(ProtocolMsg.MSG_TYPE_CHECK_TIME_RESPONSE, new DecodeClockMsg());//解析盒子上传的设备版本
	}
}
