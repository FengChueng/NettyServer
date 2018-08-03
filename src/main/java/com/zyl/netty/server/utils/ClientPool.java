package com.zyl.netty.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyl.netty.server.ProtocolMsg;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 客户端存放池，用于记录deviceNum注册的channel
 * @author laiiihan
 *
 */
public class ClientPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientPool.class);
	private static final Map<String,Channel> CHANNEL_POOL = new ConcurrentHashMap<String,Channel>();
	private static final Map<String,String> DEVICENUM_POOL = new ConcurrentHashMap<String,String>();
	
	public final static String CHANNEL_MAC_ATTR = "deviceNumber";
	private DefaultChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	
	private Map<String, ChannelData> channelDatas = new HashMap<>();

	private static ClientPool instance;

	private ClientPool() {

	}

	public static ClientPool getInstance() {
		if (instance == null) {
			synchronized (ClientPool.class) {
				if (instance == null) {
					instance = new ClientPool();
				}
			}
		}
		return instance;
	}

	public DefaultChannelGroup getAllChannels() {
		return allChannels;
	}
	
	
	
	public Map<String, ChannelData> getAllChannelIds() {
		return channelDatas;
	}

	public void addChannelId(String deviceNumberStr, ChannelData data) {
		if (data != null)
			channelDatas.put(deviceNumberStr, data);
	}

	public void delChannelId(String deviceNumberStr) {
		channelDatas.remove(deviceNumberStr);
	}
	
	
	
	/**
	 * 注册新客户端
	 * @param deviceNum
	 * @param channel
	 */
	public static void putNewChannel(String deviceNum,Channel channel){
		CHANNEL_POOL.put(deviceNum, channel);
		DEVICENUM_POOL.put(channel.id().asLongText(), deviceNum);
	}
	/**
	 * 客户端断开连接后删除
	 * @param deviceNum
	 */
	public static void removeChannel(Channel channel){
		ChannelId channelId = channel.id();
		String deviceNum = DEVICENUM_POOL.remove(channelId.asLongText());
		if(deviceNum!=null){
			channel = CHANNEL_POOL.remove(deviceNum);
			try {
				if(channel!=null){
					channel.close();
				}
			} catch (Exception e) {
				LOGGER.warn("Close channel failed.",e);
			}
		}
	}
	
	public static void sendMessage(ProtocolMsg protocolMsg) {
		String deviceNum = NumberUtils.bytesToHexString(protocolMsg.getDeviceNumber());
		if(CHANNEL_POOL.containsKey(deviceNum)){
			CHANNEL_POOL.get(deviceNum).writeAndFlush(protocolMsg);
		}else{
			throw new DeviceException("device","断开连接");
		}
	}
	
	/**
	 * 发送报文
	 * @param deviceNum
	 * @param protocolMsg
	 */
	public static void sendMessage(String deviceNum,ProtocolMsg protocolMsg) throws DeviceException{
		if(CHANNEL_POOL.containsKey(deviceNum)){
			CHANNEL_POOL.get(deviceNum).writeAndFlush(protocolMsg);
		}else{
			throw new DeviceException("device","断开连接");
		}
	}
	/**
	 * 发送报文
	 * @param deviceNum
	 * @param protocolMsg
	 */
	public static void sendMessage(byte[] deviceNum,ProtocolMsg protocolMsg) throws DeviceException{
		sendMessage(NumberUtils.bytesToHexString(deviceNum), protocolMsg);
	}
	/**
	 * 获取在线列表
	 * @return
	 */
	public static List<String> onlines(){
		return new ArrayList<>(ClientPool.CHANNEL_POOL.keySet());
	}
	
	/**
	 * 根据devicNum 判断设备是否在线
	 * @param deviceNum
	 * @return
	 */
	public static boolean isOnline(String deviceNum){
		if(CHANNEL_POOL.containsKey(deviceNum)){
			return true;
		}
		return false;
	}
}
