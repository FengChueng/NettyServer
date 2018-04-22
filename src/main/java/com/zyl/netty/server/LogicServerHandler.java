package com.zyl.netty.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * author:zyl
 * date:2018年4月22日 下午11:04:41
 */
public class LogicServerHandler extends SimpleChannelInboundHandler<String>{
    private static Logger logger = LoggerFactory.getLogger(LogicServerHandler.class);
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("channelRegistered被调用,id:{},ip:{}",ctx.channel().id().asLongText(),socketAddress.getAddress().getHostAddress());
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelUnregistered被调用,id:{}",ctx.channel().id().asLongText());
    }
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("handlerAdded被调用,id:{}",ctx.channel().id().asLongText());
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("handlerRemoved被调用,id:{}",ctx.channel().id().asLongText());
    }


    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelReadComplete被调用,id:{}",ctx.channel().id().asLongText());
    }
    
    

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("userEventTriggered被调用,id:{}",ctx.channel().id().asLongText());
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                logger.info("userEventTriggered被调用,state=READER_IDLE");
                ctx.writeAndFlush("客户端超过60s未长传数据");
                ctx.close();
//                throw new Exception("idle exception");
            }else {
                logger.info("userEventTriggered被调用,state={}",IdleState.ALL_IDLE.name());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("channelActive被调用,id:{},ip:{}",ctx.channel().id().asLongText(),socketAddress.getAddress().getHostAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead被调用,id:{}",ctx.channel().id().asLongText());
    }
    
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("收到客户端消息:"+msg);
        logger.info("receive :{}" ,msg);
    }

    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("channelInactive被调用,id:{},ip:{}",ctx.channel().id().asLongText(),socketAddress.getAddress().getHostAddress());
    }
    
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("exceptionCaught 被调用");
        Channel incoming = ctx.channel();
        if(!incoming.isActive()){
//        super.exceptionCaught(ctx, cause);
            logger.error(ctx.channel().remoteAddress().toString(), cause);
            ctx.close();
        }
        
    }
    
}
