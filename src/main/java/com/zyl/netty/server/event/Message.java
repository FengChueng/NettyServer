package com.zyl.netty.server.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件处理类
 * @author laiiihan
 *
 */
public abstract class Message implements Comparable<Message>{
	protected Logger LOGGER = LoggerFactory.getLogger(getClass()); 
	/**
	 * 接收到事件时触发的方法
	 * @param event
	 * @param applicationContext
	 * @throws Exception
	 */
	public abstract void onMessage(Event event)throws Exception;
	/**
	 * 发生异常后是否扩散继续扩散事件,默认为true
	 * @return
	 */
	public boolean continueOnError(){
		return true;
	}
	/**
	 * 消息顺序
	 * @return
	 */
	public int order(){
		return EvtPriority.EVENT_DEFAULT;
	}
	@Override
	public int compareTo(Message o) {
		if(o==null)return -1;
		return order() - o.order();
	}
	/**
	 * 事件是否被消費。
	 * @return true:代表被消費,false:未被消费,继续传递
	 */
	public boolean isConsume(){
		return false;
	}
	
}
