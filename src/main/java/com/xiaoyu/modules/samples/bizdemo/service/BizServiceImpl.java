/*
 *   唯有读书,不庸不扰
 */
package com.xiaoyu.modules.samples.bizdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyu.modules.samples.bizdemo.api.BizService;
import com.xiaoyu.modules.samples.email.api.EmailService;

/**
 * 2017年3月29日下午4:16:41
 * 
 * @author xiaoyu
 * @description
 * @version 1.0
 */
@Service
public class BizServiceImpl implements BizService {

    @Autowired
    private EmailService emailService;

    @Override
    public String register(String phone) {
        final String loginName = phone;
        System.out.println("业务处理,存入数据库成功...");
        // 模拟进行进行邮件推送
        emailService.putContentToMq(loginName + ",您好!欢迎来到召唤师峡谷,I'm xiaoyu,祝您乐在其中");
        return "register success";
    }

}
