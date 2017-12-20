/*
 * 唯有读书,不庸不扰
 */
package com.xiaoyu.core.template;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xiaoyu.core.ActivemqFactory;
import com.xiaoyu.core.MessageCallback;

/**
 * 2017年3月29日下午3:15:57
 * 
 * @author xiaoyu
 * @description 对消息的收发进行封装
 */
abstract class ActivemqTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(ActivemqTemplate.class);

    // 线程池来异步进行业务级别的消息处理
    private static final ExecutorService msgHandlerPool = Executors.newFixedThreadPool(50);

    private Session session = null;// 多线程情况下导致session关闭 在同时有生产和消费的情况下,
    // 因为生产者发送玩关闭,导致消费者出现session空异常 因为封装在一起,实际情况下消费者单独处理就行了
    // 改为threadlocal解决session问题
    private final ThreadLocal<Session> sLocal = new InheritableThreadLocal<>();
    // private Topic topic = null;
    // private Queue queue = null;
    private Destination destination = null;
    private Message msg = null;
    // private MessageProducer producer = null;//多线程情况下导致producer关闭
    private MessageConsumer consumer = null;

    /**
     * push消息到mq里面
     */
    public void produce(String message) {
        final ConnectionFactory factory = ActivemqFactory.INSTANCE.factory();
        Connection connection = null;
        Creator creator = null;
        try {
            if (sLocal.get() == null) {
                connection = factory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                sLocal.set(session);
                creator = new Creator();
                this.destination(creator);
            }
            final MessageProducer producer = sLocal.get().createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            msg = sLocal.get().createTextMessage(message);
            producer.send(msg);
        } catch (final JMSException e) {
            LOG.error("error:", e);
        } finally {
            // 只要消费生产在一起 connection就不能关闭
            // logger.info("关闭连接");
            // try {
            // if (connection != null)
            // connection.close();
            // } catch (JMSException e) {
            // e.printStackTrace();
            // }
        }
    }

    // 持有一个消息回调,用于下层业务逻辑的实现
    private MessageCallback callback;

    protected void setCallback(MessageCallback callback) {
        this.callback = callback;
    }

    /**
     * 从mq取消息并异步进行处理
     */
    public void consume() {
        final ConnectionFactory factory = ActivemqFactory.INSTANCE.factory();
        Connection connection = null;
        Creator creator = null;
        try {
            if (sLocal.get() == null) {
                connection = factory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                sLocal.set(session);
                creator = new Creator();
                this.destination(creator);
            }

            consumer = sLocal.get().createConsumer(destination);

            // Message msg = null;
            // for (;;) {//阻塞获取消息
            // msg = consumer.receive();
            // if (msg != null)
            // callback.handleMessage(((TextMessage) msg).getText());
            // }
            // 异步监听并存入pool中进行业务处理
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(final Message message) {
                    msgHandlerPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 处理下层具体的业务逻辑
                                callback.handleMessage(((TextMessage) message).getText());
                            } catch (final JMSException e) {
                                LOG.error("error:", e);
                            }
                        }
                    });

                }
            });

        } catch (final JMSException e) {
            LOG.error("error:", e);
        } finally {
            // 监听模式下connection不能关闭 关闭后监听就结束了
            // logger.info("关闭连接");
            // try {
            // if (connection != null)
            // connection.close();
            // } catch (JMSException e) {
            // e.printStackTrace();
            // }
        }
    }

    /**
     * 2017年3月29日下午3:59:29
     * 
     * @author xiaoyu
     * @description 由下层来具体实现创建destination
     */
    protected class Creator {

        public void createTopic(String topicName) {
            try {
                if (sLocal.get() != null) {
                    destination = sLocal.get().createTopic(topicName);
                }
            } catch (final JMSException e) {
                LOG.error("error:", e);
            }
        }

        public void createQueue(String queueName) {
            try {
                if (sLocal.get() != null) {
                    destination = sLocal.get().createQueue(queueName);
                }
            } catch (final JMSException e) {
                LOG.error("error:", e);
            }

        }

    }

    /**
     * 用于具体实现选择topic还是queue
     */
    protected abstract ActivemqTemplate destination(Creator creator);

}
