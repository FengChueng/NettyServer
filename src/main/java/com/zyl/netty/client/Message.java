package com.zyl.netty.client;

import com.zyl.netty.server.utils.NumberUtils;

public class Message {
	private byte[] deviceNumber;
	private byte[] command;
	private byte[] content;
	private byte contentLength;
	private byte packageLength;
	private byte length;
	private byte type= 0x00;

	public Message(){

	}

	public Message(String deviceNumber,byte [] command,byte [] content){
		this(NumberUtils.hexStringToBytes(deviceNumber),command,content);
	}

	public Message(byte[] deviceNumber,byte[] command,byte[] content) {
		this.deviceNumber = deviceNumber;
		this.command = command;
		this.contentLength = (byte) (content == null ? 0 : content.length);
		this.packageLength = (byte) (1+2+1+6+contentLength+2);
		length = (byte) (6+contentLength);
		if(content == null){
		    content = new byte[]{};
		}
		this.content = content;
	}
	public byte[] getDeviceNumber() {
		return deviceNumber;
	}
	public byte[] getCommand() {
		return command;
	}
	public byte[] getContent() {
		return content;
	}
	public byte getPackageLength() {
		return packageLength;
	}
	public byte getLength() {
		return length;
	}
	public byte getType() {
		return type;
	}
	public byte getContentLength() {
		return contentLength;
	}
	public void setContent(byte[] content) {
		this.content = content;
		this.contentLength = (byte) (content == null ? 0 : content.length);
		this.packageLength = (byte) (1+2+1+6+contentLength+2);
		this.length = (byte) (6+contentLength);
	}
}
