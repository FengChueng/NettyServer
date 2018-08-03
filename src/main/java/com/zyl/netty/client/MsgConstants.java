package com.zyl.netty.client;

public interface MsgConstants {
	byte MSG_START=(byte) 0xaa;
	byte MSG_END2=(byte) 0x5a;
	byte MSG_END1=(byte) 0xa5;

//	byte MSG_TYPE_HEART = (byte) 0x0020;// 0x20 设备上传心跳包
//	byte MSG_TYPE_HEART_RESPOSE = (byte) 0x0021;// 0x21 服务器应答心跳包
	

	   /** modify 2018-7-18*/
//	byte TIME_SERVER_REQ = (byte) 0x0042;// 0x42 服务器设置时间段参数
//	byte TIME_CLIENT_RESPONSE = (byte) 0x0043;// 0x43 设备响应
	

//	byte MSG_TYPE_CONTROL = (byte) 0x60;// 0x60 服务器远程控制灯箱开或关
//	byte MSG_TYPE_CONTROL_RESPONSE = (byte) 0x0061;// 0x61 设备应答服务器的远程控制灯箱


//	byte MSG_TYPE_CHECK_TIME = (byte) 0x0090;// 0x90 设备请求校验时钟
//	byte MSG_TYPE_CHECK_TIME_RESPONSE = (byte) 0x0091;// 0x91 服务器应答设备时钟校验

	byte [] MSG_TYPE_HEART_SEND = {0x00, (byte) 0x21};//0x21 发送心跳包
	byte [] MSG_TYPE_HEART_RESPONSE = {0x00, (byte) 0x20};// 0x20 设备应答

	byte [] MSG_TYPE_SET_TIME_SEND = {0x00, (byte) 0x42};//0x42 APP设置开关时间
	byte [] MSG_TYPE_SET_TIME_RESPONSE = {0x00, (byte) 0x43};// 0x43 设备应答



	byte [] MSG_TYPE_CONTROL_SEND = {0x00, (byte) 0x60};//0x60 APP控制灯箱开或关
	byte [] MSG_TYPE_CONTROL_RESPONSE = {0x00, (byte) 0x61};// 0x61 设备应答

	byte [] MSG_TYPE_CHECK_SEND = {0x00, (byte) 0x91};//APP主动下发时钟
	byte [] MSG_TYPE_CHECK_RESPONSE = {0x00, (byte) 0x90};//设备响应

	byte REGISTER=0x10;
}
