package com.zyl.netty.client.recv;

import java.io.IOException;

import com.zyl.netty.client.Message;
import com.zyl.netty.client.SimulatorSocket;

/**
 * 响应服务器控制开关状态0x60
 * 
 */
public class ControlSwitchResponse extends Command {

	@Override
	public void execute(SimulatorSocket simulatorSocket, Message message) throws IOException {
		LOGGER.info("收到设备开关控制回复");
	}
}
