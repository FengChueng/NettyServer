package com.zyl.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 监控程序编码器
 * 
 * @author Magic Fatty
 *
 */
public class MonitorEncoder extends MessageToByteEncoder<ProtocolMsg> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolMsg msg, ByteBuf out) throws Exception {
		if (msg == null) {
			throw new Exception("The encode message is null");
		}
		sendMsg(msg, out);
	}

	// 将制定消息发送到黑盒子
	private void sendMsg(ProtocolMsg msg, ByteBuf out) {
		out.writeByte(ProtocolMsg.MSG_START);

		out.writeByte(msg.getContentLength() + 6+1+2+1+2);
		out.writeByte(msg.getType());
		out.writeShort(msg.getCommand());
		out.writeByte(msg.getContentLength()+6);
		out.writeBytes(msg.getDeviceNumber());

		if (msg.getContent() != null) {
			out.writeBytes(msg.getContent());
		}

		out.writeByte(ProtocolMsg.MSG_END_2);
		out.writeByte(ProtocolMsg.MSG_END_1);
	}
}
