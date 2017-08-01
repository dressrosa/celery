/*
 *   唯有读书,不庸不扰
 */
package com.xiaoyu.modules.samples.email.service;

import org.springframework.stereotype.Service;

import com.xiaoyu.modules.samples.email.EmailHandler;
import com.xiaoyu.modules.samples.email.api.EmailService;

/**
 * 2017年3月29日下午4:16:41
 * 
 * @author xiaoyu
 * @description
 * @version 1.0
 */
@Service
public class EmailServiceImpl implements EmailService {

	private static final EmailHandler handler = EmailHandler.instance();

	public String putContentToMq(String content) {
		try {
			handler.produce(content);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "put success";
	}

}
