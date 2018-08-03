package com.zyl.netty.server.utils;

public class DeviceException extends RuntimeException{
	private static final long serialVersionUID = 7578567483018388777L;
	private String key;
	public DeviceException(String key,String msg){
		super(msg);
		this.key = key;
	}
	public String getKey() {
		return key;
	}
}
