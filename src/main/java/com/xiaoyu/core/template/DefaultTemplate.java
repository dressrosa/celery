/*
 * 唯有读书,不庸不扰
 */
package com.xiaoyu.core.template;

import com.xiaoyu.core.MessageCallback;

/**
 * 2017年3月29日下午4:01:15
 * 
 * @author xiaoyu
 * @description
 * @version 1.0
 */
public abstract class DefaultTemplate extends ActivemqTemplate implements MessageCallback {

	private String topicName;

	public DefaultTemplate(String topicName) {
		this.topicName = topicName;
		super.setCallback(this);
	}

	@Override
	protected ActivemqTemplate topic(Creator creator) {
		creator.createTopic(topicName);
		return this;
	}

}
