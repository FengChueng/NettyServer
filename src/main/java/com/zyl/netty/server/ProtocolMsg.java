package com.zyl.netty.server;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 消息.
 * 
 * @author hepeng
 *
 */
public class ProtocolMsg implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    // 0x10 设备注册
	// 0x11 服务器应答注册成功
	// 0x20 设备上传心跳包
	// 0x21 服务器应答心跳包
	// 0x22 设备上传n(服务器下发的判断时长参数,0x93)个小时内的耗电量
	// 0x23 服务器响应设备
	// 0x30 设备上传灯箱功率
	// 0x31 服务器应答收到设备上传灯箱功率数据
	// 0x32 设备上传灯箱电压、电流数据
	// 0x33 设备上传功率
	// 0x40 设备查询控制时间段参数
	// 0x41 服务器下传控制时间段参数
	// 0x50 服务器查询设备控制时间段参数
	// 0x51 设备上传控制时间段参数
	// 0x60 服务器远程控制灯箱开或关
	// 0x61 设备应答服务器的远程控制灯箱
	// 0x70 设备上传灯箱开或关状态
	// 0x71 服务器应答设备上传灯箱电源状态
	// 0x80 服务器查询设备下的灯箱开或关状态
	// 0x81 设备应答服务灯箱开或关的状态
	// 0x90 设备请求校验时钟
	// 0x91 服务器应答设备时钟校验
	public final static short MSG_TYPE_REGISTER = (short) 0x0010;// 0x10 设备注册
	public final static short MSG_TYPE_REGISTER_SUCCESS = (short) 0x0011;// 0x11 服务器应答注册成功
	
	/**
     * author :zyl
     * date   :2017-8-24
     * description:设备当前网络连接类型
     */
	public final static short MSG_TYPE_CONN = (short) 0x0012;// 0x12 设备上传网络连接类型
	public final static short MSG_TYPE_CONN_RESPONSE = (short) 0x0013;// 0x13 服务器应答
	
	
	/**
	 * author :zyl
	 * date   :2017-11-23
	 * description:设备版本
	 */
    public final static short MSG_TYPE_DEVICE_VERSION = (short) 0x0014;// 0x14 设备上传设备版本
    public final static short MSG_TYPE_DEVICE_VERSION_RESPONSE = (short) 0x0015;// 0x15 服务器应答
	
	
	public final static short MSG_TYPE_HEART = (short) 0x0020;// 0x20 设备上传心跳包
	public final static short MSG_TYPE_HEART_RESPOSE = (short) 0x0021;// 0x21 服务器应答心跳包
	
	/**add 2017-6-6*/
	public final static short MSG_TYPE_CHECK_ENERGY_ONE = 0x0022;//0x22 设备上传n(服务器下发的判断时长参数,0x93)个小时内的端口一的耗电量
	/**add 2017-6-6*/
	public final static short MSG_TYPE_CHECK_ENERGY_ONE_RESPONSE = 0x0023;//服务器响应0x23
	
	/**add 2017-7-16*/
	public final static short MSG_TYPE_CHECK_ENERGY_TWO = 0x0024;//0x24 设备上传n(服务器下发的判断时长参数,0x93)个小时内的端口二耗电量
	/**add 2017-7-16*/
	public final static short MSG_TYPE_CHECK_ENERGY_TWO_RESPONSE = 0x0025;//服务器响应0x25
	
	
	public final static short MSG_TYPE_ENERGY = (short) 0x0030;// 0x30 设备上传电能
	public final static short MSG_TYPE_ENERGY_RESPONSE = (short) 0x0031;// 0x31 服务器应答收到设备上传电能数据
	public final static short MSG_TYPE_VI = (short) 0x0032;// 0x32 设备上传灯箱电压、电流数据	
	/**add 2017-4-10*/
	public final static short MSG_TYPE_POWER = (short) 0x0033;//0x33 设备上传功率
	
	public final static short MSG_TYPE_GET_TIME = (short) 0x0040;// 0x40 设备查询控制时间段参数
	public final static short MSG_TYPE_TIME_RESPONSE = (short) 0x0041;// 0x41  服务器响应并下传控制时间段参数
	/** modify 2018-7-18*/
	public final static short TIME_SERVER_REQ = (short) 0x0042;// 0x42 服务器设置时间段参数
	public final static short TIME_CLIENT_RESPONSE = (short) 0x0043;// 0x43 设备响应
	
	public final static short MSG_TYPE_QUERY_TIME = (short) 0x0050;// 0x50 服务器查询设备控制时间段参数
	public final static short MSG_TYPE_QUERY_TIME_RESPONSE = (short) 0x0051;// 0x51 设备上传控制时间段参数
	public final static short MSG_TYPE_CONTROL = (short) 0x0060;// 0x60 服务器远程控制灯箱开或关
	public final static short MSG_TYPE_CONTROL_RESPONSE = (short) 0x0061;// 0x61 设备应答服务器的远程控制灯箱
	
	/** modify 2017-7-19*/
	public final static short MSG_TYPE_REPORT_SWITCH_ONE = (short) 0x0070;// 0x70 设备上传端口一(灯箱)开关状态
	/** modify 2018-7-18*/
	public final static short SWITCH_ONE_SERVER_RESPONSE = (short) 0x0072;// 0x72 服务器响应设备上传端口一(灯箱)开关状态
	
	/** modify 2017-7-19*/
	public final static short MSG_TYPE_REPORT_SWITCH_TWO = (short) 0x0071;// 0x71 设备上传端口二(字体)开关状态
	/** modify 2018-7-18*/
	public final static short SWITCH_TWO_SERVER_RESPONSE = (short) 0x0073;// 0x73服务器响应 设备上传端口二(字体)开关状态
	
	
	
	public final static short MSG_TYPE_QUERY_SWITCH = (short) 0x0080;// 0x80 服务器查询设备下的灯箱开或关状态
	public final static short MSG_TYPE_QUERY_SWITCH_RESPONSE = (short) 0x0081;// 0x81 设备应答服务灯箱开或关的状态
	public final static short MSG_TYPE_CHECK_TIME = (short) 0x0090;// 0x90 设备请求校验时钟
	public final static short MSG_TYPE_CHECK_TIME_RESPONSE = (short) 0x0091;// 0x91 服务器应答设备时钟校验
	
	/**modify 2017-7-18*/
	public final static short MSG_TYPE_SET_TIME_ONE = (short) 0x0092;//0x92 服务器下发端口一(灯箱)判断时长
	/**modify 2017-7-18*/
	public final static short MSG_TYPE_SET_TIME_TWO = (short) 0x0093;//0x93 服务器下发端口一(灯箱)判断时长
	
	
	public final static short MSG_TYPE_REPORT_WARNING = (short) 0x0094;//0x94 设备上传报警信息,根据电能判断
	public final static short MSG_TYPE_REPORT_WARNING_RESPONSE = (short) 0x0095;//0x95 服务器响应设备上传报警信息
	
	public final static short MSG_TYPE_SET_MAIN_IP = (short) 0x0096;//0x96 服务器设置设备访问的主IP地址
	public final static short MSG_TYPE_SET_MAIN_IP_RESPONSE = (short) 0x0097;//0x97 盒子响应服务器
	
	public final static short MSG_TYPE_SET_SECONDARY_IP = (short) 0x0098;//0x98 服务器设置设备访问的备用IP地址
	public final static short MSG_TYPE_SET_SECONDARY_IP_RESPONSE = (short) 0x0099;//0x99 盒子响应服务器

	public final static byte MSG_START = (byte) 0xAA;
	public final static byte MSG_END_2 = (byte) 0x5A;
	public final static byte MSG_END_1 = (byte) 0xA5;
	
	public final static short MSG_END = (short) 0x5AA5;

	private Date currentDate; //记录消息的接收时间
	private int length; // 长度 = 1+2+1+6+内容长度+2
	private byte type = (byte) 0x00; // 消息类型
	private short command; // 命令号
	private byte[] deviceNumber; // 6字节设备号
	public ProtocolMsg() {
		currentDate = new Date();
	}
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public short getCommand() {
		return command;
	}
	public void setCommand(short command) {
		this.command = command;
	}
	public int getContentLength() {
		return 0;
	}
	public byte[] getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(byte[] deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public byte[] getContent() {
		
		return new byte[]{};
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	/**
	 * 如果这个ProtocolMsg对象可以直接用于处理一个来自于黑盒子的请求，那么返回true 只有当命令参数为
	 * ProtocolMsg.MSG_TYPE_QUERY_TIME_RESULT}；
	 * ProtocolMsg.MSG_TYPE_CONTROL_RESPONSE；
	 * ProtocolMsg.MSG_TYPE_QUERY_CONTROL_RESPONS； 时，返回false
	 * 
	 * @return
	 */
	public boolean isResponse() {
		List<Short> commonds = Arrays.asList(ProtocolMsg.MSG_TYPE_QUERY_TIME_RESPONSE,
				ProtocolMsg.MSG_TYPE_CONTROL_RESPONSE, ProtocolMsg.MSG_TYPE_QUERY_SWITCH_RESPONSE);
		return !commonds.contains(this.getCommand());
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("currentDate:".concat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentDate)).concat(","));
		str.append("length:");
		str.append(length);
		str.append(",");
		str.append("type:");
		str.append(type);
		str.append(",");
		str.append("command:");
		str.append(command);
		str.append(",");
		str.append("deviceNumber:");
		str.append(Arrays.toString(deviceNumber));
		return str.toString();
	}
	/**
	    private Date currentDate; //记录消息的接收时间
		private int length; // 长度 = 1+2+1+6+内容长度+2
		private byte type = (byte) 0x00; // 消息类型
		private short command; // 命令号
		private int contentLength; // 内容长度+6
		private byte[] deviceNumber; // 6字节设备号
		private byte[] content;
	 */
}

