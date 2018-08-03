package com.zyl.netty.server.event;

import com.zyl.netty.server.ProtocolMsg;

/**
 * 开关事件
 * @author zyl
 *
 */
public class SwitchEvt extends CmdEvt{
	
	public SwitchEvt(int evtCommand, ProtocolMsg protocolMsg) {
		super(evtCommand, protocolMsg);
	}
}
