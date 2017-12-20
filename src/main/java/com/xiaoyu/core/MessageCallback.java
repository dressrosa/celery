/*
 *  唯有读书,不庸不扰
 */
package com.xiaoyu.core;

/**
 * 2017年3月29日下午4:42:40
 * 
 * @author xiaoyu
 * @description 用于消费者获取消息后对消息的业务逻辑处理
 */
public interface MessageCallback {

    /**
     * 具体业务逻辑的实现
     */
    public void handleMessage(String message);
}
