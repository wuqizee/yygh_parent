package com.atguigu.yygh.order.listener;

import com.atguigu.yygh.mq.constant.MqConst;
import com.atguigu.yygh.order.service.OrderInfoService;
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
 * @date 2022/11/18 18:48
 */
@Component
public class OrderListener {

    @Autowired
    private OrderInfoService orderInfoService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    exchange = @Exchange(name = MqConst.EXCHANGE_TASK, type = "direct"),
                    value = @Queue(name = MqConst.QUEUE_TASK),
                    key = {MqConst.ROUTING_KEY_TASK}
            )
    })
    public void consumer(Message message, Channel channel) {
        orderInfoService.patientTips();
    }

}
