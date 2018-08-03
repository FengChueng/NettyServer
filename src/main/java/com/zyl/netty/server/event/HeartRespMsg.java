package com.zyl.netty.server.event;

import org.springframework.context.ApplicationContext;

import com.zyl.netty.server.ProtocolMsg;
import com.zyl.netty.server.utils.ClientPool;
import com.zyl.netty.server.utils.NumberUtils;

/**
 * 处理app发送的心跳包
 * 
 * @author zyl
 *
 */
public class HeartRespMsg extends Message {


    public HeartRespMsg(ApplicationContext applicationContext) {
    }

    @Override
    public void onMessage(Event event) throws Exception {
        if (!(event instanceof HeartEvt)) {
            // 事件继续向下传递
            return;
        }

        HeartEvt heartEvt = (HeartEvt) event;
        ProtocolMsg protocolMsg = heartEvt.getEvt();
        // 响应设备
        ClientPool.sendMessage(protocolMsg);

        String deviceNumber = NumberUtils.bytesToHexString(protocolMsg.getDeviceNumber());
        
        LOGGER.info("设备响应心跳包:{}",deviceNumber);
    }

    @Override
    public int order() {
        return EvtPriority.EVENT_SECOND;
    }

    @Override
    public boolean continueOnError() {
        return false;
    }

}
