/*
 * 唯有读书,不庸不扰
 */
package com.xiaoyu.core.template;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.log4j.Logger;

import com.xiaoyu.core.ActivemqFactory;
import com.xiaoyu.core.MessageCallback;
import com.xiaoyu.modules.samples.email.EmailSchedule;

/**
 * 2017年3月29日下午3:15:57
 * 
 * @author xiaoyu
 * @description
 */
public abstract class ActivemqTemplate {
	private static final Logger logger = Logger.getLogger(ActivemqTemplate.class);
	private Session session = null;
	private Topic topic = null;
	private Message msg = null;
	private MessageProducer producer = null;
	private MessageConsumer consumer = null;

	public void produce(String message) {
		final ConnectionFactory factory = ActivemqFactory.INSTANCE.factory();
		Connection connection = null;
		try {
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Creator creator = new Creator();
			this.topic(creator);
			producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			msg = session.createTextMessage(message);
			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	private MessageCallback callback;

	protected void setCallback(MessageCallback callback) {
		this.callback = callback;
	}

	public void consume() {
		final ConnectionFactory factory = ActivemqFactory.INSTANCE.factory();
		Connection connection = null;
		try {
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Creator creator = new Creator();
			this.topic(creator);
			consumer = session.createConsumer(topic);
			Message msg = consumer.receiveNoWait();
			if (msg != null)
				callback.handleMessage(((TextMessage) msg).getText());
			// consumer.setMessageListener(new MessageListener() {
			// @Override
			// public void onMessage(Message message) {
			// try {
			// callback.handleMessage(((TextMessage) message).getText());
			// } catch (JMSException e) {
			// e.printStackTrace();
			// }
			// }
			// });
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

	/**
	 * 2017年3月29日下午3:59:29
	 * 
	 * @author xiaoyu
	 * @description 创建topic
	 */
	protected class Creator {
		public void createTopic(String topicName) {
			try {
				if (session != null)
					topic = session.createTopic(topicName);
			} catch (JMSException e) {
				e.printStackTrace();
			}

		}

	}

	protected abstract ActivemqTemplate topic(Creator creator);

}
