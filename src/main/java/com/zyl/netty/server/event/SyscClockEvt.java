package com.zyl.netty.server.event;

import com.zyl.netty.server.ProtocolMsg;

/**
 * 同步时钟事件
 * @author zyl
 *
 */
public class SyscClockEvt extends CmdEvt{
	
	public SyscClockEvt(int evtCommand, ProtocolMsg protocolMsg) {
		super(evtCommand, protocolMsg);
	}
}
