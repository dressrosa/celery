/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.biz.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import com.xiaoyu.common.utils.ActivemqUtils;
import com.xiaoyu.modules.api.ProducerService;

@Service
@EnableScheduling
public class ProducerServiceImpl implements ProducerService {

	@Autowired
	private JmsMessagingTemplate template;

	@Autowired
	private Queue queue;

	@Override
	public void sendNoSpringJms(String msg) {
		ConnectionFactory factory = ActivemqUtils.Factory.getFactory();

		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageProducer producer = null;
		try {
			connection = factory.createConnection();
			// connection.start();//生产者这行可有可无
			// 如果设置为true为事务型,之后需要session.commit();才能提交消息
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("xiaoyu.second");
			producer = session.createProducer(destination);
			TextMessage mess = session.createTextMessage(msg);
			producer.send(mess);
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

	// @Scheduled(fixedDelay = 15000)
	public void sendScheduledWithSB() {
		String msg = " a beautiful girl ";
		this.template.convertAndSend(queue, msg);
	}

	@Override
	public void sendWithSpringJms(String msg) {
		Queue queue = new ActiveMQQueue("xiaoyu.third");
		this.template.convertAndSend(queue, msg);
	}

	@Override
	public void sendWithTopic(String msg) {

		ConnectionFactory factory = ActivemqUtils.Factory.getFactory();
		Connection connection = null;
		try {
			connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("xiaoyu.topic.second");
			MessageProducer producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			Message message = session.createTextMessage(msg);
			producer.send(message);
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

}
