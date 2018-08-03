package com.zyl.netty.client.recv;

import java.io.IOException;

import com.zyl.netty.client.Message;
import com.zyl.netty.client.SimulatorSocket;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * 服务器响应时钟校验命令
 * @author laiiihan
 *
 */
public class ClockResponse extends Command{

	@Override
	public void execute(SimulatorSocket simulatorSocket, Message message) throws IOException {
		LOGGER.info("收到设备时钟回复{}");
	}
}
