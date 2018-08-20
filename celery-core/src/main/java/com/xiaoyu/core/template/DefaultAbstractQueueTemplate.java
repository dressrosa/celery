/*
 * 唯有读书,不庸不扰
 */
package com.xiaoyu.core.template;

import com.xiaoyu.core.MessageCallback;

/**
 * 2017年3月29日下午4:01:15
 * 
 * @author xiaoyu
 * @description 对queue模式的一种实现,通过构造函数来对destination进行指定, 通过实现
 *              {@link MessageCallback} 接口,将自身作为回调,将下层的具体业务逻辑实现,传入上层
 *              {@link BaseActivemqTemplate}进行处理 对某个具体业务实现时,需要继承此类,并对
 *              {@link MessageCallback#handleMessage()} 进行具体实现,使用时只需调用
 *              {@link BaseActivemqTemplate#produce(String)} 和
 *              {@link BaseActivemqTemplate#consume()}即可
 */
public abstract class DefaultAbstractQueueTemplate extends BaseActivemqTemplate implements MessageCallback {

    private final String destinationName;

    public DefaultAbstractQueueTemplate(String destinationName) {
        this.destinationName = destinationName;
        // 作为回调传入上层
        super.setCallback(this);

    }

    @Override
    protected BaseActivemqTemplate destination(Creator creator) {
        // 创建queue
        creator.createQueue(destinationName);
        return this;
    }

}
