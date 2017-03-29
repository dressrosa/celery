/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.samples.learndemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoyu.modules.samples.learndemo.api.ConsumerService;
import com.xiaoyu.modules.samples.learndemo.api.ProducerService;

@RestController
public class MqTestController {

	@Autowired
	private ConsumerService consumerService;

	@Autowired
	private ProducerService producerService;

	@RequestMapping("/")
	public String main() {
		return "hello world";
	}

	@RequestMapping("receiveNoSpringJms")
	public String receiveNoSpringJms() {
		this.consumerService.receiveNoSpringJms();
		return "receiveNoSpringJms:" + "ok";
	}

	@RequestMapping("receiveAndSendTo")
	public String receiveAndSendTo() {
		String msg = null;
		return this.consumerService.receiveAndSendTo(msg);
	}

	@RequestMapping("receiveWithSpringJms")
	public String receiveWithSpringJms() {
		String msg = "1234";
		this.consumerService.receiveWithSpringJms(msg);
		return "receiveWithSpringJms:ok";
	}

	@RequestMapping("sendNoSpringJms")
	public String producerNoSpringJms(String msg) {
		this.producerService.sendNoSpringJms(msg);
		return "sendNoSpringJms:ok";
	}

	@RequestMapping("sendWithSpringJms")
	public String sendWithSpringJms(String msg) {
		this.producerService.sendWithSpringJms(msg);
		return "sendWithSpringJms:ok";
	}

	@RequestMapping("sendWithTopic")
	public String sendWithTopic(String msg) {
		this.producerService.sendWithTopic(msg);
		return "sendWithTopic:ok";
	}

}
