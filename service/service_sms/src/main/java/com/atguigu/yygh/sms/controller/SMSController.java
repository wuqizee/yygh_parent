package com.atguigu.yygh.sms.controller;

import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.sms.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wqz
 * @date 2022/11/10 16:39
 */
@RestController
@RequestMapping("/user/sms")
public class SMSController {

    @Autowired
    private SMSService smsService;

    @GetMapping("/send/{phone}")
    public R sendCode(@PathVariable String phone) {
        boolean flag = smsService.sendCode(phone);
        if (flag) {
            R.error();
        }
        return R.ok();
    }

}
