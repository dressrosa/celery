/*
 *   唯有读书,不庸不扰
 */
package com.xiaoyu.modules.samples.bizdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoyu.modules.samples.bizdemo.api.BizService;

/**
 * 2017年3月29日下午5:33:15
 * 
 * @author xiaoyu
 * @description 模拟一个业务,执行完后像用户回执一个邮件
 */
@RestController
public class BizController {

    @Autowired
    private BizService bizService;

    @RequestMapping("register")
    public String register(@RequestParam(required = true) String phone) {
        return bizService.register(phone);
    }
}
