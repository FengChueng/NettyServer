package com.zyl.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * author:zyl
 * date:2018年4月22日 下午10:36:12
 */

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.zyl.netty" })
@SpringBootApplication
public class NettyServerApp {

    public static void main(String[] args) {
        new SpringApplication(NettyServerApp.class).run(args);
    }
    
}
