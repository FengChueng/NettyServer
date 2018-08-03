package com.zyl.netty.server.utils;

import io.netty.channel.ChannelId;

public class ChannelData {

	private ChannelId channelId;
	
	public ChannelData(){}
		
	public ChannelData(ChannelId channelId){
		this.channelId = channelId;
	}

	public ChannelId getChannelId() {
		return channelId;
	}

	public void setChannelId(ChannelId channelId) {
		this.channelId = channelId;
	}
}
