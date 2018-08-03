package com.zyl.netty.server.event;

import com.zyl.netty.server.ProtocolMsg;

/**
 * 开关事件
 * @author zyl
 *
 */
public class DateEvt extends CmdEvt{
	
	public DateEvt(int evtCommand, ProtocolMsg protocolMsg) {
		super(evtCommand, protocolMsg);
	}
}
