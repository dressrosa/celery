/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.common.utils;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**2016年9月13日上午10:17:21
 * @author xiaoyu
 * @description  创建ConnectionFactory的枚举单例
 * @version 1.0
 */
public enum ActivemqUtils {

	Factory;

	ConnectionFactory factory = null;
	private final String userName = ActiveMQConnection.DEFAULT_USER;
	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private final String brokerURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	private ActivemqUtils() {
		factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
	}

	public ConnectionFactory getFactory() {
		return factory;
	}
}
