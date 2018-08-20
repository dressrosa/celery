/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.samples.learndemo.config;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;

/**
 * 2016年9月13日上午10:18:16
 * 
 * @author xiaoyu
 * @description spring集成activemq
 * @version 1.0
 */
// @Configuration
// @EnableJms
public class ActivemqConfig {

    @Bean
    public Queue queue() {
        final Queue queue = new ActiveMQQueue("xiaoyu.first");
        return queue;
    }

    @Bean
    public ActiveMQConnectionFactory factory() {
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
        return factory;
    }
}
