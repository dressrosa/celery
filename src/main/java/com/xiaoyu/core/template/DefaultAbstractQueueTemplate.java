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
public abstract class DefaultAbstractQueueTemplate extends ActivemqTemplate implements MessageCallback {

	private String destination;

	public DefaultAbstractQueueTemplate(String destination) {
		this.destination = destination;
		super.setCallback(this);
	}

	@Override
	protected ActivemqTemplate destination(Creator creator) {
		creator.createQueue(destination);
		return this;
	}

}
