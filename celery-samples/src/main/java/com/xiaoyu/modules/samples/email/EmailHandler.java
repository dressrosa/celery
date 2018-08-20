/*
 *  唯有读书,不庸不扰
 */
package com.xiaoyu.modules.samples.email;

import java.util.concurrent.TimeUnit;

import com.xiaoyu.core.template.DefaultAbstractQueueTemplate;

/**
 * 2017年3月29日下午5:50:56
 * 
 * @author xiaoyu
 * @description 邮件业务的实现,继承queue类型,设定好destination和业务逻辑
 */
public class EmailHandler extends DefaultAbstractQueueTemplate {

    private EmailHandler(String destination) {
        super(destination);
    }

    /*
     * 根据
     */
    @Override
    public void handleMessage(String message) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("回执给用户的注册邮件,内容:-----" + message);
    }

    private static final class Handler {
        public static final EmailHandler INSTANCE = new EmailHandler("xiaoyu.email");
    }

    public static EmailHandler instance() {
        return Handler.INSTANCE;
    }
}
