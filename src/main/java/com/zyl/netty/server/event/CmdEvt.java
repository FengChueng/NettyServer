package com.zyl.netty.server.event;

import com.zyl.netty.server.ProtocolMsg;

/**
 * 设备向服务器发送的消息事件
 * @author zyl
 *
 */
public class CmdEvt implements Event{
	/** 事件源的命令*/
	private int evtCommand;
	/** 事件源*/
	private ProtocolMsg protocolMsg;
	public CmdEvt(int evtCommand,ProtocolMsg protocolMsg) {
		this.evtCommand = evtCommand;
		this.protocolMsg = protocolMsg;
	}
	
	/**
	 * 获取事件源的命令
	 * @return
	 */
	public int getEvtCommand(){
		return evtCommand;
	}
	
	public void setEvt(ProtocolMsg protocolMsg) {
		this.protocolMsg = protocolMsg;
	}
	
	/**
	 * 获取事件源
	 * @return
	 */
	public ProtocolMsg getEvt(){
		return protocolMsg;
	}
}
