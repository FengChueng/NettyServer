package com.zyl.netty.server.decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.resp.DeviceResponseDateMsg;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * app下发时间参数0x42  盒子响应 0x43
 * 
 * @author Administrator
 *
 */
public class DecodeDateMsg implements ProtocolMsgBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(DecodeDateMsg.class);

	@Override
	public ProtocolMsg builder(ProtocolMsg msg, int contentlength, byte[] content) {
		String deviceNumber = NumberUtils.bytesToHexString(msg.getDeviceNumber());
		DeviceResponseDateMsg responseServerQueryDateMsg = new DeviceResponseDateMsg(msg.getDeviceNumber());
		if (content.length == 8) {
			responseServerQueryDateMsg.setStartDateLight(getContentTime(new byte[] { content[0], content[1] }));
			responseServerQueryDateMsg.setEndDateLight(getContentTime(new byte[] { content[2], content[3] }));
			responseServerQueryDateMsg.setStartDateWord(getContentTime(new byte[] { content[4], content[5] }));
			responseServerQueryDateMsg.setEndDateWord(getContentTime(new byte[] { content[6], content[7] }));
			LOGGER.info("----0x42 app设置时间,{}当前灯箱开关时间段:{}-{},----设备当前字体开关时间段:{}-{}", deviceNumber,
					responseServerQueryDateMsg.getStartDateLight(), responseServerQueryDateMsg.getEndDateLight(),
					responseServerQueryDateMsg.getStartDateWord(), responseServerQueryDateMsg.getEndDateWord());
		} else {
			LOGGER.error("设备{}上传时间异常", deviceNumber);
		}
		return responseServerQueryDateMsg;
	}

	/**
	 * 格式化字节数组，example: byte[]{0x19,0x20} - > 19:20
	 * 
	 * @param content
	 * @return
	 */
	private String getContentTime(byte[] content) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < content.length; i++) {
			String hex = NumberUtils.byteToHex(content[i]);
			str.append(hex);
			if (i == 0) {
				str.append(":");
			}
		}
		return str.toString();
	}

}
