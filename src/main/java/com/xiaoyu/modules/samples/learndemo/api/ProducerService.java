/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.samples.learndemo.api;

public interface ProducerService {

	public void sendNoSpringJms(String msg);

	public void sendWithSpringJms(String msg);

	public void sendScheduledWithSB();

	public void sendWithTopic(String msg);

}
