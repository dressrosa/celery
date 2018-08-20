/*
 *  唯有读书,不庸不扰
 */
package com.xiaoyu.modules.samples.email.api;

/**
 * 2017年3月29日下午4:15:26
 * 
 * @author xiaoyu
 * @description
 */
public interface EmailTestService {

    /**
     * 将邮件内容推送到mq中
     */
    public String putContentToMq(String content);

}
