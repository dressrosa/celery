package com.xiaoyu.modules.samples.email;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailSchedule {

	private static final Logger logger = Logger.getLogger(EmailSchedule.class);

	private static final EmailHandler handler = EmailHandler.instance();

	@Scheduled(fixedRate = 5000) // 每几秒执行一次
	public void getMqContentToSend() {
		try {
			logger.info("定时任务开始执行.....");
			handler.consume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
