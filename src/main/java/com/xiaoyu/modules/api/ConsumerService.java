/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.api;

public interface ConsumerService {

	public void receiveNoSpringJms();

	public void receiveWithSpringJms(String msg);

	public void receiveScheduledWithSB(String msg);

	public String receiveAndSendTo(String msg);

	public void receiveWithTopic();
}
