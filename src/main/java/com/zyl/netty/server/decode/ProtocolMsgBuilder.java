package com.zyl.netty.server.decode;

import com.zyl.netty.server.ProtocolMsg;

public interface ProtocolMsgBuilder{
	public ProtocolMsg builder(ProtocolMsg msg,int contentlength,byte[] content);
}
