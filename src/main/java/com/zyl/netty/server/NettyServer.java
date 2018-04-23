package com.zyl.netty.server;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * author:zyl
 * date:2018年4月22日 下午10:40:45
 */

@Component
public class NettyServer implements ApplicationContextAware{
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private static ApplicationContext applicationContext = null;

    private Channel channel;

    private EventLoopGroup workerGroup;

    private EventLoopGroup bossGroup;

    private LogicServerHandler logicServerHandler;
    
    
    @PostConstruct
    public void start() throws InterruptedException{
        logger.info("start netty server...");
        int coreNum = new Double(Runtime.getRuntime().availableProcessors()*2 / 0.2).intValue();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(coreNum);
        logicServerHandler = new LogicServerHandler();
        
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .handler(new LoggingHandler(LogLevel.DEBUG))
        .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                .addLast(new IdleStateHandler(60, 60, 60,TimeUnit.SECONDS))
                .addLast(new StringDecoder(Charset.forName("UTF-8")))
                .addLast(new StringEncoder(Charset.forName("UTF-8")))
                .addLast(logicServerHandler);
            }
        });
        ChannelFuture cFuture = serverBootstrap.bind(6666).sync();
        channel = cFuture.channel();
        logger.info("netty start on port:9000");
    }
    
    @PreDestroy
    public void stop(){
        logger.info("destroy server resources");
        if (null == channel) {
            logger.error("server channel is null");
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        bossGroup = null;
        workerGroup = null;
        channel = null;
    }
    
    
    
    

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (NettyServer.applicationContext == null) {
            NettyServer.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("========ApplicationContext配置成功========");
        System.out.println("---------------------------------------------------------------------");
    }
    
     //获取applicationContext  
    public static ApplicationContext getApplicationContext() {  
       return applicationContext;  
    }  
  
    //通过name获取 Bean.  
    public static Object getBean(String name){  
       return getApplicationContext().getBean(name);  
  
    }  
  
    //通过class获取Bean.  
    public static <T> T getBean(Class<T> clazz){  
       return getApplicationContext().getBean(clazz);  
    }  
  
    //通过name,以及Clazz返回指定的Bean  
    public static <T> T getBean(String name,Class<T> clazz){  
       return getApplicationContext().getBean(name, clazz);  
    } 
    
}
