package com.zyl.netty.server.event;

import org.springframework.context.ApplicationContext;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.resp.ClockCheckMsg;
import com.zyl.netty.server.utils.ClientPool;

/**
 * 服务器响应设备查询时钟同步参数
 * @author zyl
 *
 */
public class SyscClockRespMsg extends Message {
	public SyscClockRespMsg(ApplicationContext applicationContext) {
	}

	@Override
	public void onMessage(Event event) throws Exception {
		if (!(event instanceof SyscClockEvt)) {
			// 事件继续向下传递
			return;
		}
		SyscClockEvt syscClockEvt = (SyscClockEvt) event;
		ProtocolMsg protocolMsg = syscClockEvt.getEvt();
		ClientPool.sendMessage(protocolMsg);
		ClockCheckMsg clockCheckMsg = (ClockCheckMsg) protocolMsg;
		LOGGER.info("设备响应 app下发时钟:{},{},{},{}",clockCheckMsg.getYear(),clockCheckMsg.getMonth(),clockCheckMsg.getDay(),clockCheckMsg.getTime());
	}

	@Override
	public int order() {
		return EvtPriority.EVENT_THIRTEENTH;
	}

	@Override
	public boolean continueOnError() {
		return false;
	}
}
