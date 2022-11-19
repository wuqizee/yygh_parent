package com.atguigu.yygh.sms.listener;

import com.atguigu.yygh.mq.constant.MqConst;
import com.atguigu.yygh.sms.service.SMSService;
import com.atguigu.yygh.vo.sms.SmsVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wqz
 * @date 2022/11/16 15:20
 */
@Component
public class SMSListener {

    @Autowired
    private SMSService smsService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    exchange = @Exchange(name = MqConst.EXCHANGE_SMS, type = "direct"),
                    value = @Queue(MqConst.QUEUE_SMS),
                    key = {MqConst.ROUTING_KEY_SMS}
            )
    })
    public void consumer(SmsVo smsVo, Message message, Channel channel) {
        smsService.sendMessage(smsVo);
    }

}
