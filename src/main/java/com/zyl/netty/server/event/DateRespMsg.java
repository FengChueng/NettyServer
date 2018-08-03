package com.zyl.netty.server.event;

import org.springframework.context.ApplicationContext;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.utils.ClientPool;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * 
 * 设备处理app下发的时间参数
 * @author zyl
 *
 */
public class DateRespMsg extends Message {

	public DateRespMsg(ApplicationContext applicationContext) {
	}

	@Override
	public void onMessage(Event event) throws Exception {
		if (!(event instanceof DateEvt)) {
			// 事件继续向下传递
			return;
		}
		DateEvt dateEvt = (DateEvt) event;
		ProtocolMsg protocolMsg = dateEvt.getEvt();
		String deviceNumber = NumberUtils.bytesToHexString(protocolMsg.getDeviceNumber());
        LOGGER.info("设备{}响应设置时间命令",deviceNumber);
        ClientPool.sendMessage(protocolMsg);
	}

	@Override
	public int order() {
		return EvtPriority.EVENT_TWELFTH;
	}

	@Override
	public boolean continueOnError() {
		return false;
	}
}
