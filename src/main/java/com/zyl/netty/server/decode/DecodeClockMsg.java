package com.zyl.netty.server.decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.resp.ClockCheckMsg;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * app下发时钟 0x91,设备响应0x90
 * 
 * @author Administrator
 *
 */
public class DecodeClockMsg implements ProtocolMsgBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(DecodeClockMsg.class);

	@Override
	public ProtocolMsg builder(ProtocolMsg msg, int contentlength, byte[] content) {
		String deviceNumber = NumberUtils.bytesToHexString(msg.getDeviceNumber());
		ClockCheckMsg clockCheckMsg = new ClockCheckMsg(msg.getDeviceNumber());
		if (content.length == 7) {
		    StringBuffer date = new StringBuffer();
		    String year = NumberUtils.bytesToHexString(new byte[]{content[0],content[1]});
            date.append(year+"-");
            String month = NumberUtils.bytesToHexString(new byte[]{content[2]});
            date.append(month+"-");
            String day = NumberUtils.bytesToHexString(new byte[]{content[3]});
            date.append(day+" ");
            String time = getContentTime(new byte[]{content[4],content[5],content[6]});
            date.append(time);
            LOGGER.info("0x91--------------【收到app下发的时钟。当前时间:"+date+"】--------------");
            
            clockCheckMsg.setYear(year);
            clockCheckMsg.setMonth(month);
            clockCheckMsg.setDay(day);
            clockCheckMsg.setTime(time);
            
		} else {
			LOGGER.error("设备{}上传时间异常", deviceNumber);
		}

		return clockCheckMsg;
	}

	/**
	 * 格式化字节数组，example: byte[]{0x19,0x20,0x30} - > 19:20:30
	 * 
	 * @param content
	 * @return
	 */
	public static String getContentTime(byte[] content) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < content.length; i++) {
			String hex = NumberUtils.byteToHex(content[i]);
			str.append(hex);
			if (i !=2 ) {
				str.append(":");
			}
		}
		return str.toString();
	}

}
