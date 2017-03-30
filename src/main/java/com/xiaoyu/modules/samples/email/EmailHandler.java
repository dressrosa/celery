/*
 *  唯有读书,不庸不扰
 */
package com.xiaoyu.modules.samples.email;

import com.xiaoyu.core.template.DefaultAbstractQueueTemplate;

/**
 * 2017年3月29日下午5:50:56
 * 
 * @author xiaoyu
 * @description
 * @version 1.0
 */
public class EmailHandler extends DefaultAbstractQueueTemplate {

	private EmailHandler(String destination) {
		super(destination);
	}

	@Override
	public void handleMessage(String message) {
		System.out.println("回执给用户的注册邮件,内容:" + message);
	}

	private static final class Handler {
		public static final EmailHandler INSTANCE = new EmailHandler("xiaoyu.email");
	}

	public static EmailHandler instance() {
		return Handler.INSTANCE;
	}
}
