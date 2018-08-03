package com.zyl.netty.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyl.netty.client.recv.ClockResponse;
import com.zyl.netty.client.recv.Command;
import com.zyl.netty.client.recv.ControlSwitchResponse;
import com.zyl.netty.client.recv.DateResponse;
import com.zyl.netty.client.recv.HeartResponse;

public class SimulatorSocket {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass()); 
	private final String deviceNumber;
	private OutputStream out;
	private Socket socket;
	private Map<Byte, Command> commands = new HashMap<>();// 存放command
																		// -->
																		// 实现类
	private boolean isClose = true;

	public SimulatorSocket(String deviceNumber) {
		this.deviceNumber = deviceNumber;

	    // app发送心跳包(0x21),设备需要响应0x20
        commands.put(MsgConstants.MSG_TYPE_HEART_RESPONSE[1], new HeartResponse());

        // app发送时钟(0x91) 设备响应(0x90)
        commands.put(MsgConstants.MSG_TYPE_CHECK_RESPONSE[1], new ClockResponse());

        /*收到需要响应*/
        // 服务器主动设置开关时间 0x42,设备响应43
        commands.put(MsgConstants.MSG_TYPE_SET_TIME_RESPONSE[1], new DateResponse());
        // 服务器请求控制开关状态 0x60,设备响应61
		commands.put(MsgConstants.MSG_TYPE_CONTROL_RESPONSE[1], new ControlSwitchResponse());
	}

	public void connection() {
		try {
//			socket = new Socket("127.0.0.1", 9999);
			socket = new Socket("120.77.252.107", 9999);
			out = socket.getOutputStream();
			new Thread(new SocketListenerRunnable(this,socket.getInputStream())).start();
			// sendTestData();
			register();
			isClose = false;
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(),e);
			close();
		}
	}

	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
		    LOGGER.error(e.getMessage(),e);
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(),e);
		} finally {
			isClose = true;
		}
	}

	public void sendTestData() throws IOException {
		byte testData = (byte) 0xff;// 发送测试包
		out.write(new byte[] { testData });
		out.flush();
	}

	/**
	 * 发送注册信息
	 * 
	 * @throws IOException
	 */
	public void register() throws IOException {
		//发送心跳包

		Message message = new Message(deviceNumber,new byte[]{0x00,0x21},null);
		sendMessage(message);
		//开启一个定时任务,每隔30s发心跳包
	}

	public Command getCommand(byte command) {
		return commands.get(command);
	}

	//发送
	public void sendMessage(Message message) throws IOException {
		List<Byte> bytes = new ArrayList<Byte>();
		bytes.add(MsgConstants.MSG_START);
		bytes.add(message.getPackageLength());// 包长
		bytes.add((byte) 0x01);
		bytes.add(message.getCommand()[0]);
		bytes.add(message.getCommand()[1]);
		bytes.add(message.getLength());
		bytes.add(message.getDeviceNumber()[0]);
		bytes.add(message.getDeviceNumber()[1]);
		bytes.add(message.getDeviceNumber()[2]);
		bytes.add(message.getDeviceNumber()[3]);
		bytes.add(message.getDeviceNumber()[4]);
		bytes.add(message.getDeviceNumber()[5]);
		for (int i = 0, len = message.getContent().length; i < len; i++) {
			bytes.add(message.getContent()[i]);
		}
		bytes.add(MsgConstants.MSG_END2);
		bytes.add(MsgConstants.MSG_END1);
		byte[] bs = new byte[bytes.size()];
		int i = 0;
		for (Byte byte1 : bytes) {
			bs[i] = byte1.byteValue();
			i++;
		}
		// System.err.println(Arrays.toString(bs));
		out.write(bs);
		out.flush();

	}

	public boolean isClose() {
		return isClose;
	}
}
