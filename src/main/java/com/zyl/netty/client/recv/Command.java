package com.zyl.netty.client.recv;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyl.netty.client.Message;
import com.zyl.netty.client.SimulatorSocket;

/**
 * 模拟器命令基类
 * @author Administrator
 *
 */
public abstract class Command {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass()); 
	/**
	 * 模拟器命令执行方法
	 * @param simulatorSocket -->报文发送工具类
	 * @param message         -->服务端发送报文对象
	 * @throws IOException
	 */
	public abstract void execute(SimulatorSocket simulatorSocket, Message message) throws IOException;
}
