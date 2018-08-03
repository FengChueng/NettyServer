package com.zyl.netty.server;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zyl.netty.server.decode.ProtocolMsgFactory;
import com.zyl.netty.server.event.DateEvt;
import com.zyl.netty.server.event.EventThreadFactory;
import com.zyl.netty.server.event.HeartEvt;
import com.zyl.netty.server.event.SwitchEvt;
import com.zyl.netty.server.event.SyscClockEvt;
import com.zyl.netty.server.resp.HeartMsg;
import com.zyl.netty.server.utils.ChannelData;
import com.zyl.netty.server.utils.ClientPool;
import com.zyl.netty.server.utils.NumberUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;

/**
 * 监控服务器解码程序
 * 
 * @author Magic Fatty
 *
 */
public class MonitorDecoder extends ByteToMessageDecoder {
	private final static Logger LOGGER = LoggerFactory.getLogger(MonitorDecoder.class);
	private ProtocolMsgFactory protocolMsgFactory;
	private EventThreadFactory eventThreadFactory;
	private Map<String, String> deviceNumbers = new ConcurrentHashMap<String, String>();


	public MonitorDecoder(ApplicationContext applicationContext) {
		// commandFactory = applicationContext.getBean(CommandFactory.class);
		protocolMsgFactory = applicationContext.getBean(ProtocolMsgFactory.class);
		eventThreadFactory = applicationContext.getBean(EventThreadFactory.class);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
//		Object object = ctx.channel().attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).get();
//		if (object != null) {
//			String deviceNumber = object.toString();
//			LOGGER.warn("--------channelActive " + deviceNumber + "重新连接" + "id = "
//					+ ctx.channel().id().asLongText());
//		} else {
//			LOGGER.warn("--------channelActive " + "首次注册" + "id = " + ctx.channel().id().asLongText());
//		}
	}

