package com.zyl.netty.client.recv;

import java.io.IOException;

import com.zyl.netty.client.Message;
import com.zyl.netty.client.SimulatorSocket;

/**
 * 服务器响应设备查询开关时间命令0x41
 * @author laiiihan
 *
 */
public class DateResponse extends Command{

	@Override
	public void execute(SimulatorSocket simulatorSocket, Message message ) throws IOException {
        LOGGER.info("收到设备时间设置回复");
	}

}
