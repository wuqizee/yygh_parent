package com.atguigu.yygh.sms.service;

import com.atguigu.yygh.vo.sms.SmsVo;

/**
 * @author wqz
 * @date 2022/11/10 16:39
 */
public interface SMSService {

    boolean sendCode(String phone);

    void sendMessage(SmsVo smsVo);

}
