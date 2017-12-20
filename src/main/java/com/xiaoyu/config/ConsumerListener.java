/*
 * 唯有读书,不庸不扰
 * 
 */
package com.xiaoyu.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.xiaoyu.modules.samples.email.EmailHandler;

/**
 * 2017年3月31日下午4:31:40
 * 
 * @author xiaoyu
 * @description 启动时执行消费者进行消费
 */
@Component
public class ConsumerListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        EmailHandler handler = EmailHandler.instance();
        handler.consume();
        System.out.println("消费者已启动进行消费...");
    }

}