	/**
	 * 盒子断线后触发
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		if (ctx == null) {
			LOGGER.error("ctx已关闭");
			return;
		}

		// 移除设备
		ClientPool.getInstance().getAllChannels().remove(ctx.channel());
		Object object = ctx.channel().attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).get();
		if (object != null) {
			String deviceNumber = object.toString();
			LOGGER.warn(deviceNumber + "失去连接" + "id = " + ctx.channel().id().asLongText());
			ClientPool.getInstance().delChannelId(deviceNumber);
		} else {
			LOGGER.warn("失去连接" + "id = " + ctx.channel().id().asLongText());
		}

		/*
		 * Calendar calendar = Calendar.getInstance(); int year =
		 * calendar.get(Calendar.YEAR); int month =
		 * calendar.get(Calendar.MONTH)+1; int day =
		 * calendar.get(Calendar.DAY_OF_MONTH);
		 */
		String id = ctx.channel().id().asLongText();
		if (deviceNumbers.containsKey(id)) {
			ClientPool.removeChannel(ctx.channel());
			// 在box断开连接后删除保存的数据，如果断开时需要同步业务，请再次同步
			String deviceNumber = deviceNumbers.remove(id);
			LOGGER.warn("--------------设备:" + deviceNumber + "已断开连接--------------");
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		String id = ctx.channel().id().asLongText();
		//一个数据包可以读取的字节数
		int readableBytes = in.readableBytes();
		
		if(readableBytes < 12){
			/**
			 * aa 0c 00 00 20 06 00 00 00 00
			 * 出现拆包,等待下一个包到达时,合并后读取。
			 */
			return;
		}
		
		// 防止Socket攻击
		if(readableBytes > 1024){
			//直接丢弃该包
			in.skipBytes(in.readableBytes());
			return;
		}			
		
		while (true) {
			/**
			 * [1] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
			 * [2] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
			 * [3] aa 0c 00 00 20 06 00 00 00 00
			 * singleBytes[1] = 14 + 14 +10;
			 * singleBytes[2] = 14 + 10;
			 * singleBytes[3] = 10;
			 * 当前可读字节数
			 */
			int currentReadableBytes = in.readableBytes();
//			System.out.println("---------readableBytes:" + byteCountAll);

			//获取之前存储的设备编号
			Object object = ctx.channel().attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).get();
			//设备编号
			String deviceNumberStr = object == null ? null:object.toString();
			
			/**
			 * 有可能出现拆包,故不能这样处理
			 */
//			if (currentReadableBytes < 12) {
//				if (object != null) {
//					deviceNumberStr = object.toString();
//				}
//				StringBuffer sb = new StringBuffer();
//				if (currentReadableBytes != 0) {
//					byte[] des = new byte[currentReadableBytes];
//					in.readBytes(des);
//					for (byte b : des) {
//						sb.append(NumberUtils.byteToHex(b));
//						sb.append(" ");
//					}
//					LOGGER.warn("{}收到错误包:{}.总长度={}",deviceNumberStr!=null?deviceNumberStr:id, sb.toString(), currentReadableBytes);
//				}
//				return;
//			}
			
			int firstIndex = in.readerIndex();
			int beginIndex = -1;
			int endIndex = -1;
			/**
			 * 处理粘包
			 */
			for (int i = firstIndex; i < firstIndex + currentReadableBytes; i++) {
				byte b = in.getByte(i);
				if (beginIndex == -1 && b == ProtocolMsg.MSG_START) {
					in.readerIndex(i);
					beginIndex = i;
					
					/**
					 * 记录一条消息的开始读取点
					 * 消息出现拆包时,即当前消息的部分数据在下一个包中,
					 * 需要将不完整的数据,放回至缓冲区,用于下次读取
					 */
					in.markReaderIndex();
				} else if (b == ProtocolMsg.MSG_END_2 && beginIndex != -1) {
					if (i + 1 < firstIndex + currentReadableBytes && in.getByte(i + 1) == ProtocolMsg.MSG_END_1) {
						endIndex = i + 1;
						break;
					}
				}
			}
			
			/**
			 * 处理拆包
			 */
			if(beginIndex == -1 && endIndex == -1){
				if(currentReadableBytes==0){
//					LOGGER.warn("{}收到空消息",deviceNumberStr);
					return;
				}
				/**
				 * 一个包的消息
				 * [1] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * [2] 0c 00 00 20 06 00 00 00 00 00 67
				 * 形如第2列的数据,没有包头和包尾,丢弃该消息,结束对该包的解析
				 */
				LOGGER.warn("{}未读到包头和包尾,丢弃该消息",deviceNumberStr);
				return;
			}else if(beginIndex == -1){
				/**
				 * [1] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * [2] 	  0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * 形如第2列的数据,没有包头,丢弃该消息,结束对该包的解析
				 */
				LOGGER.warn("{}未读到包头,丢弃该消息.endIndex={}",deviceNumberStr,endIndex);
				return;
			}else if(endIndex == -1){
				/**
				 * [1] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * [2] aa 0c 00 00 20 06 00 00 00 00 00 67
				 * 形如第2列的数据,没有包尾,出现拆包信息
				 * [2]中消息在下一个包中
				 */
				// 还原到上述标记位置  
                in.resetReaderIndex();  
                // 缓存当前剩余的buffer数据，等待剩下一个数据包到来合并后读取
                return;
			}
			
			if (endIndex - beginIndex + 1 < 12) {
				/**
				 * [1] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * [2] aa 0c 00 00 20 06 00 00 00 00 5a a5
				 * [3] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * 形如第2列的数据,有包头有包尾,但是数据不完整,丢弃该数据
				 */
				// 错误包
				in.readerIndex(endIndex + 1);
				LOGGER.warn("{}有包头有包尾,但是数据不完整,丢弃该数据:beginIndex={},endIndex={}",deviceNumberStr!=null?deviceNumberStr:id,beginIndex,endIndex);
				continue;
			}
			
			// 还原到上述beginIndex位置,同  in.readerIndex(beginIndex);
//            in.resetReaderIndex();
			in.readerIndex(beginIndex);
			// (1)解析盒子上传的消息
			ProtocolMsg protocolMsg = new ProtocolMsg();
			in.readByte();// 包头
			// 获取包长
			protocolMsg.setLength(in.readByte());
			protocolMsg.setCurrentDate(new Date());
			// 获取类型
			byte type = in.readByte();
			protocolMsg.setType(type);
			// 获取命令
			short command = in.readShort();
			protocolMsg.setCommand(command);
			// 获取内容长度
			byte contentLength = in.readByte();
			contentLength -= 6;
			// 获取device number
			byte[] deviceNumber = new byte[6];
			in.readBytes(deviceNumber);
			protocolMsg.setDeviceNumber(deviceNumber);
			
			deviceNumberStr = NumberUtils.bytesToHexString(deviceNumber);
			
			if (contentLength == protocolMsg.getLength() - 12
					&& protocolMsg.getLength() + 2 == endIndex - beginIndex + 1) {
				// (2)解析盒子上传的数据
				if (contentLength > 0) {
					byte[] bs = new byte[contentLength];
					in.readBytes(bs);
					// 解析设备返回的数据。如电流、电压、电能、功率、开关状态、开关时间、报警信息
					if (protocolMsgFactory.exist(command)) {
						protocolMsg = protocolMsgFactory.getProtocolMsg(command).builder(protocolMsg, contentLength,
								bs);
					} else {
						LOGGER.warn("Can not found ProtocolMsgBuilder from ProtocolMsgFactory,{}",NumberUtils.byteToHex((byte)command));
					}
				}
			} else {
				/**
				 * [1] aa 0c 00 00 20 06 00 00 00 00 00 67
				 * [2] aa 0c 00 00 20 06 00 00 00 00 00 67 5a a5
				 * 形如第1列的数据,没有包尾
				 * 此时数据在处理粘包时,就会从[1]的aa读取至[2] 5a a5,导致数据解析异常
				 */
				in.readerIndex(endIndex + 1);
				LOGGER.warn("0x{}收到{}错误包(粘包时,有包头无包尾),firstIndex={},beginIndex={},endIndex={}", Integer.toHexString(command),
						deviceNumberStr, firstIndex, beginIndex, endIndex);
				continue;
			}
			
			// 结束字符,5AA5
			short msg_end = in.readShort();
			if (msg_end == ProtocolMsg.MSG_END) {
				out.add(protocolMsg);
				deviceNumbers.put(id, NumberUtils.bytesToHexString(deviceNumber));
				// 注册channel到连接池
				ClientPool.putNewChannel(NumberUtils.bytesToHexString(deviceNumber), ctx.channel());
				preprocessDeviceInfo(ctx, deviceNumberStr);
				processRequest(protocolMsg, command, deviceNumber);
			} else {
				LOGGER.error("illegal command");
			}
		}
	}

	private void preprocessDeviceInfo(ChannelHandlerContext ctx, String deviceNumberStr) {
		// 查询是否已有设备
		if (!ClientPool.getInstance().getAllChannels().contains(ctx.channel())) {
			ClientPool.getInstance().getAllChannels().add(ctx.channel());
		}

		Object object = ctx.channel().attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).get();
		if (object == null) {
			ChannelData channelData = ClientPool.getInstance().getAllChannelIds().get(deviceNumberStr);
			if (channelData == null) {
				channelData = new ChannelData(ctx.channel().id());
				ClientPool.getInstance().addChannelId(deviceNumberStr, channelData);
				ctx.channel().attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).set(deviceNumberStr);
			} else {
				// 设备已经有注册
				if (!ctx.channel().id().equals(channelData.getChannelId())) {
					Channel channel = ClientPool.getInstance().getAllChannels().find(channelData.getChannelId());
					if (channel != null) {
						channel.attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).set(null);
						ClientPool.getInstance().getAllChannels().remove(channel);
					}
					channelData.setChannelId(ctx.channel().id());
					ClientPool.getInstance().addChannelId(deviceNumberStr, channelData);
					ctx.channel().attr(AttributeKey.valueOf(ClientPool.CHANNEL_MAC_ATTR)).set(deviceNumberStr);
				}
			}
		}
	}

	private void processRequest(ProtocolMsg protocolMsg, short command, byte[] deviceNumber) {
		switch (command) {
		/** 1.app下发命令 */
		// (1)content无数据
		case ProtocolMsg.MSG_TYPE_HEART_RESPOSE:// 0x21
			eventThreadFactory.post(new HeartEvt(ProtocolMsg.MSG_TYPE_HEART, new HeartMsg(deviceNumber)));
			break;
		// (2)content有数据	
		case ProtocolMsg.MSG_TYPE_CHECK_TIME_RESPONSE:// 0x91
		    //app下发时钟
			eventThreadFactory.post(new SyscClockEvt(ProtocolMsg.MSG_TYPE_CHECK_TIME, protocolMsg));
			break;
		case ProtocolMsg.TIME_SERVER_REQ:// 0x42
		    //app下发时间
			eventThreadFactory.post(new DateEvt(ProtocolMsg.TIME_CLIENT_RESPONSE, protocolMsg));
			break;
		case ProtocolMsg.MSG_TYPE_CONTROL:// 0x60
		    //app控制开关
			eventThreadFactory.post(new SwitchEvt(ProtocolMsg.MSG_TYPE_CONTROL_RESPONSE, protocolMsg));
			break;
		default:
			LOGGER.error("Network Content has been unsupport");
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		super.exceptionCaught(ctx, cause);
        Channel incoming = ctx.channel();
        if(!incoming.isActive()){
//        super.exceptionCaught(ctx, cause);
            LOGGER.error(ctx.channel().remoteAddress().toString(), cause);
            ctx.close();
        }

	}
}
