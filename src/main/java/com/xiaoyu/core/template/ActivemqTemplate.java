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

import org.apache.log4j.Logger;

import com.xiaoyu.core.ActivemqFactory;
import com.xiaoyu.core.MessageCallback;

/**
 * 2017年3月29日下午3:15:57
 * 
 * @author xiaoyu
 * @description 对消息的收发进行封装
 */
public abstract class ActivemqTemplate {

	private static final Logger logger = Logger.getLogger(ActivemqTemplate.class);

	// 线程池来异步进行业务级别的消息处理
	private static final ExecutorService msgHandlerPool = Executors.newFixedThreadPool(8);

	private Session session = null;// 多线程情况下导致session关闭 在同时有生产和消费的情况下,
	// 因为生产者发送玩关闭,导致消费者出现session空异常 因为封装在一起,实际情况下消费者单独处理就行了

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
		try {
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Creator creator = new Creator();
			this.destination(creator);
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			msg = session.createTextMessage(message);
			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			logger.info("关闭连接");
			try {
				if (connection != null)
					connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
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
		try {
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Creator creator = new Creator();
			this.destination(creator);

			consumer = session.createConsumer(destination);

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
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					});

				}
			});

		} catch (JMSException e) {
			e.printStackTrace();
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
				if (session != null)
					destination = session.createTopic(topicName);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		public void createQueue(String queueName) {
			try {
				if (session != null)
					destination = session.createQueue(queueName);

			} catch (JMSException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 用于具体实现选择topic还是queue
	 */
	protected abstract ActivemqTemplate destination(Creator creator);

}
