package com.zyl.netty.server.event;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EventThreadFactory {
	private List<Message> messages = new Vector<Message>();//事件处理集合
	private final static ExecutorService SINGLETHREADEXECUTOR = Executors.newSingleThreadExecutor();
	
	
	public void register(Message message){
		messages.add(message);
		Collections.sort(messages);
	}
	public void post(Event event){
		SINGLETHREADEXECUTOR.execute(new MessageThread(messages, event));
	}
}
class MessageThread implements Runnable{
	private final static Logger LOG = Logger.getLogger(MessageThread.class);
	private List<Message> messages;
	private Event event;
	public MessageThread(List<Message> messages,Event event) {
		this.messages = messages;
		this.event = event;
	}
	@Override
	public void run() {
		Iterator<Message> iterator=messages.iterator();
		while (iterator.hasNext()) {
			Message message = (Message) iterator.next();
			try {
				
				message.onMessage(event);
				
				
			} catch (Exception e) {
				LOG.error("事件传递时，出现异常",e);
				if(!message.continueOnError()){
					break;
				}
			}
		}
	}
}
