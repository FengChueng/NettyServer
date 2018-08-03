package com.zyl.netty.server.event;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {
	@Resource
	private EventThreadFactory eventThreadFactory;
	@Resource
	private ApplicationContext applicationContext;

	@PostConstruct
	public void postConstruct() {
		// app 发送心跳包 0x21
		eventThreadFactory.register(new HeartRespMsg(applicationContext));// 2
		// app 发送时钟 0x91
		eventThreadFactory.register(new SyscClockRespMsg(applicationContext));// 13
		
		// 0x42 app下发时间参数
		eventThreadFactory.register(new DateRespMsg(applicationContext));// 12
		// 0x60 app下发开关参数
		eventThreadFactory.register(new SwitchHandleMsg(applicationContext));// 17
		

	}
}
