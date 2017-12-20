/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.samples.learndemo.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.stereotype.Service;

import com.xiaoyu.core.ActivemqFactory;
import com.xiaoyu.modules.samples.learndemo.api.ConsumerService;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void receiveNoSpringJms() {
        Destination destination = null;
        MessageConsumer consumer = null;
        Connection connection = null;
        try {
            final ConnectionFactory factory = ActivemqFactory.INSTANCE.factory();

            connection = factory.createConnection();
            connection.start();// 消费者这句不能空 否则接收不到
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("xiaoyu.second");
            consumer = session.createConsumer(destination);
            // 设置监听器后 session connection都不能主动close,否则导致监听失效
            consumer.setMessageListener(new MessageListener() {// 监听获取
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("receiveNoSpringJms:xiaoyu.second:" + ((TextMessage) message).getText());
                    } catch (final JMSException e) {
                        e.printStackTrace();
                    }

                }
            });
            // for (;;) {// 一直等待获取
            // TextMessage mess = (TextMessage) consumer.receive(5000);
            // if (mess != null) {
            // System.out.println(mess.getText());
            // } else
            // break;
            // }
        } catch (final JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    // @JmsListener(destination = "xiaoyu.third")
    public void receiveWithSpringJms(String msg) {
        System.out.println("接收消息xiaoyu.third::" + msg);
    }

    @Override
    // @JmsListener(destination = "xiaoyu.first")
    public void receiveScheduledWithSB(String msg) {
        System.out.println("定时接收消息xiaoyu.first:" + msg);
    }

    // @JmsListener(destination = "xiaoyu.fourth")
    // @SendTo(value = "xiaoyu.first")
    @Override
    public String receiveAndSendTo(String msg) {
        return " (xiaoyu.second:)" + msg;
    }

    // @Scheduled(fixedRate = 10000)
    @Override
    public void receiveWithTopic() {
        Topic topic = null;
        MessageConsumer consumer = null;
        Connection connection = null;
        try {
            final ConnectionFactory factory = ActivemqFactory.INSTANCE.factory();
            connection = factory.createConnection();
            connection.start();
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic("xiaoyu.topic.second");
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("receiveWithTopic:xiaoyu.topic.second " + ((TextMessage) message).getText());
                    } catch (final JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (final JMSException e) {
            e.printStackTrace();
        }

    }

}
