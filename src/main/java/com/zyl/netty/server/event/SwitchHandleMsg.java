package com.zyl.netty.server.event;

import org.springframework.context.ApplicationContext;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.resp.SwitchMsg;
import com.zyl.netty.server.utils.ClientPool;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * 处理设备上传开关状态(服务器查询开关状态/服务器控制开关状态/设备主动上传开关状态)
 * 
 * @author zyl
 *
 */
public class SwitchHandleMsg extends Message {

	public SwitchHandleMsg(ApplicationContext applicationContext) {
	}

	@Override
	public void onMessage(Event event) throws Exception {
		if (!(event instanceof SwitchEvt)) {
			// 事件继续向下传递
			return;
		}

		SwitchEvt switchEvt = (SwitchEvt) event;
		ProtocolMsg protocolMsg = switchEvt.getEvt();
		ClientPool.sendMessage(protocolMsg);
		String deviceNumber = NumberUtils.bytesToHexString(protocolMsg.getDeviceNumber());
		int lightSwitch = 0;
		int wordSwitch = 0;
		SwitchMsg switchMsg60 = (SwitchMsg) protocolMsg;
        lightSwitch = switchMsg60.isOpenLight();
        wordSwitch = switchMsg60.isOpenWord();
        LOGGER.info("设备响应开关控制:{},{},{}",deviceNumber,lightSwitch,wordSwitch);
	}

	@Override
	public int order() {
		return EvtPriority.EVENT_EIGHTEENTH;
	}

	@Override
	public boolean continueOnError() {
		return false;
	}
}
