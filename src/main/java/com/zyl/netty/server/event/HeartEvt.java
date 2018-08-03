package com.zyl.netty.server.event;

import com.zyl.netty.server.ProtocolMsg;

/**
 * 心跳事件
 * @author zyl
 *
 */
public class HeartEvt extends CmdEvt{
	
	public HeartEvt(int evtCommand, ProtocolMsg protocolMsg) {
		super(evtCommand, protocolMsg);
	}
}
