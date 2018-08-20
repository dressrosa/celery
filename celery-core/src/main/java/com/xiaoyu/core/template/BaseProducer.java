/**
 * 
 */
package com.xiaoyu.core.template;

import java.util.HashMap;
import java.util.Map;

import com.xiaoyu.core.constant.Type;

/**
 * 通用生产者
 * 
 * @author hongyu
 * @date 2018-08
 * @description 适合无需处理消息,只是生产消息的场景
 */
public class BaseProducer {

    /**
     * 每个消息类型生成一个template
     */
    private static final Map<String, BaseActivemqTemplate> Pool = new HashMap<>(16);

    private static final class BaseQueue extends BaseActivemqTemplate {
        private final String destinationName;

        public BaseQueue(String destinationName) {
            this.destinationName = destinationName;
        }

        @Override
        protected BaseActivemqTemplate destination(Creator creator) {
            creator.createQueue(destinationName);
            return this;
        }
    }

    private static final class BaseTopic extends BaseActivemqTemplate {
        private final String destinationName;

        public BaseTopic(String destinationName) {
            this.destinationName = destinationName;
        }

        @Override
        protected BaseActivemqTemplate destination(Creator creator) {
            creator.createTopic(destinationName);
            return this;
        }
    }

    /**
     * @param type
     *            消息类型
     * @param destination
     *            topic or queue name
     * @param message
     *            消息
     */
    public static void produce(Type type, String destination, String message) throws Exception {
        BaseActivemqTemplate mq = Pool.get(destination);
        if (mq != null) {
            mq.produce(message);
            return;
        }
        if (type == Type.TOPIC) {
            BaseTopic topic = new BaseTopic(destination);
            topic.produce(message);
            Pool.put(destination, topic);
        } else if (type == Type.QUEUE) {
            BaseQueue queue = new BaseQueue(destination);
            queue.produce(message);
            Pool.put(destination, queue);
        }
    }

}
